package ui.admin.form;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.admin.UserController;
import model.admin.UserModel;
import ui.admin.UserView;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserAddForm extends JDialog {

    private JTextField txtFullName;

    private JTextField txtPhone;

    private JPasswordField txtPassword;

    private JTextField txtBirthDate;

    private JTextField txtAddress;

    private JTextField txtAvatar;

    private JTextField txtEmail;

    private JButton btnSave;

    private JButton btnCancel;

    private UserController userController;
    private UserView userView = new UserView();

    public UserAddForm(JFrame parent) {

        super(parent, "THÊM NGƯỜI DÙNG", true);

        userController = new UserController();

        setSize(500, 650);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        getContentPane().setBackground(
                new Color(220, 235, 250)
        );

        JLabel lblTitle = new JLabel(
                "THÊM NGƯỜI DÙNG",
                SwingConstants.CENTER
        );

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        lblTitle.setForeground(
                new Color(0, 51, 102)
        );

        lblTitle.setBorder(
                new EmptyBorder(20, 10, 20, 10)
        );

        add(lblTitle, BorderLayout.NORTH);

        // =====================================
        // FORM PANEL
        // =====================================
        JPanel formPanel = new JPanel(
                new GridLayout(7, 2, 15, 15)
        );

        formPanel.setBackground(
                new Color(220, 235, 250)
        );

        formPanel.setBorder(
                new EmptyBorder(20, 30, 20, 30)
        );

        txtFullName = new JTextField();

        txtPhone = new JTextField();

        txtPassword = new JPasswordField();

        txtBirthDate = new JTextField();

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
        formPanel.add(txtBirthDate);

        formPanel.add(createLabel("Địa chỉ"));
        formPanel.add(txtAddress);

        formPanel.add(createLabel("Avatar"));
        formPanel.add(txtAvatar);

        formPanel.add(createLabel("Email"));
        formPanel.add(txtEmail);

        add(formPanel, BorderLayout.CENTER);

        // =====================================
        // BUTTON PANEL
        // =====================================
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 10)
        );

        buttonPanel.setBackground(
                new Color(220, 235, 250)
        );

        btnSave = createButton("Lưu");

        btnCancel = createButton("Hủy");

        buttonPanel.add(btnSave);

        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        // =====================================
        // EVENT SAVE
        // =====================================
        btnSave.addActionListener(e -> insertUser());

        // =====================================
        // EVENT CANCEL
        // =====================================
        btnCancel.addActionListener(e -> dispose());
    }

    // =====================================
    // INSERT USER
    // =====================================
    // =====================================
// INSERT USER
// =====================================
private void insertUser() {

    try {

        String fullName = txtFullName.getText().trim();

        String phone = txtPhone.getText().trim();

        String password = String.valueOf(
                txtPassword.getPassword()
        );

        String birth = txtBirthDate.getText().trim();

        String address = txtAddress.getText().trim();

        String avatar = txtAvatar.getText().trim();

        String email = txtEmail.getText().trim();

        // =====================================
        // LOG INPUT
        // =====================================

        // =====================================
        // VALIDATE
        // =====================================
        if (
                fullName.isEmpty() ||
                phone.isEmpty() ||
                password.isEmpty() ||
                birth.isEmpty() ||
                email.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin"
            );

            return;
        }

        // =====================================
        // CONVERT DATE
        // =====================================
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy");

        sdf.setLenient(false);

        Date birthDate = sdf.parse(birth);

        System.out.println("BirthDate Parsed: " + birthDate);

        // =====================================
        // SET MODEL
        // =====================================
        UserModel user = new UserModel();

        user.setFullName(fullName);

        user.setPhone(phone);

        user.setPassword(password);

        user.setBirthDate(birthDate);

        user.setAddress(address);

        user.setAvatar(avatar);

        user.setEmail(email);


        // =====================================
        // INSERT DATABASE
        // =====================================
        boolean result =
                userController.insertUser(user);

        System.out.println("Insert Result: " + result);

        // =====================================
        // RESULT
        // =====================================
        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Thêm người dùng thành công"
            );
            userView.loadAllUser();
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Thêm người dùng thất bại"
            );
        }

    } catch (Exception e) {

        // =====================================
        // LOG ERROR
        // =====================================
        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Lỗi: " + e.getMessage()
        );
    }
}
    // =====================================
    // LABEL UI
    // =====================================
    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        label.setForeground(
                new Color(0, 51, 102)
        );

        return label;
    }

    // =====================================
    // BUTTON UI
    // =====================================
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setPreferredSize(
                new Dimension(120, 40)
        );

        button.setBackground(Color.WHITE);

        button.setForeground(Color.BLACK);

        button.setFocusPainted(false);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }
}