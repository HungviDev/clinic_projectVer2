package ui.admin;

import controller.admin.MedicalRecordController;
import controller.admin.RoadMapController;
import controller.admin.StepRoadMapController;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap; // Khai báo thêm Controller này
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.admin.RoadmapModel;
import model.admin.StepRoadMapModel;

public class ItinereryView extends JPanel {

    /*
     * =========================================
     * CLASS HỖ TRỢ (GIẤU ID BỆNH NHÂN & BỆNH ÁN)
     * =========================================
     */
    // Class giấu ID Bệnh nhân
    public static class PatientComboItem {
        private int userId;
        private String fullname;

        public PatientComboItem(int userId, String fullname) {
            this.userId = userId;
            this.fullname = fullname;
        }

        public int getUserId() { return userId; }
        @Override
        public String toString() { return fullname; }
    }

    // Class giấu ID Bệnh án (Mỏ neo)
    public static class ServiceComboItem {
        private int medicalRecordId;
        private String serviceName;

        public ServiceComboItem(int medicalRecordId, String serviceName) {
            this.medicalRecordId = medicalRecordId;
            this.serviceName = serviceName;
        }

        public int getMedicalRecordId() { return medicalRecordId; }
        @Override
        public String toString() { return serviceName; }
    }

    /*
     * =========================================
     * COLOR
     * =========================================
     */
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(255, 167, 38);

    /*
     * =========================================
     * TABLE
     * =========================================
     */
    private JTable tableItinerary;
    private JTable tableStep;
    private DefaultTableModel itineraryModel;
    private DefaultTableModel stepModel;

    /*
     * =========================================
     * DATA
     * =========================================
     */
    private final HashMap<Integer, ArrayList<Object[]>> itinerarySteps = new HashMap<>();
    
    // Khai báo các Controller
    RoadMapController roadmapController = new RoadMapController();
    StepRoadMapController stepController = new StepRoadMapController();
    MedicalRecordController mrController = new MedicalRecordController(); // Đã thêm Controller dữ liệu thật

