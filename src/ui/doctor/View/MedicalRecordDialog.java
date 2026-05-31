package ui.doctor.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import ui.doctor.Controller.MedicalRecordController;
import ui.doctor.Model.MedicalRecordModel;
import ui.doctor.Model.TreatmentStageModel;

public class MedicalRecordDialog extends JDialog {

    private final MedicalRecordController controller;
    private final MedicalRecordModel record;
    private final boolean isEditMode; // Cờ phân biệt Thêm mới hay Cập nhật

    private JTextField txtPatientName;
    private JTextField txtDisease;
    private JTextField txtStartDate;
    private JComboBox<RouteItem> cboRoutes; 
    private boolean saved = false;
    
    // ======================================================================
    // 🌟 THÀNH PHẦN MỚI: Các Component phục vụ hiển thị giai đoạn điều trị
    // ======================================================================
    private JPanel stagesPanel;            // Panel chứa danh sách các bước động
    private JButton btnNextStage;          // Nút bấm "Chuyển giai đoạn"
    private List<TreatmentStageModel> activeStages; // Lưu list bước đang hiển thị

    public MedicalRecordDialog(Window owner, MedicalRecordController controller, MedicalRecordModel record) {
        super(owner, record == null ? "Thêm hồ sơ bệnh án" : "Cập nhật hồ sơ bệnh án", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        
        // Nếu truyền vào null -> Tạo model rỗng phục vụ tính năng Thêm mới
        this.isEditMode = (record != null);
        this.record = isEditMode ? record : new MedicalRecordModel(0, "", "", "", 0, "", "");

        initUI();
        loadRouteList(); // Nạp danh sách lộ trình vào ComboBox trước
        loadData();      // Đổ dữ liệu vào các ô nhập (Nếu là Edit)
        
        // Kích hoạt nạp danh sách các bước điều trị lần đầu tiên dựa trên lộ trình được chọn
        triggerInitialStagesLoad();

        // Tăng chiều cao Dialog từ 420 lên 580 để vừa vặn cho khu vực danh sách giai đoạn điều trị
        setSize(520, 580); 
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 248, 252));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        JLabel lblTitle = new JLabel(!isEditMode ? "THÊM HỒ SƠ" : "CẬP NHẬT HỒ SƠ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 51, 102));
        root.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        txtPatientName = new JTextField();
        addField(formPanel, gbc, row++, "Họ tên bệnh nhân:", txtPatientName);

        txtDisease = new JTextField();
        addField(formPanel, gbc, row++, "Bệnh điều trị:", txtDisease);

        txtStartDate = new JTextField(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
        addField(formPanel, gbc, row++, "Ngày bắt đầu (dd/mm/yyyy):", txtStartDate);

        cboRoutes = new JComboBox<>();
        addField(formPanel, gbc, row++, "Chọn lộ trình:", cboRoutes);

        // ----------------------------------------------------------------------
        // 🌟 THIẾT KẾ KHU VỰC HIỂN THỊ CÁC BƯỚC ĐIỀU TRỊ ĐỘNG
        // ----------------------------------------------------------------------
        gbc.gridx = 0; gbc.gridy = row++;
        gbc.gridwidth = 2; // Chiếm toàn bộ 2 cột trái và phải
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; // 🌟 ĐÃ THÊM: Cho phép List giãn nở theo chiều dọc không bị bẹp
        gbc.fill = GridBagConstraints.BOTH;
        
        stagesPanel = new JPanel();
        stagesPanel.setLayout(new BoxLayout(stagesPanel, BoxLayout.Y_AXIS));
        stagesPanel.setBackground(Color.WHITE);
        
        // Tạo viền có tiêu đề nổi bật cho danh sách
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1), 
                " Các bước điều trị chưa hoàn thành ", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), new Color(0, 51, 102)
        );
        stagesPanel.setBorder(titledBorder);
        
        // Bọc danh sách vào một JScrollPane nhỏ để tránh tràn giao diện khi có nhiều bước
        JScrollPane scrollStages = new JScrollPane(stagesPanel);
        scrollStages.setPreferredSize(new Dimension(440, 150));
        scrollStages.setMinimumSize(new Dimension(440, 150)); // 🌟 ĐÃ THÊM: Chốt cứng size tối thiểu
        scrollStages.setBorder(null);
        formPanel.add(scrollStages, gbc);

        // Sự kiện thay đổi lựa chọn trên ComboBox Lộ trình -> Load lại danh sách bước tương ứng
        cboRoutes.addActionListener(e -> {
            RouteItem selected = (RouteItem) cboRoutes.getSelectedItem();
            if (selected != null) {
                refreshStagesList(selected.getId());
            }
        });

        root.add(formPanel, BorderLayout.CENTER);

        // Khởi tạo các nút bấm ở thanh điều khiển phía dưới thanh Dialog
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setOpaque(false);

        // Nút chuyển giai đoạn (Chỉ khả dụng và hiển thị logic khi có dữ liệu)
        btnNextStage = new JButton("Chuyển giai đoạn");
        btnNextStage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNextStage.setBackground(new Color(230, 126, 34)); // Màu cam đặc trưng tiến trình
        btnNextStage.setForeground(Color.WHITE);
        btnNextStage.setFocusPainted(false);
        btnNextStage.addActionListener(e -> advanceCurrentStage());
        buttonPanel.add(btnNextStage);

        JButton btnSave = new JButton("Lưu lại");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setBackground(new Color(46, 204, 113)); // Màu xanh lá lưu dữ liệu
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> saveRecord());
        buttonPanel.add(btnSave);
        
        root.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadRouteList() {
        cboRoutes.removeAllItems();
        List<RouteItem> routes = controller.getRouteList(); 
        for (RouteItem r : routes) {
            cboRoutes.addItem(r);
        }
    }

    private void loadData() {
        if (isEditMode) {
            txtPatientName.setText(record.getPatientName());
            txtPatientName.setEditable(false); // Đã là bệnh án cũ thì không nên sửa tên bệnh nhân
            txtDisease.setText(record.getDisease());
            txtStartDate.setText(record.getStartDate());
            
            // Tự động chọn đúng Lộ trình hiện tại trong ComboBox bằng ID
            for (int i = 0; i < cboRoutes.getItemCount(); i++) {
                RouteItem item = cboRoutes.getItemAt(i);
                if (item.getId() == record.getTreatmentRouteId()) {
                    cboRoutes.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void triggerInitialStagesLoad() {
        RouteItem selected = (RouteItem) cboRoutes.getSelectedItem();
        if (selected != null) {
            refreshStagesList(selected.getId());
        }
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridwidth = 1; gbc.weightx = 0.0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comp.setPreferredSize(new Dimension(250, 30));
        panel.add(comp, gbc);
    }

    // ======================================================================
    // 🌟 HÀM MỚI: Tải và vẽ giao diện động danh sách các bước
    // ======================================================================
    private void refreshStagesList(int routeId) {
        stagesPanel.removeAll(); // Xóa sạch giao diện các bước cũ
        
        // Gọi Controller lấy danh sách bước chưa "Hoàn thành" từ cơ sở dữ liệu
        activeStages = controller.getActiveStagesByRoute(routeId);
        
        if (activeStages == null || activeStages.isEmpty()) {
            JLabel lblDone = new JLabel("Toàn bộ lộ trình điều trị này đã hoàn thành!");
            lblDone.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblDone.setForeground(new Color(39, 174, 96));
            lblDone.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            stagesPanel.add(lblDone);
            
            btnNextStage.setEnabled(false); // Hết giai đoạn thì tắt nút chuyển đi sếp
        } else {
            btnNextStage.setEnabled(true);
            
            for (TreatmentStageModel stage : activeStages) {
                // Tạo câu chuỗi định dạng: Tên bước | Trạng thái
                String text = String.format("• %s   [%s]", stage.getStageName(), stage.getStatus());
                if (stage.getNote() != null && !stage.getNote().isEmpty()) {
                    text += " - Ghi chú: " + stage.getNote();
                }
                
                JLabel lblStage = new JLabel(text);
                lblStage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lblStage.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                
                // Trực quan hóa: Đánh dấu màu xanh đậm cho giai đoạn "Đang thực hiện" hiện tại
                if ("Đang thực hiện".equals(stage.getStatus())) {
                    lblStage.setForeground(new Color(41, 128, 185));
                    lblStage.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    lblStage.setForeground(Color.GRAY); // Chưa thực hiện thì để màu xám mờ
                }
                stagesPanel.add(lblStage);
            }
        }
        
        // Yêu cầu Swing cập nhật vẽ lại giao diện mới tức thì
        stagesPanel.revalidate();
        stagesPanel.repaint();
    }

    // ======================================================================
    // 🌟 HÀM MỚI: Thực thi logic sự kiện bấm nút "Chuyển giai đoạn"
    // ======================================================================
    private void advanceCurrentStage() {
        if (activeStages == null || activeStages.isEmpty()) return;

        // Giai đoạn đầu tiên trong mảng chưa hoàn thành luôn là giai đoạn đang được xử lý
        TreatmentStageModel currentStage = activeStages.get(0);
        RouteItem selectedRoute = (RouteItem) cboRoutes.getSelectedItem();

        if (selectedRoute == null) return;

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Xác nhận hoàn thành giai đoạn [" + currentStage.getStageName() + "] và chuyển sang bước tiếp theo?",
            "Xác nhận chuyển đổi tiến trình", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Gọi lệnh tiến trình từ Controller xử lý cập nhật trạng thái kép dưới CSDL
            boolean isAdvanced = controller.advanceStage(currentStage.getId(), selectedRoute.getId());
            if (isAdvanced) {
                JOptionPane.showMessageDialog(this, "Hệ thống đã tự động nhảy sang giai đoạn tiếp theo!");
                refreshStagesList(selectedRoute.getId());
                this.saved = true; // Đánh dấu là có thay đổi dữ liệu
                MedicalRecordView.refreshData(); // Cập nhật lại giao diện, ẩn ngay bước cũ đi
            } else {
                JOptionPane.showMessageDialog(this, "Chuyển giai đoạn thất bại! Sếp vui lòng kiểm tra lại DB.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveRecord() {
        String patientName = txtPatientName.getText().trim();
        String disease = txtDisease.getText().trim();
        String startDate = txtStartDate.getText().trim();
        RouteItem selected = (RouteItem) cboRoutes.getSelectedItem();

        if (patientName.isEmpty() || disease.isEmpty() || selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Đổ dữ liệu vào Model
        record.setPatientName(patientName);
        record.setDisease(disease);
        record.setStartDate(startDate);
        record.setTreatmentRouteId(selected.getId());

        boolean success;
        if (isEditMode) {
            success = controller.updateRecord(record, selected.getId());
        } else {
            success = controller.addRecord(record, selected.getId());
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this, "Đã lưu thông tin hồ sơ bệnh án thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu thất bại! Xin vui lòng kiểm tra lại kết nối Database.", "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public static class RouteItem {
        private final int id;
        private final String name;
        public RouteItem(int id, String name) { this.id = id; this.name = name; }
        public int getId() { return id; }
        @Override public String toString() { return name; }
    }
}