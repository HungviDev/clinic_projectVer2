package ui.auth;

import controller.user.AuthController;
import controller.user.DashboardController;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.admin.UserController;
import controller.user.AuthController;
import controller.user.DashboardController;
import global.GlobalData;
import model.admin.UserModel;
import model.user.User;


public class LoginForm extends JFrame {

    private JTextField txtPhone;
    private JPasswordField txtPass;
    private boolean isPasswordVisible = false;
    private UserController userController = new UserController();

    // Định nghĩa các màu sắc chủ đạo
private final Color COLOR_PRIMARY = new Color(0, 123, 255);   // xanh dương chính
private final Color COLOR_BUTTON  = new Color(0, 102, 204);   // xanh đậm cho nút
    private final Color COLOR_TEXT = new Color(70, 70, 70);        // Xám đen cho chữ
    private final Color COLOR_LINK = new Color(230, 120, 30);      // Cam cho link đăng ký
    private final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public LoginForm() {
        setTitle("Đăng nhập - Nha Khoa Việt Anh");
        setSize(400, 650); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        setResizable(false); 
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_PRIMARY);
        headerPanel.setPreferredSize(new Dimension(400, 220));
        headerPanel.setLayout(new GridBagLayout());

        JLabel lblLogo = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/resources/logo.png");

    if (logoIcon.getIconWidth() > 0) {
        int maxWidth = 400;   
        int maxHeight = 220;  

        Image scaledImage = logoIcon.getImage().getScaledInstance(
                maxWidth,
                maxHeight,
                Image.SCALE_SMOOTH
        );

        lblLogo.setIcon(new ImageIcon(scaledImage));
    } else {
            lblLogo.setForeground(Color.WHITE);
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        }
        headerPanel.add(lblLogo);
        add(headerPanel, BorderLayout.NORTH);

        // ================= MAIN FORM PANEL =================
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int gridy = 0;

        // --- Tiêu đề "Đăng nhập" ---
        JLabel lblTitle = new JLabel("Đăng nhập", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(10, 0, 20, 0);
        formPanel.add(lblTitle, gbc);

        // --- Label Tài khoản ---
        JLabel lblPhoneTitle = new JLabel("Tài khoản");
        lblPhoneTitle.setFont(FONT_REGULAR);
        lblPhoneTitle.setForeground(COLOR_TEXT);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 2, 40); // Căn lề trái phải 40px
        formPanel.add(lblPhoneTitle, gbc);

        // --- Ô nhập Tài khoản ---
        JPanel phoneInputPanel = createInputPanel("src/resources/phone.png", "📞");
        txtPhone = new JTextField();
        setupTextField(txtPhone);
        phoneInputPanel.add(txtPhone, BorderLayout.CENTER);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 15, 40);
        formPanel.add(phoneInputPanel, gbc);

        // --- Label Mật khẩu ---
        JLabel lblPassTitle = new JLabel("Mật khẩu");
        lblPassTitle.setFont(FONT_REGULAR);
        lblPassTitle.setForeground(COLOR_TEXT);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 2, 40);
        formPanel.add(lblPassTitle, gbc);

        // --- Ô nhập Mật khẩu ---
        JPanel passInputPanel = createInputPanel("src/resources/lock.png", "🔒");
        txtPass = new JPasswordField();
        setupTextField(txtPass);
        passInputPanel.add(txtPass, BorderLayout.CENTER);

        // Nút con mắt
        JLabel lblEye = new JLabel();
        lblEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblEye.setBorder(new EmptyBorder(0, 5, 0, 5));
        ImageIcon eyeOpen = new ImageIcon("src/resources/eye.png");
        ImageIcon eyeHide = new ImageIcon("src/resources/eye_hide.png");
        
        lblEye.setIcon(eyeHide.getIconWidth() > 0 ? eyeHide : null);
        if (eyeHide.getIconWidth() <= 0) lblEye.setText("👁");
        
        lblEye.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    txtPass.setEchoChar((char) 0);
                    if (eyeOpen.getIconWidth() > 0) lblEye.setIcon(eyeOpen);
                } else {
                    txtPass.setEchoChar('•');
                    if (eyeHide.getIconWidth() > 0) lblEye.setIcon(eyeHide);
                }
            }
        });
        passInputPanel.add(lblEye, BorderLayout.EAST);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 25, 40);
        formPanel.add(passInputPanel, gbc);

        // --- Nút Đăng nhập ---
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(FONT_BOLD);
        btnLogin.setBackground(COLOR_BUTTON);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0)); // Nút to và cân đối hơn
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(10, 40, 25, 40);
        formPanel.add(btnLogin, gbc);

        // --- Dòng chữ Đăng ký ---
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBackground(Color.WHITE);
        
        JLabel lblQuestion = new JLabel("Bạn đăng ký tài khoản chưa?");
        lblQuestion.setFont(FONT_REGULAR);
        lblQuestion.setForeground(COLOR_TEXT);
        
        JLabel lblRegister = new JLabel("Đăng ký ngay");
        lblRegister.setFont(FONT_BOLD);
        lblRegister.setForeground(COLOR_LINK);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        registerPanel.add(lblQuestion);
        registerPanel.add(lblRegister);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 20, 40);
        formPanel.add(registerPanel, gbc);

        // Wrapper để đẩy formPanel lên phía trên, tránh bị dính sát xuống đáy
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(formPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);

        // ================= SỰ KIỆN (EVENTS) =================
        btnLogin.addActionListener(e -> login());
        txtPass.addActionListener(e -> login()); 
        
        // Mở form đăng ký theo yêu cầu của bạn
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterForm();
                dispose(); // Đóng form đăng nhập (nếu cần thiết)
            }
        });

        setVisible(true);
    }

// Thiết lập viền khung cho ô nhập liệu (bo góc nhẹ) và resize icon
    private JPanel createInputPanel(String iconPath, String fallbackText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 45)); // Cố định chiều cao các ô
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 15, 5, 10) 
        ));

        JLabel lblIcon = new JLabel();
        ImageIcon icon = new ImageIcon(iconPath);
        
        if (icon.getIconWidth() > 0) {
            // Thay đổi kích thước icon cho vừa vặn (ví dụ: 20x20 pixel)    
            int iconSize = 20; 
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(scaledImage));
        } else {
            lblIcon.setText(fallbackText);
            lblIcon.setForeground(Color.GRAY);
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        
        panel.add(lblIcon, BorderLayout.WEST);

        return panel;
    }

    // Tinh chỉnh JTextfield (bỏ viền mặc định, đồng bộ nền)
    private void setupTextField(JTextField textField) {
        textField.setBorder(null);
        textField.setFont(FONT_REGULAR);
        textField.setForeground(COLOR_TEXT);
        textField.setBackground(Color.WHITE);
    }

private void login() {

    String phone = txtPhone.getText().trim();

    String password =
            new String(txtPass.getPassword());

    if (phone.isEmpty() || password.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Vui lòng nhập đầy đủ Tài khoản và Mật khẩu!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    try {

        // ================= LOGIN =================

        AuthController authController =
                new AuthController();

        User user =
                authController.login(phone, password);
        UserModel userModel = userController.getUserByPhone(phone);
        GlobalData.userModel = userModel;
        // ================= LOGIN FAIL =================

        if (user == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Sai tài khoản hoặc mật khẩu!",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // ================= DASHBOARD =================

        DashboardController dashboardController =
                new DashboardController(user.getId());

        new MainDashboard(dashboardController);

        dispose();

    } catch (IllegalArgumentException ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
        );

    } catch (Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Lỗi kết nối cơ sở dữ liệu!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}