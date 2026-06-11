package ui.admin.form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

import controller.admin.DoctorController;
import model.admin.DoctorModel;
import ui.admin.DoctorView;

import java.awt.*;
import java.util.Date;

public class DoctorEditForm extends JDialog {

    private JTextField txtFullName;
    private JTextField txtPhone;
    private JPasswordField txtPassword;
    private JDateChooser dateChooserAppointment;
    private JTextField txtAddress;
    private JTextField txtAvatar;
    private JTextField txtEmail;
    private JTextField txtSpecialization;
    private JTextField txtExperience;

    private JButton btnSave;
    private JButton btnCancel;

    private DoctorController doctorController;
    private DoctorView doctorView;
    private int doctorId;

    public DoctorEditForm(JFrame parent, DoctorView doctorView, int doctorId) {
        super(parent, "SỬA BÁC SĨ", true);
        this.doctorView = doctorView;
        this.doctorId = doctorId;
        doctorController = new DoctorController();

        setSize(500, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(220, 235, 250));

        JLabel lblTitle = new JLabel("SỬA BÁC SĨ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 15, 15));
        formPanel.setBackground(new Color(220, 235, 250));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        txtFullName = new JTextField();
        txtPhone = new JTextField();
        txtPassword = new JPasswordField();
        dateChooserAppointment = new JDateChooser();
        dateChooserAppointment.setDateFormatString("dd/MM/yyyy");
        txtAddress = new JTextField();
        txtAvatar = new JTextField();
        txtEmail = new JTextField();
        txtSpecialization = new JTextField();
        txtExperience = new JTextField();

        formPanel.add(createLabel("Họ và tên"));
        formPanel.add(txtFullName);
        formPanel.add(createLabel("Số điện thoại"));
        formPanel.add(txtPhone);
        formPanel.add(createLabel("Mật khẩu"));
        formPanel.add(txtPassword);
        formPanel.add(createLabel("Ngày sinh"));
        formPanel.add(dateChooserAppointment);
        formPanel.add(createLabel("Địa chỉ"));
        formPanel.add(txtAddress);
        formPanel.add(createLabel("Avatar"));
        formPanel.add(txtAvatar);
        formPanel.add(createLabel("Email"));
        formPanel.add(txtEmail);
        formPanel.add(createLabel("Chuyên ngành"));
        formPanel.add(txtSpecialization);
        formPanel.add(createLabel("Năm kinh nghiệm"));
        formPanel.add(txtExperience);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(220, 235, 250));
        btnSave = createButton("Lưu");
        btnCancel = createButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> updateDoctor());
        btnCancel.addActionListener(e -> dispose());

        loadDoctorData();
    }

    private void loadDoctorData() {
        DoctorModel doctor = doctorController.getDoctorById(doctorId);
        if (doctor != null) {
            txtFullName.setText(doctor.getFullName());
            txtPhone.setText(doctor.getPhone());
            txtPassword.setText(doctor.getPassword());
            if (doctor.getBirthDate() != null) {
                dateChooserAppointment.setDate(doctor.getBirthDate());
            }
            txtAddress.setText(doctor.getAddress());
            txtAvatar.setText(doctor.getAvatar());
            txtEmail.setText(doctor.getEmail());
            txtSpecialization.setText(doctor.getSpecialization());
            txtExperience.setText(String.valueOf(doctor.getExperience()));
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy bác sĩ");
            dispose();
        }
    }

    private void updateDoctor() {
        try {
            String fullName = txtFullName.getText().trim();
            String phone = txtPhone.getText().trim();
            String password = String.valueOf(txtPassword.getPassword());
            Date birthDate = dateChooserAppointment.getDate();
            String address = txtAddress.getText().trim();
            String avatar = txtAvatar.getText().trim();
            String email = txtEmail.getText().trim();
            String specialization = txtSpecialization.getText().trim();
            String experienceStr = txtExperience.getText().trim();

            if (fullName.isEmpty() || phone.isEmpty() || password.isEmpty() || birthDate == null || email.isEmpty() || specialization.isEmpty() || experienceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            int experience = 0;
            try {
                experience = Integer.parseInt(experienceStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Năm kinh nghiệm phải là số");
                return;
            }

            DoctorModel doctor = new DoctorModel();
            doctor.setId(doctorId);
            doctor.setFullName(fullName);
            doctor.setPhone(phone);
            doctor.setPassword(password);
            doctor.setBirthDate(birthDate);
            doctor.setAddress(address);
            doctor.setAvatar(avatar);
            doctor.setEmail(email);
            doctor.setSpecialization(specialization);
            doctor.setExperience(experience);

            boolean result = doctorController.updateDoctor(doctor);
            if (result) {
                JOptionPane.showMessageDialog(this, "Cập nhật bác sĩ thành công");
                if (doctorView != null) {
                    doctorView.loadAllDoctor();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật bác sĩ thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(0, 51, 102));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