    public ItinereryView() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        initUI();
        loadData();
        initAction();
    }

    private void initUI() {
        // =========================================
        // HEADER
        // =========================================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Quản lý lộ trình điều trị");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnAddItinerary = createButton("Thêm lộ trình", SUCCESS_COLOR);
        JButton btnAddStep = createButton("Thêm bước", PRIMARY_COLOR);
        JButton btnDelete = createButton("Xóa", DANGER_COLOR);

        buttonPanel.add(btnAddItinerary);
        buttonPanel.add(btnAddStep);
        buttonPanel.add(btnDelete);

        header.add(title, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        // =========================================
        // MAIN PANEL
        // =========================================
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(0, 25, 25, 25));

        // =========================================
        // LEFT PANEL (Lộ trình)
        // =========================================
        JPanel leftPanel = createCardPanel("Danh sách lộ trình");
        String[] itineraryColumns = {"ID", "Tên lộ trình", "Mô tả", "Ngày tạo"};
        itineraryModel = new DefaultTableModel(itineraryColumns, 0);
        tableItinerary = new JTable(itineraryModel);
        styleTable(tableItinerary);
        tableItinerary.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableItinerary.setRowHeight(35);

        tableItinerary.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableItinerary.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableItinerary.getColumnModel().getColumn(2).setPreferredWidth(450);
        tableItinerary.getColumnModel().getColumn(3).setPreferredWidth(180);

        JScrollPane scrollLeft = new JScrollPane(tableItinerary);
        scrollLeft.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollLeft.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollLeft.getViewport().setBackground(Color.WHITE);
        scrollLeft.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        leftPanel.add(scrollLeft, BorderLayout.CENTER);

        // =========================================
        // RIGHT PANEL (Các bước)
        // =========================================
        JPanel rightPanel = createCardPanel("Các bước điều trị");
        String[] stepColumns = {"STT", "Tên giai đoạn", "Thứ tự", "Khoảng cách", "Ngày hẹn", "Trạng thái", "Chi phí", "Ghi chú"};
        stepModel = new DefaultTableModel(stepColumns, 0);
        tableStep = new JTable(stepModel);
        styleTable(tableStep);
        tableStep.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableStep.setRowHeight(35);

        tableStep.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableStep.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableStep.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableStep.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableStep.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableStep.getColumnModel().getColumn(5).setPreferredWidth(130);
        tableStep.getColumnModel().getColumn(6).setPreferredWidth(120);
        tableStep.getColumnModel().getColumn(7).setPreferredWidth(300);

        JScrollPane scrollRight = new JScrollPane(tableStep);
        scrollRight.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollRight.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollRight.getViewport().setBackground(Color.WHITE);
        scrollRight.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        rightPanel.add(scrollRight, BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(header, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        /*
         * =========================================
         * ACTION: THÊM LỘ TRÌNH (DÙNG DỮ LIỆU THẬT)
         * =========================================
         */
        btnAddItinerary.addActionListener(e -> {
            Font font = new Font("Segoe UI", Font.PLAIN, 15);
            Dimension fieldSize = new Dimension(280, 38);

            // 1. Ô CHỌN BỆNH NHÂN
            JComboBox<PatientComboItem> cboPatient = new JComboBox<>();
            cboPatient.setFont(font);
            cboPatient.setPreferredSize(fieldSize);
            
            // 2. Ô CHỌN DỊCH VỤ
            JComboBox<ServiceComboItem> cboService = new JComboBox<>();
            cboService.setFont(font);
            cboService.setPreferredSize(fieldSize);

            // --- NẠP DỮ LIỆU THẬT: Danh sách Bệnh nhân ---
            List<PatientComboItem> patients = mrController.getPatientsNeedingRoute();
            for (PatientComboItem p : patients) {
                cboPatient.addItem(p);
            }

            // --- SỰ KIỆN: Liên kết 2 ô để load dữ liệu thật ---
            cboPatient.addActionListener(event -> {
                PatientComboItem selectedPatient = (PatientComboItem) cboPatient.getSelectedItem();
                cboService.removeAllItems(); // Xóa sạch dịch vụ cũ

                if (selectedPatient != null) {
                    // Gọi DB lấy danh sách dịch vụ đang chữa dở của bệnh nhân này
                    List<ServiceComboItem> services = mrController.getUnroutedServicesByPatient(selectedPatient.getUserId());
                    for (ServiceComboItem s : services) {
                        cboService.addItem(s);
                    }
                }
            });

            // Kích hoạt thủ công 1 lần đầu tiên để Load dịch vụ
            if (cboPatient.getItemCount() > 0) {
                cboPatient.setSelectedIndex(0); 
            }

            // 3. Ô Tên lộ trình
            JTextField txtTitle = new JTextField();
            txtTitle.setFont(font);
            txtTitle.setPreferredSize(fieldSize);

            // 4. Ô Mô tả (TextArea to đùng)
            JTextArea txtDescription = new JTextArea(4, 20);
            txtDescription.setFont(font);
            txtDescription.setLineWrap(true);
            txtDescription.setWrapStyleWord(true);
            JScrollPane scrollDescription = new JScrollPane(txtDescription);
            scrollDescription.setPreferredSize(new Dimension(280, 100));

            // Dựng giao diện Popup
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblFormTitle = new JLabel("THÊM LỘ TRÌNH ĐIỀU TRỊ");
            lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblFormTitle.setForeground(new Color(33, 150, 243));

            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panel.add(lblFormTitle, gbc);
            gbc.gridwidth = 1;

            // Row 1: Chọn bệnh nhân
            gbc.gridy = 1; gbc.gridx = 0;
            JLabel lblPatient = new JLabel("Chọn bệnh nhân:"); lblPatient.setFont(font);
            panel.add(lblPatient, gbc);
            gbc.gridx = 1; panel.add(cboPatient, gbc);

            // Row 2: Chọn dịch vụ
            gbc.gridy = 2; gbc.gridx = 0;
            JLabel lblService = new JLabel("Chọn dịch vụ:"); lblService.setFont(font);
            panel.add(lblService, gbc);
            gbc.gridx = 1; panel.add(cboService, gbc);

            // Row 3: Tên lộ trình
            gbc.gridy = 3; gbc.gridx = 0;
            JLabel lblName = new JLabel("Tên lộ trình:"); lblName.setFont(font);
            panel.add(lblName, gbc);
            gbc.gridx = 1; panel.add(txtTitle, gbc);

            // Row 4: Mô tả
            gbc.gridy = 4; gbc.gridx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
            JLabel lblDesc = new JLabel("Mô tả:"); lblDesc.setFont(font);
            panel.add(lblDesc, gbc);
            gbc.gridx = 1; panel.add(scrollDescription, gbc);

            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);

            int result = JOptionPane.showConfirmDialog(null, panel, "Thêm lộ trình", 
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // =========================================
            // XỬ LÝ LƯU DỮ LIỆU
            // =========================================
            if (result == JOptionPane.OK_OPTION) {
                
                // 1. LẤY CÁI MỎ NEO (ID BỆNH ÁN) TỪ Ô DỊCH VỤ
                ServiceComboItem selectedService = (ServiceComboItem) cboService.getSelectedItem();
                if (selectedService == null) {
                    JOptionPane.showMessageDialog(null, "Khách hàng này chưa có dịch vụ nào để thêm lộ trình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int targetMedicalRecordId = selectedService.getMedicalRecordId();

                // 2. Gói dữ liệu
                RoadmapModel roadmap = new RoadmapModel();
                roadmap.setTitle(txtTitle.getText());
                roadmap.setDescription(txtDescription.getText());

                // 3. TRUYỀN ID BỆNH ÁN XUỐNG CONTROLLER ĐỂ CHẠY TRANSACTION
                boolean isSuccess = roadmapController.insertRoadmap(roadmap, targetMedicalRecordId);

                if (isSuccess) {
                    loadData();
                    JOptionPane.showMessageDialog(null, "Thêm lộ trình thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "Thêm lộ trình thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /*
         * =========================================
         * ACTION: ADD STEP
         * =========================================
         */
        btnAddStep.addActionListener(e -> {
            int row = tableItinerary.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Hãy chọn lộ trình trước khi thêm bước điều trị!");
                return;
            }
            int itineraryId = Integer.parseInt(itineraryModel.getValueAt(row, 0).toString());

            JTextField txtStageName = new JTextField();
            JTextField txtOrder = new JTextField();
            JTextField txtDuration = new JTextField();
            JTextField txtCost = new JTextField();
            JTextArea txtNote = new JTextArea(4, 20);

            Font font = new Font("Segoe UI", Font.PLAIN, 15);
            Font titleFont = new Font("Segoe UI", Font.BOLD, 15);

            txtStageName.setFont(font); txtOrder.setFont(font);
            txtDuration.setFont(font); txtCost.setFont(font); txtNote.setFont(font);
            txtNote.setLineWrap(true); txtNote.setWrapStyleWord(true);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(245, 247, 250));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblTitle = new JLabel("THÊM GIAI ĐOẠN ĐIỀU TRỊ");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            panel.add(lblTitle, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(Color.WHITE);
            formPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(12, 12, 12, 12);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int y = 0;
            // Tên giai đoạn
            gbc.gridx = 0; gbc.gridy = y;
            JLabel lblStage = new JLabel("Tên giai đoạn"); lblStage.setFont(titleFont);
            formPanel.add(lblStage, gbc);
            gbc.gridx = 1; txtStageName.setPreferredSize(new Dimension(260, 38)); formPanel.add(txtStageName, gbc);

            // Thứ tự
            y++; gbc.gridx = 0; gbc.gridy = y;
            JLabel lblOrder = new JLabel("Thứ tự"); lblOrder.setFont(titleFont);
            formPanel.add(lblOrder, gbc);
            gbc.gridx = 1; txtOrder.setPreferredSize(new Dimension(260, 38)); formPanel.add(txtOrder, gbc);

            // Thời gian
            y++; gbc.gridx = 0; gbc.gridy = y;
            JLabel lblDuration = new JLabel("Khoảng cách (Ngày)"); lblDuration.setFont(titleFont);
            formPanel.add(lblDuration, gbc);
            gbc.gridx = 1; txtDuration.setPreferredSize(new Dimension(260, 38)); formPanel.add(txtDuration, gbc);

            // Chi phí
            y++; gbc.gridx = 0; gbc.gridy = y;
            JLabel lblCost = new JLabel("Chi phí"); lblCost.setFont(titleFont);
            formPanel.add(lblCost, gbc);
            gbc.gridx = 1; txtCost.setPreferredSize(new Dimension(260, 38)); formPanel.add(txtCost, gbc);

            // Ghi chú
            y++; gbc.gridx = 0; gbc.gridy = y;
            JLabel lblNote = new JLabel("Ghi chú"); lblNote.setFont(titleFont);
            formPanel.add(lblNote, gbc);
            gbc.gridx = 1; JScrollPane scrollNote = new JScrollPane(txtNote);
            scrollNote.setPreferredSize(new Dimension(260, 100)); formPanel.add(scrollNote, gbc);

            panel.add(formPanel, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(null, panel, "Thêm bước điều trị", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    StepRoadMapModel step = new StepRoadMapModel();
                    step.setTreatmentRouteId(itineraryId);
                    step.setStageName(txtStageName.getText());
                    step.setSequenceOrder(Integer.parseInt(txtOrder.getText().isEmpty() ? "0" : txtOrder.getText()));
                    step.setDelay(Integer.parseInt(txtDuration.getText().isEmpty() ? "0" : txtDuration.getText()));
                    step.setCost(Double.parseDouble(txtCost.getText().isEmpty() ? "0" : txtCost.getText()));
                    step.setNote(txtNote.getText());
                    step.setStatus("Chưa thực hiện");
                    
                    boolean inserted = roadmapController.insertStageAndCreatePayment(step);
                  
                    if (inserted) {
                        JOptionPane.showMessageDialog(null, "Thêm giai đoạn thành công");
                        loadSteps(itineraryId);
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng số cho Thứ tự, Khoảng cách và Chi phí!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /*
         * =========================================
         * ACTION: DELETE
         * =========================================
         */
        btnDelete.addActionListener(e -> {
            int row = tableItinerary.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn lộ trình cần xóa");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(itineraryModel.getValueAt(row, 0).toString());
                boolean check = roadmapController.deleteRoadmap(id);
                if (check) {
                    itineraryModel.removeRow(row);
                    itinerarySteps.remove(id);
                    stepModel.setRowCount(0);
                    JOptionPane.showMessageDialog(null, "Xóa thành công");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa thất bại");
                }
            }
        });
    }

    private void initAction() {
        tableItinerary.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableItinerary.getSelectedRow();
                if (row != -1) {
                    int itineraryId = Integer.parseInt(itineraryModel.getValueAt(row, 0).toString());
                    loadSteps(itineraryId);
                }
            }
        });
    }

    private void loadSteps(int itineraryId) {
        stepModel.setRowCount(0);
        List<StepRoadMapModel> steps = stepController.getStepsByRouteId(itineraryId);
        for (StepRoadMapModel step : steps) {
            stepModel.addRow(new Object[]{
                    step.getId(), step.getStageName(), step.getSequenceOrder(),
                    step.getDelay() + " ngày", step.getAppointmentDate(),
                    step.getStatus(), step.getCost(), step.getNote()
            });
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(lblTitle, BorderLayout.NORTH);
        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadData() {
        itineraryModel.setRowCount(0);
        itinerarySteps.clear();
        StepRoadMapController stepController = new StepRoadMapController();
        List<RoadmapModel> roadmapList = roadmapController.getAllRoadmap();

        for (RoadmapModel roadmap : roadmapList) {
            itineraryModel.addRow(new Object[]{
                    roadmap.getId(), roadmap.getTitle(), roadmap.getDescription(), roadmap.getDatecreate()
            });

            List<StepRoadMapModel> stepList = stepController.getStepsByRouteId(roadmap.getId());
            ArrayList<Object[]> stepData = new ArrayList<>();
            for (StepRoadMapModel step : stepList) {
                stepData.add(new Object[]{
                        step.getId(), step.getStageName(), step.getSequenceOrder(),
                        step.getDelay() + " ngày", step.getAppointmentDate(),
                        step.getStatus(), step.getCost(), step.getNote()
                });
            }
            itinerarySteps.put(roadmap.getId(), stepData);
        }
    }
}