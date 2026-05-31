package ui.patient;

import ui.auth.MainDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.user.UserController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Date;
import java.net.URL;

public class EditProfilePanel extends JPanel {

    // ================= BẢNG MÀU =================
    private final Color COLOR_BG = new Color(248, 250, 252); 
    private final Color TEXT_DARK = new Color(44, 62, 80); 
    private final Color TEXT_MUTED = new Color(149, 165, 166); 
    private final Color PRIMARY_COLOR = new Color(93, 173, 226); // Xanh dương pastel chính

    private JTextField txtFullName, txtPhone, txtBirthDate, txtAddress, txtemail;
    private int loggedInUserId;
    private String avatarPath; 
    
    // BIẾN LƯU FILE ẢNH VỪA CHỌN
    private File selectedAvatarFile = null; 

    public EditProfilePanel(int userId, String currentFullName, String currentPhone, Date currentBirthDate, String currentAddress, String currentemail, String currentAvatarPath) {
        this.loggedInUserId = userId;
        this.avatarPath = currentAvatarPath; 
        
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // ================= HEADER: Tiêu đề và Nút Quay Lại =================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Nút Back
        JLabel lblBack = new JLabel("←");
        lblBack.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBack.setForeground(TEXT_DARK);
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPage("PROFILE");
            }
        });

        JLabel lblTitle = new JLabel("Thông tin cá nhân", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_DARK);

        // Nút Lưu
        JLabel lblSave = new JLabel("Lưu");
        lblSave.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSave.setForeground(PRIMARY_COLOR);
        lblSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveProfileInfo();
            }
        });

        headerPanel.add(lblBack, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(lblSave, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // ================= NỘI DUNG CHÍNH (Form) =================
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBackground(Color.WHITE);
        
        // 1. Khu vực Avatar
        JPanel avatarContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        avatarContainer.setBackground(Color.WHITE);
        
        JPanel avatarWrapper = new JPanel(new BorderLayout());
        avatarWrapper.setOpaque(false);
        
        JLabel lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(100, 100));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setVerticalAlignment(SwingConstants.CENTER);
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(236, 240, 241)); 
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true)); 

        // Load ảnh avatar hiện tại từ DB
        ImageIcon avatarIcon = loadScaledImage(this.avatarPath, 100, 100);
        if (avatarIcon != null) {
            lblAvatar.setIcon(avatarIcon);
        } else {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 50));
            lblAvatar.setForeground(TEXT_MUTED);
        }
        
        // Nút Camera
        JLabel lblCamera = new JLabel("📷", SwingConstants.CENTER);
        lblCamera.setOpaque(true);
        lblCamera.setBackground(Color.WHITE);
        lblCamera.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        lblCamera.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCamera.setToolTipText("Đổi ảnh đại diện");
        
        // SỰ KIỆN CLICK CHỌN ẢNH TỪ MÁY TÍNH
        lblCamera.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn ảnh đại diện mới");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "jpeg", "png"));
                
                int result = fileChooser.showOpenDialog(EditProfilePanel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedAvatarFile = fileChooser.getSelectedFile(); 
                    
                    // Hiển thị xem trước ảnh ngay lập tức
                    ImageIcon previewIcon = new ImageIcon(selectedAvatarFile.getAbsolutePath());
                    Image img = previewIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lblAvatar.setIcon(new ImageIcon(img));
                    lblAvatar.setText(""); // Ẩn icon 👤 đi
                }
            }
        });
        
        avatarWrapper.add(lblAvatar, BorderLayout.CENTER);
        avatarWrapper.add(lblCamera, BorderLayout.SOUTH); // Gắn nút camera ở dưới ảnh
        avatarContainer.add(avatarWrapper);
        
        bodyPanel.add(avatarContainer);

        // 2. Khu vực nhập thông tin (Form Fields)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        txtFullName = new JTextField(currentFullName);
        txtPhone = new JTextField(currentPhone);
        txtBirthDate = new JTextField(currentBirthDate != null ? currentBirthDate.toString() : "");
        txtAddress = new JTextField(currentAddress != null ? currentAddress : "");
        txtemail = new JTextField(currentemail != null ? currentemail : "");
        
        formPanel.add(createInputRow("Họ và tên", txtFullName));
        formPanel.add(createInputRow("Số điện thoại", txtPhone));
        formPanel.add(createInputRow("Ngày sinh", txtBirthDate));
        formPanel.add(createInputRow("Địa chỉ", txtAddress));
        formPanel.add(createInputRow("Email", txtemail));

        bodyPanel.add(formPanel);
        bodyPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= HÀM TẠO DÒNG NHẬP LIỆU =================
    private JPanel createInputRow(String labelText, JTextField textField) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)), 
                new EmptyBorder(15, 0, 15, 0)
        ));

        JLabel lblLabel = new JLabel(labelText);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblLabel.setForeground(TEXT_MUTED);
        lblLabel.setPreferredSize(new Dimension(140, 30));

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setForeground(TEXT_DARK);
        textField.setBorder(null); 
        textField.setHorizontalAlignment(JTextField.RIGHT); 
        textField.setBackground(Color.WHITE);

        row.add(lblLabel, BorderLayout.WEST);
        row.add(textField, BorderLayout.CENTER);

        return row;
    }

    // ================= HÀM XỬ LÝ LƯU =================
    private void saveProfileInfo() {
        String newName = txtFullName.getText().trim();
        String newPhone = txtPhone.getText().trim();
        String newBirth = txtBirthDate.getText().trim();
        String newAddress = txtAddress.getText().trim();
        String newEmail = txtemail.getText().trim();

        if (newName.isEmpty() || newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên và Số điện thoại không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UserController userController = new UserController();
            
            // LƯU Ý CHO BẠN:
            // Hiện tại hàm updateProfile của bạn chỉ nhận 6 tham số.
            // Nếu muốn lưu file ảnh thực tế, bạn cần sửa hàm updateProfile trong UserController 
            // để nhận thêm biến selectedAvatarFile, rồi dùng Java Copy File vào thư mục resources nhé!
            
            boolean isSuccess = userController.updateProfile(loggedInUserId, newName, newPhone, newBirth, newAddress, newEmail, selectedAvatarFile);
            
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof MainDashboard) {
                    ((MainDashboard) window).reloadUserData(); 
                    ((MainDashboard) window).showPage("PROFILE"); 
                } 
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra, không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải nhập đúng định dạng Năm-Tháng-Ngày.\nVí dụ: 2004-04-22", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống không xác định!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= HÀM CHUYỂN TRANG =================
    private void switchPage(String pageName) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof MainDashboard) {
            ((MainDashboard) window).showPage(pageName);
        }
    }

    // ================= HÀM XỬ LÝ ẢNH AVATAR =================
    private ImageIcon loadScaledImage(String imageFileName, int width, int height) {
        if (imageFileName == null || imageFileName.trim().isEmpty() || imageFileName.equalsIgnoreCase("NULL")) {
            return null;
        }
        
        String fullPath = "/resources/" + imageFileName; 
        
        try {
            URL url = getClass().getResource(fullPath);
            if (url != null) {
                ImageIcon originalIcon = new ImageIcon(url);
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Lỗi load avatar: " + fullPath);
        }
        return null; 
    }
}