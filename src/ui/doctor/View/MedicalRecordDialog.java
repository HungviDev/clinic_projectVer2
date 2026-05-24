
package ui.doctor.View;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.doctor.Controller.MedicalRecordController;
import ui.doctor.Model.MedicalRecordModel;

public class MedicalRecordDialog extends JDialog {

    private final MedicalRecordController controller;
    private final MedicalRecordModel record;

    private JTextField txtPatientName;

    private JTextField txtDisease;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JTextField txtDuration;
    private JComboBox<String> cboStage;

    private boolean saved = false;
    // Format hỗ trợ việc kiểm tra tính hợp lệ và tính toán số ngày
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");

    public MedicalRecordDialog(Window owner, MedicalRecordController controller, MedicalRecordModel record) {
        super(owner, record == null ? "Thêm hồ sơ bệnh án" : "Cập nhật hồ sơ bệnh án", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        this.record = record;
        displayFormat.setLenient(false); // Ngăn chặn ngày sai (VD: 32/01)

        initUI();
        loadData();

        setSize(650, 540);
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 248, 252));
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        JLabel lblTitle = new JLabel(record == null ? "THÊM HỒ SƠ BỆNH ÁN" : "CẬP NHẬT HỒ SƠ BỆNH ÁN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        root.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        txtPatientName = new JTextField();
        addField(formPanel, gbc, row++, "Họ tên bệnh nhân:", txtPatientName);

        txtDisease = new JTextField();
        addField(formPanel, gbc, row++, "Bệnh điều trị:", txtDisease);

        // Ngày mặc định khi thêm mới là ngày hôm nay
        String today = displayFormat.format(new java.util.Date());
        txtStartDate = new JTextField(today);
        addField(formPanel, gbc, row++, "Ngày bắt đầu (dd/mm/yyyy):", txtStartDate);

        txtEndDate = new JTextField(today);
        addField(formPanel, gbc, row++, "Ngày kết thúc (dd/mm/yyyy):", txtEndDate);

        txtDuration = new JTextField("0");
        txtDuration.setEditable(false);
        txtDuration.setBackground(new Color(240, 242, 245));
        addField(formPanel, gbc, row++, "Số ngày điều trị tự động:", txtDuration);

        cboStage = new JComboBox<>();
        addField(formPanel, gbc, row++, "Lộ trình điều trị:", cboStage);

        // Sự kiện tự động tính toán số ngày & load Combobox khi trỏ chuột ra ngoài
        java.awt.event.FocusAdapter updateListener = new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                calculateDuration();
                refreshStageCombo();
            }
        };
        txtStartDate.addFocusListener(updateListener);
        txtEndDate.addFocusListener(updateListener);
        txtDisease.addFocusListener(updateListener);

        root.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(245, 248, 252));
        JButton btnSave = createButton("Lưu Dữ Liệu", new Color(46, 204, 113));
        JButton btnCancel = createButton("Hủy bỏ", new Color(149, 165, 166));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        root.add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveRecord());
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        component.setPreferredSize(new Dimension(300, 36));

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(component, gbc);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        return btn;
    }

    private void calculateDuration() {
        try {
            java.util.Date start = displayFormat.parse(txtStartDate.getText().trim());
            java.util.Date end = displayFormat.parse(txtEndDate.getText().trim());
            long diff = end.getTime() - start.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            txtDuration.setText(days >= 0 ? String.valueOf(days) : "0");
        } catch (ParseException e) {
            txtDuration.setText("0");
        }
    }

    private void loadData() {
        if (record == null) return;

        txtPatientName.setText(record.getPatientName());
        txtDisease.setText(record.getDisease());
        
        // ĐÃ SỬA: Vì Model trả về String nên không dùng displayFormat.format() nữa
        txtStartDate.setText(record.getStartDate());
        txtEndDate.setText(record.getEndDate());
        
        txtDuration.setText(String.valueOf(record.getTreatmentDurationDays()));
        
        refreshStageCombo();
        cboStage.setSelectedItem(record.getCurrentStage());
    }

    private void refreshStageCombo() {

    // Xóa dữ liệu cũ
    cboStage.removeAllItems();

    java.util.List<String> stages = controller.getTitleRoadMap();

    // Add dữ liệu vào combobox
    for (String stage : stages) {
        cboStage.addItem(stage);
    }

    // if (stages.isEmpty()) {
    //     cboStage.addItem("Giai đoạn khởi phát");
    //     cboStage.addItem("Giai đoạn toàn phát");
    //     cboStage.addItem("Giai đoạn lui bệnh");
    // }
}

    private void saveRecord() {
        String txtStart = txtStartDate.getText().trim();
        String txtEnd = txtEndDate.getText().trim();
        String durationStr = txtDuration.getText().trim();
        String name = txtPatientName.getText().trim();
        String disease = txtDisease.getText().trim();

        // Kiểm tra rỗng đầu vào cơ bản
        if (name.isEmpty() || disease.isEmpty() || txtStart.isEmpty() || txtEnd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
            return;
        }

        try {
            // Kiểm tra định dạng ngày hợp lệ trước khi đóng gói gửi đi
            displayFormat.parse(txtStart);
            displayFormat.parse(txtEnd);

            int duration = Integer.parseInt(durationStr);
            String currentStage = (String) cboStage.getSelectedItem();

            // Khởi tạo Model đồng nhất với Constructor mới của bạn
            MedicalRecordModel data = new MedicalRecordModel(
                record == null ? 0 : record.getId(),
                name,
                disease,
                txtStart,        // Chuỗi String chuẩn dd/MM/yyyy
                txtEnd,          // Chuỗi String chuẩn dd/MM/yyyy
                duration,
                currentStage != null ? currentStage : "Giai đoạn khởi phát",
                "/images/default.png"
            );

            // Nếu tạo mới thì nạp ID tự động tăng từ Database
            if (record == null) {
                data.setId(controller.getNextId());
            }

            // Thực thi lưu xuống Database
            boolean isSuccess = (record == null) ? controller.addRecord(data) : controller.updateRecord(data);

            if (isSuccess) {
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi thao tác ghi dữ liệu trên Cơ sở dữ liệu thất bại!");
            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Sai định dạng ngày tháng! Vui lòng nhập theo dạng: dd/mm/yyyy.");
        }
    }

    public boolean isSaved() { return saved; }
}