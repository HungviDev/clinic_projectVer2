package ui.patient;

import ui.auth.MainDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.admin.UserController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;

public class EditProfilePanel extends JPanel {

    // ================= BẢNG MÀU =================
    private final Color COLOR_BG = new Color(248, 250, 252); 
    private final Color TEXT_DARK = new Color(44, 62, 80); 
    private final Color TEXT_MUTED = new Color(149, 165, 166); 
    private final Color PRIMARY_COLOR = new Color(93, 173, 226); // Xanh dương pastel chính

    private JTextField txtFullName, txtPhone, txtBirthDate, txtAddress, txtemail;
    private int loggedInUserId;

    public EditProfilePanel(int userId, String currentFullName, String currentPhone, Date currentBirthDate, String currentAddress, String currentemail) {
        this.loggedInUserId = userId;
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
                switchPage("PROFILE"); // Trở về trang Profile
            }
        });

        JLabel lblTitle = new JLabel("Thông tin cá nhân", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_DARK);

        // Nút Lưu (Góc phải)
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
        
        // Tạo khối avatar với nút camera giả lập
        JPanel avatarWrapper = new JPanel(new BorderLayout());
        avatarWrapper.setOpaque(false);
        
        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(236, 240, 241)); // Xám nhạt
        lblAvatar.setForeground(TEXT_MUTED);
        lblAvatar.setPreferredSize(new Dimension(100, 100));
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true)); 
        
        JLabel lblCamera = new JLabel("📷");
        lblCamera.setOpaque(true);
        lblCamera.setBackground(Color.WHITE);
        lblCamera.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        lblCamera.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCamera.setToolTipText("Đổi ảnh đại diện");
        
        avatarWrapper.add(lblAvatar, BorderLayout.CENTER);
        avatarContainer.add(avatarWrapper);
        
        bodyPanel.add(avatarContainer);

        // 2. Khu vực nhập thông tin (Form Fields)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Tạo các trường nhập liệu với dữ liệu hiện tại
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
        
        // Khoảng trống đẩy nội dung lên trên
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
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)), // Gạch chân
                new EmptyBorder(15, 0, 15, 0)
        ));

        // Nhãn bên trái
        JLabel lblLabel = new JLabel(labelText);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblLabel.setForeground(TEXT_MUTED);
        lblLabel.setPreferredSize(new Dimension(140, 30));

        // Ô nhập bên phải
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setForeground(TEXT_DARK);
        textField.setBorder(null); // Bỏ viền để nó hòa vào nền giống app mobile
        textField.setHorizontalAlignment(JTextField.RIGHT); // Chữ đẩy sang phải
        textField.setBackground(Color.WHITE);

        row.add(lblLabel, BorderLayout.WEST);
        row.add(textField, BorderLayout.CENTER);

        return row;
    }

    // ================= HÀM XỬ LÝ LƯU =================
    // ================= HÀM XỬ LÝ LƯU (CHUẨN MVC) =================
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
            // Khởi tạo Controller và gọi hàm update
            UserController userController = new UserController();
            boolean isSuccess = userController.updateProfile(loggedInUserId, newName, newPhone, newBirth, newAddress, newEmail);
            
            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                
                // GỌI HÀM LÀM MỚI BÊN MAINDASHBOARD
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof MainDashboard) {
                    ((MainDashboard) window).reloadUserData(); // Cập nhật lại Header & Panel
                    ((MainDashboard) window).showPage("PROFILE"); // Trượt về trang Profile mới
                } 
                // switchPage("PROFILE"); // Trở về màn hình Profile
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra, không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IllegalArgumentException ex) {
            // Bắt lỗi định dạng ngày tháng ném ra từ Controller
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
}