package ui.admin.form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.admin.UserController;
import model.admin.UserModel;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class UserEditForm extends JDialog {

    private JTextField txtFullName;
    private JTextField txtPhone;
    private JPasswordField txtPassword;
    // ĐÃ ĐỔI TỪ JTextField SANG JDateChooser
    private JDateChooser dateChooserAppointment;
    private JTextField txtAddress;
    private JTextField txtAvatar;
    private JTextField txtEmail;
    private JButton btnSave;
    private JButton btnCancel;
    private UserController userController;
    private int userId;

    public UserEditForm(JFrame parent, int userId) {
        super(parent, "SỬA NGƯỜI DÙNG", true);
        this.userId = userId;
        userController = new UserController();

        setSize(500, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(220, 235, 250));

        JLabel lblTitle = new JLabel("SỬA NGƯỜI DÙNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 15, 15));
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

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(220, 235, 250));
        btnSave = createButton("Lưu");
        btnCancel = createButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> updateUser());
        btnCancel.addActionListener(e -> dispose());

        loadUserData();
    }

    private void loadUserData() {
        UserModel user = userController.getUserById(userId);
        if (user != null) {
            txtFullName.setText(user.getFullName());
            txtPhone.setText(user.getPhone());
            txtPassword.setText(user.getPassword());
            if (user.getBirthDate() != null) {
                dateChooserAppointment.setDate(user.getBirthDate());
            }
            txtAddress.setText(user.getAddress());
            txtAvatar.setText(user.getAvatar());
            txtEmail.setText(user.getEmail());
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng");
            dispose();
        }
    }

    private void updateUser() {
        try {
            String fullName = txtFullName.getText().trim();
            String phone = txtPhone.getText().trim();
            String password = String.valueOf(txtPassword.getPassword());
            Date birthDate = dateChooserAppointment.getDate();
            String address = txtAddress.getText().trim();
            String avatar = txtAvatar.getText().trim();
            String email = txtEmail.getText().trim();

            if (fullName.isEmpty() || phone.isEmpty() || password.isEmpty() || birthDate == null || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            UserModel user = new UserModel();
            user.setId(userId);
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setPassword(password);
            user.setBirthDate(birthDate);
            user.setAddress(address);
            user.setAvatar(avatar);
            user.setEmail(email);

            boolean result = userController.updateUser(user);
            if (result) {
                JOptionPane.showMessageDialog(this, "Cập nhật người dùng thành công");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật người dùng thất bại");
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
