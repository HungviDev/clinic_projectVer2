package ui.auth;

import controller.user.AuthController;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;



public class RegisterForm extends JFrame {

    private JTextField txtName, txtPhone;
    private JPasswordField txtPass;
    private boolean isPasswordVisible = false;

    // Định nghĩa các màu sắc chủ đạo (Giống LoginForm)
    private final Color COLOR_PRIMARY = new Color(34, 166, 76);    
    private final Color COLOR_BUTTON = new Color(140, 198, 63);    
    private final Color COLOR_TEXT = new Color(70, 70, 70);        
    private final Color COLOR_LINK = new Color(230, 120, 30);      
    private final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public RegisterForm() {
        setTitle("Đăng ký tài khoản - Nha Khoa Viet Smile");
        setSize(400, 720); // Kích thước dài hơn Login một chút vì có 3 trường nhập liệu
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        setResizable(false); 

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new LoginForm(); // Quay lại trang đăng nhập
            }
        });

        // ================= HEADER PANEL =================
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_PRIMARY);
        headerPanel.setPreferredSize(new Dimension(400, 180));
        headerPanel.setLayout(new GridBagLayout());

        JLabel lblLogo = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/resources/logo.png"); 
        if (logoIcon.getIconWidth() > 0) {
            lblLogo.setIcon(logoIcon);
        } else {
            lblLogo.setText("VIET SMILE");
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
        gbc.insets = new Insets(6, 0, 6, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int gridy = 0;

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("Đăng ký tài khoản", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(10, 0, 15, 0);
        formPanel.add(lblTitle, gbc);

        // --- Họ tên ---
        JLabel lblNameTitle = new JLabel("Họ và tên");
        lblNameTitle.setFont(FONT_REGULAR);
        lblNameTitle.setForeground(COLOR_TEXT);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 2, 40);
        formPanel.add(lblNameTitle, gbc);

        JPanel nameInputPanel = createInputPanel("src/resources/user.jpg", "👤");
        txtName = new JTextField();
        setupTextField(txtName);
        nameInputPanel.add(txtName, BorderLayout.CENTER);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 10, 40);
        formPanel.add(nameInputPanel, gbc);

        // --- Tài khoản / Phone ---
        JLabel lblPhoneTitle = new JLabel("Số điện thoại");
        lblPhoneTitle.setFont(FONT_REGULAR);
        lblPhoneTitle.setForeground(COLOR_TEXT);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 2, 40);
        formPanel.add(lblPhoneTitle, gbc);

        JPanel phoneInputPanel = createInputPanel("src/resources/phone.png", "📞");
        txtPhone = new JTextField();
        setupTextField(txtPhone);
        phoneInputPanel.add(txtPhone, BorderLayout.CENTER);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 10, 40);
        formPanel.add(phoneInputPanel, gbc);

        // --- Mật khẩu ---
        JLabel lblPassTitle = new JLabel("Mật khẩu");
        lblPassTitle.setFont(FONT_REGULAR);
        lblPassTitle.setForeground(COLOR_TEXT);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 2, 40);
        formPanel.add(lblPassTitle, gbc);

        JPanel passInputPanel = createInputPanel("src/resources/lock.png", "🔒");
        txtPass = new JPasswordField();
        setupTextField(txtPass);
        passInputPanel.add(txtPass, BorderLayout.CENTER);

        // Nút ẩn hiện mật khẩu
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

        // --- Nút Đăng ký ---
        JButton btnRegister = new JButton("Đăng ký");
        btnRegister.setFont(FONT_BOLD);
        btnRegister.setBackground(COLOR_BUTTON);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0)); 
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 20, 40);
        formPanel.add(btnRegister, gbc);

        // --- Link Quay Lại Đăng Nhập ---
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginLinkPanel.setBackground(Color.WHITE);
        
        JLabel lblQuestion = new JLabel("Đã có tài khoản?");
        lblQuestion.setFont(FONT_REGULAR);
        lblQuestion.setForeground(COLOR_TEXT);
        
        JLabel lblLoginLink = new JLabel("Đăng nhập");
        lblLoginLink.setFont(FONT_BOLD);
        lblLoginLink.setForeground(COLOR_LINK);
        lblLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginLinkPanel.add(lblQuestion);
        loginLinkPanel.add(lblLoginLink);
        
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 20, 40);
        formPanel.add(loginLinkPanel, gbc);

        // Đẩy Panel lên trên cùng
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(formPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);

        // ================= SỰ KIỆN =================
        btnRegister.addActionListener(e -> register());
        
        lblLoginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginForm(); // Quay lại trang đăng nhập
                dispose();       // Đóng trang đăng ký
            }
        });

        setVisible(true);
    }

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

    private void setupTextField(JTextField textField) {
        textField.setBorder(null);
        textField.setFont(FONT_REGULAR);
        textField.setForeground(COLOR_TEXT);
        textField.setBackground(Color.WHITE);
    }

    void register() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (name.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AuthController controller = new AuthController();
            controller.register(name, phone, pass);
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.");
            new LoginForm();
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối hoặc số điện thoại đã tồn tại!", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }
}