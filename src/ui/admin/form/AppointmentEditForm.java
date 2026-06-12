package ui.admin.form;

import com.toedter.calendar.JDateChooser;

import controller.admin.AppointmentController;
import model.admin.AppointmentModel;
import ui.admin.AppointmentView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentEditForm extends JDialog {

    private final Color MAIN_BG = new Color(240, 248, 255);
    private final Color PANEL_BG = Color.WHITE;
    private final Color PRIMARY = new Color(0, 153, 255);
    private final Color PRIMARY_DARK = new Color(0, 102, 204);
    private final Color BORDER_COLOR = new Color(153, 204, 255);
    private final Color TEXT_COLOR = new Color(30, 40, 50);

    private JDateChooser dateChooserAppointment;
    private JComboBox<String> cbStatus;
    private JTextField txtDoctorName;
    private JTextField txtPatientName;

    private AppointmentController appointmentController;
    private AppointmentModel appointmentModel;
    private AppointmentView appointmentView;
    private int id;

    public AppointmentEditForm(JFrame parent, AppointmentView appointmentView, int id) {
        super(parent, "SỬA LỊCH HẸN", true);
        this.appointmentView = appointmentView;
        this.id = id;
        this.appointmentController = new AppointmentController();
        
        initUI();
        loadData();
    }

    private void initUI() {
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAIN_BG);

        JLabel lblTitle = new JLabel("CHI TIẾT & CẬP NHẬT LỊCH HẸN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(PRIMARY_DARK);
        lblTitle.setBorder(new EmptyBorder(15, 10, 15, 10));
        add(lblTitle, BorderLayout.NORTH);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(MAIN_BG);
        wrapperPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDoctorName = new JTextField();
        setupDisabledTextField(txtDoctorName);
        
        txtPatientName = new JTextField();
        setupDisabledTextField(txtPatientName);

        dateChooserAppointment = new JDateChooser();
        dateChooserAppointment.setDateFormatString("yyyy-MM-dd");
        dateChooserAppointment.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooserAppointment.setPreferredSize(new Dimension(200, 35));

        String[] statuses = {"Chờ duyệt", "Đã duyệt", "Đã hủy", "Từ chối"};
        cbStatus = new JComboBox<>(statuses);
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbStatus.setPreferredSize(new Dimension(200, 35));

        int gridy = 0;
        
        // Bác sĩ
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Bác sĩ:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(txtDoctorName, gbc);

        // Bệnh nhân
        gridy++;
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Bệnh nhân:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(txtPatientName, gbc);

        // Ngày hẹn
        gridy++;
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Ngày hẹn (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(dateChooserAppointment, gbc);

        // Trạng thái
        gridy++;
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.weightx = 0.4;
        formPanel.add(createLabel("Trạng thái:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        formPanel.add(cbStatus, gbc);

        wrapperPanel.add(formPanel, BorderLayout.CENTER);
        add(wrapperPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(MAIN_BG);

        JButton btnSave = new JButton("Cập nhật");
        styleButton(btnSave, PRIMARY);

        JButton btnCancel = new JButton("Đóng");
        styleButton(btnCancel, new Color(255, 77, 77));

        btnSave.addActionListener(e -> saveAppointment());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupDisabledTextField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setEditable(false);
        txt.setBackground(new Color(245, 245, 245));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 38));
        btn.setBorder(BorderFactory.createEmptyBorder());
    }

    private void loadData() {
        appointmentModel = appointmentController.getAppointmentById(id);
        if (!"Pending".equalsIgnoreCase(appointmentModel.getStatus())) {
            dateChooserAppointment.setEnabled(false);
        }
        else{
            dateChooserAppointment.setEnabled(true);
        }
        if (appointmentModel != null) {
            txtDoctorName.setText(appointmentModel.getDoctorName() != null ? appointmentModel.getDoctorName() : "");
            txtPatientName.setText(appointmentModel.getPatientName() != null ? appointmentModel.getPatientName() : "");
            
            if (appointmentModel.getAppointmentDate() != null) {
                dateChooserAppointment.setDate(appointmentModel.getAppointmentDate());
            }

            // Chuyển status Tiếng Anh từ DB thành Tiếng Việt để hiển thị lên ComboBox
            String dbStatus = appointmentModel.getStatus();
            String displayStatus = "Chờ duyệt";
            if ("Pending".equalsIgnoreCase(dbStatus)) displayStatus = "Chờ duyệt";
            else if ("Approved".equalsIgnoreCase(dbStatus)) displayStatus = "Đã duyệt";
            else if ("Cancel".equalsIgnoreCase(dbStatus)) displayStatus = "Đã hủy";
            else if ("Reject".equalsIgnoreCase(dbStatus)) displayStatus = "Từ chối";

            cbStatus.setSelectedItem(displayStatus);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void saveAppointment() {

        try {
            java.util.Date selectedDate = dateChooserAppointment.getDate();
            String displayStatus = (String) cbStatus.getSelectedItem();

            // Chuyển lại trạng thái Tiếng Việt về Tiếng Anh để lưu DB
            String dbStatus = "Pending";
            if ("Chờ duyệt".equals(displayStatus)) dbStatus = "Pending";
            else if ("Đã duyệt".equals(displayStatus)) dbStatus = "Approved";
            else if ("Đã hủy".equals(displayStatus)) dbStatus = "Cancel";
            else if ("Từ chối".equals(displayStatus)) dbStatus = "Reject";
            
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày hẹn hợp lệ!");
                return;
            }

            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

            appointmentModel.setAppointmentDate(sqlDate);
            appointmentModel.setStatus(dbStatus);

            boolean success = appointmentController.updateAppointment(appointmentModel);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch hẹn thành công!");
                appointmentView.loadAllAppointment();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đã có lỗi xảy ra. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
