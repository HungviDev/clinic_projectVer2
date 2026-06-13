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
    private final boolean isEditMode; // Kiểm tra dựa trên ID bệnh án (> 0 là Edit)

    private JTextField txtPatientName;
    private JTextField txtDisease;
    private JTextField txtStartDate;
    
    // 🌟 THAY ĐỔI: Chuyển từ JComboBox sang JTextField để ghim cứng lộ trình
    private JTextField txtRouteName; 
    private int selectedRouteId; // Biến lưu ngầm ID lộ trình được ghim
    
    private boolean saved = false;

    // Các Component phục vụ hiển thị giai đoạn điều trị
    private JPanel stagesPanel; 
    private JButton btnNextStage; 
    private List<TreatmentStageModel> activeStages; 

    public MedicalRecordDialog(Window owner, MedicalRecordController controller, MedicalRecordModel record) {
        // Tiêu đề dựa trên ID bệnh án (bằng 0 là thêm mới, > 0 là cập nhật)
        super(owner, (record == null || record.getId() == 0) ? "Thêm hồ sơ bệnh án" : "Cập nhật hồ sơ bệnh án", ModalityType.APPLICATION_MODAL);
        this.controller = controller;

        // 🌟 SỬA LOGIC: Kiểm tra ID > 0 mới là EditMode, giúp chế độ Add vẫn nhận được tên bệnh nhân & lộ trình truyền vào
        this.isEditMode = (record != null && record.getId() > 0);
        this.record = (record != null) ? record : new MedicalRecordModel(0, "", "", "", 0, "", "");

        initUI();
        loadAndPinData(); // Đổ dữ liệu và ghim lộ trình/bệnh nhân lên giao diện

        // Tăng chiều cao Dialog lên 580 để vừa vặn cho khu vực danh sách giai đoạn điều trị
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
        
        // Họ tên bệnh nhân (Khóa editable luôn vì đã chọn từ trước)
        txtPatientName = new JTextField();
        txtPatientName.setEditable(false);
        txtPatientName.setBackground(new Color(240, 242, 245));
        addField(formPanel, gbc, row++, "Họ tên bệnh nhân:", txtPatientName);

        // Bệnh điều trị
        txtDisease = new JTextField();
        addField(formPanel, gbc, row++, "Bệnh điều trị:", txtDisease);

        // Ngày bắt đầu
        txtStartDate = new JTextField(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
        addField(formPanel, gbc, row++, "Ngày bắt đầu (dd/mm/yyyy):", txtStartDate);

        // 🌟 THAY ĐỔI: Biến thành JTextField chỉnh màu xám mờ để báo ghim cứng lộ trình
        txtRouteName = new JTextField();
        txtRouteName.setEditable(false);
        txtRouteName.setBackground(new Color(240, 242, 245)); 
        txtRouteName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtRouteName.setForeground(new Color(0, 51, 102));
        addField(formPanel, gbc, row++, "Lộ trình ghim sẵn:", txtRouteName);

        // ----------------------------------------------------------------------
        // THIẾT KẾ KHU VỰC HIỂN THỊ CÁC BƯỚC ĐIỀU TRỊ ĐỘNG
        // ----------------------------------------------------------------------
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2; 
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;

        stagesPanel = new JPanel();
        stagesPanel.setLayout(new BoxLayout(stagesPanel, BoxLayout.Y_AXIS));
        stagesPanel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                " Các bước điều trị chưa hoàn thành ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(0, 51, 102));
        stagesPanel.setBorder(titledBorder);

        JScrollPane scrollStages = new JScrollPane(stagesPanel);
        scrollStages.setPreferredSize(new Dimension(440, 150));
        scrollStages.setMinimumSize(new Dimension(440, 150)); 
        scrollStages.setBorder(null);
        formPanel.add(scrollStages, gbc);

        root.add(formPanel, BorderLayout.CENTER);

        // Khởi tạo các nút bấm ở thanh điều khiển phía dưới thanh Dialog
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setOpaque(false);

        btnNextStage = new JButton("Chuyển giai đoạn");
        btnNextStage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNextStage.setBackground(new Color(230, 126, 34)); 
        btnNextStage.setForeground(Color.WHITE);
        btnNextStage.setFocusPainted(false);
        btnNextStage.addActionListener(e -> advanceCurrentStage());
        buttonPanel.add(btnNextStage);

        JButton btnSave = new JButton("Lưu lại");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setBackground(new Color(46, 204, 113)); 
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> saveRecord());
        buttonPanel.add(btnSave);

        root.add(buttonPanel, BorderLayout.SOUTH);
    }

    // 🌟 HÀM MỚI: Đổ dữ liệu và ghim chặt Lộ Trình/Bệnh Nhân
    private void loadAndPinData() {
        // Đổ tên bệnh nhân và ID lộ trình lấy trực tiếp từ Model sếp truyền vào
        txtPatientName.setText(record.getPatientName());
        this.selectedRouteId = record.getTreatmentRouteId();

        if (isEditMode) {
            txtDisease.setText(record.getDisease());
            txtStartDate.setText(record.getStartDate());
        }

        // 🌟 Tự động tìm kiếm tên Lộ trình tương ứng từ list cấu hình hệ thống bằng Stream API để hiển thị lên JTextField
        List<RouteItem> routes = controller.getRouteList();
        String routeName = routes.stream()
                .filter(r -> r.getId() == this.selectedRouteId)
                .map(RouteItem::toString)
                .findFirst()
                .orElse("Chưa chọn lộ trình phù hợp");
        txtRouteName.setText(routeName);

        // Tải danh sách các bước tiến độ dựa trên lộ trình ghim sẵn
        if (this.selectedRouteId > 0) {
            refreshStagesList(this.selectedRouteId);
        }
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        comp.setPreferredSize(new Dimension(250, 30));
        panel.add(comp, gbc);
    }

    private void refreshStagesList(int routeId) {
        stagesPanel.removeAll(); 

        activeStages = controller.getActiveStagesByRoute(routeId);

        if (activeStages == null || activeStages.isEmpty()) {
            JLabel lblDone = new JLabel("Toàn bộ lộ trình điều trị này đã hoàn thành!");
            lblDone.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblDone.setForeground(new Color(39, 174, 96));
            lblDone.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            stagesPanel.add(lblDone);

            btnNextStage.setEnabled(false); 
        } else {
            btnNextStage.setEnabled(true);

            for (TreatmentStageModel stage : activeStages) {
                String text = String.format("• %s   [%s]", stage.getStageName(), stage.getStatus());
                if (stage.getNote() != null && !stage.getNote().isEmpty()) {
                    text += " - Ghi chú: " + stage.getNote();
                }

                JLabel lblStage = new JLabel(text);
                lblStage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lblStage.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

                if ("Đang thực hiện".equals(stage.getStatus())) {
                    lblStage.setForeground(new Color(41, 128, 185));
                    lblStage.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    lblStage.setForeground(Color.GRAY); 
                }
                stagesPanel.add(lblStage);
            }
        }

        stagesPanel.revalidate();
        stagesPanel.repaint();
    }

    private void advanceCurrentStage() {
        if (activeStages == null || activeStages.isEmpty() || this.selectedRouteId <= 0)
            return;

        TreatmentStageModel currentStage = activeStages.get(0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận hoàn thành giai đoạn [" + currentStage.getStageName() + "] và chuyển sang bước tiếp theo?",
                "Xác nhận chuyển đổi tiến trình",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean isAdvanced = controller.advanceStage(currentStage.getId(), this.selectedRouteId);
            if (isAdvanced) {
                JOptionPane.showMessageDialog(this, "Hệ thống đã tự động nhảy sang giai đoạn tiếp theo!");
                refreshStagesList(this.selectedRouteId);
                this.saved = true; 
                MedicalRecordView.refreshData(); 
            } else {
                JOptionPane.showMessageDialog(this, "Chuyển giai đoạn thất bại! Sếp vui lòng kiểm tra lại DB.",
                        "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveRecord() {
        String patientName = txtPatientName.getText().trim();
        String disease = txtDisease.getText().trim();
        String startDate = txtStartDate.getText().trim();

        // 🌟 SỬA ĐIỀU KIỆN CHECK: Kiểm tra biến int selectedRouteId thay vì Combobox cũ
        if (patientName.isEmpty() || disease.isEmpty() || this.selectedRouteId <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hoặc kiểm tra lộ trình ghim!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        record.setPatientName(patientName);
        record.setDisease(disease);
        record.setStartDate(startDate);
        record.setTreatmentRouteId(this.selectedRouteId);

        boolean success;
        if (isEditMode) {
            success = controller.updateRecord(record, this.selectedRouteId);
        } else {
            success = controller.addRecord(record, this.selectedRouteId);
        }

        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this, "Đã lưu thông tin hồ sơ bệnh án thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu thất bại! Xin vui lòng kiểm tra lại kết nối Database.",
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public static class RouteItem {
        private final int id;
        private final String name;

        public RouteItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}