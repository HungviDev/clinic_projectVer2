package ui.patient;

import ui.auth.LoginForm;
import ui.auth.MainDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfilePanel extends JPanel {

    // ================= BẢNG MÀU PASTEL BLUE ĐỒNG BỘ =================
    private final Color COLOR_BG = new Color(248, 250, 252);      // Nền chính xám pha xanh nhạt
    private final Color TEXT_DARK = new Color(44, 62, 80);        // Chữ xanh than đậm (dễ đọc)
    private final Color TEXT_MUTED = new Color(149, 165, 166);    // Chữ xám xám nhạt (Muted)

    private final Color HEADER_COLOR_PASTEL = new Color(133, 193, 233);  // Xanh biển pastel vừa (Avatar/Menu Icon)
    private final Color SIDEBAR_COLOR_PASTEL = new Color(214, 234, 248); // Xanh biển pastel cực nhạt (Hover)
    private final Color HOVER_COLOR_PASTEL = new Color(93, 173, 226);    // Xanh pastel đậm hơn

    public ProfilePanel(String userName, String phone) {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(COLOR_BG);

        // ================= 1. HEADER (Thông tin cá nhân - Tông Pastel) =================
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(235, 240, 245))); // Vạch chia đáy

        // Avatar tròn (Tông Pastel)
        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 45));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(SIDEBAR_COLOR_PASTEL); // Nền xanh nhạt
        lblAvatar.setForeground(HEADER_COLOR_PASTEL); // Icon màu pastel vừa
        lblAvatar.setPreferredSize(new Dimension(80, 80));
        lblAvatar.setBorder(BorderFactory.createLineBorder(HEADER_COLOR_PASTEL, 1, true)); // Viền bo nhẹ
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblName = new JLabel(userName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblName.setForeground(TEXT_DARK);
        
        JLabel lblPhone = new JLabel(phone);
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblPhone.setForeground(TEXT_MUTED);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        infoPanel.add(lblName);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        infoPanel.add(lblPhone);

        headerPanel.add(lblAvatar);
        headerPanel.add(infoPanel);
        
        mainContent.add(headerPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15))); // Khoảng xám phân cách

        // ================= 2. MENU ITEMS (Tông Xanh Pastel) =================
        JPanel menuGroup1 = new JPanel();
        menuGroup1.setLayout(new BoxLayout(menuGroup1, BoxLayout.Y_AXIS));
        menuGroup1.setBackground(Color.WHITE);
        
        // Gắn lệnh switchPage vào các menu
        menuGroup1.add(createMenuItem("", "Lịch hẹn của tôi", true, () -> switchPage("SCHEDULE")));
        // menuGroup1.add(createMenuItem("", "Lịch sử mua hàng", true, () -> switchPage("ORDER_HISTORY")));
        menuGroup1.add(createMenuItem("", "Lịch sử thanh toán", true, () -> switchPage("PAYMENT_HISTORY")));
        
        // ĐÂY LÀ CHỖ CHUYỂN TRANG LỊCH SỬ ĐIỀU TRỊ:
        menuGroup1.add(createMenuItem("", "Lịch sử điều trị", true, () -> switchPage("MEDICAL_RECORD")));
        
        mainContent.add(menuGroup1);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15))); // Khoảng xám phân cách

        // ================= 3. TRỢ GIÚP & ĐĂNG XUẤT (Tông Xanh Pastel) =================
        JPanel menuGroup2 = new JPanel();
        menuGroup2.setLayout(new BoxLayout(menuGroup2, BoxLayout.Y_AXIS));
        menuGroup2.setBackground(Color.WHITE);
        
        // Trợ giúp và Đăng xuất thì chưa cần nhảy trang CardLayout nên truyền null
        menuGroup2.add(createMenuItem("", "Chỉnh sửa thông tin", true, () -> switchPage("EDIT_PROFILE")));
        menuGroup2.add(createMenuItem("", "Đăng xuất", false, null));
        
        mainContent.add(menuGroup2);

        // Thêm vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= HÀM HỖ TRỢ CHUYỂN TRANG =================
    private void switchPage(String pageName) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof MainDashboard) {
            ((MainDashboard) window).showPage(pageName);
        } else {
            System.out.println("Cần đảm bảo MainDashboard có hàm showPage()");
        }
    }

    // Hàm tạo 1 dòng Menu: THÊM THAM SỐ Runnable onClick
    private JPanel createMenuItem(String icon, String text, boolean hasBottomBorder, Runnable onClick) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(800, 60));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        Color borderColor = new Color(235, 240, 245); // Vạch chia mờ pha xanh

        if (hasBottomBorder) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, borderColor),
                    new EmptyBorder(0, 25, 0, 25)
            ));
        } else {
            panel.setBorder(new EmptyBorder(0, 25, 0, 25));
        }

        // Icon + Text (Tông Xanh Pastel)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 12));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setOpaque(false); // Xuyên thấu để ăn theo màu hover của panel cha
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblIcon.setForeground(HEADER_COLOR_PASTEL); // Đổi màu icon menu
        
        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblText.setForeground(TEXT_DARK);
        
        leftPanel.add(lblIcon);
        leftPanel.add(lblText);

        // Mũi tên (>) bên phải
        JLabel lblArrow = new JLabel(">");
        lblArrow.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblArrow.setForeground(TEXT_MUTED);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(lblArrow, BorderLayout.EAST);

        // Hiệu ứng hover & click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(SIDEBAR_COLOR_PASTEL); // Đổi nền sang xanh biển cực nhạt khi hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE); // Trả lại màu trắng
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (text.equals("Đăng xuất")) {
                    int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn đăng xuất khỏi Nha Khoa?", "Xác nhận Đăng xuất", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Window window = SwingUtilities.getWindowAncestor(panel);
                        window.dispose(); // Đóng Dashboard
                        new LoginForm();   // Mở lại Login
                    }
                } else if (onClick != null) {
                    // Nếu có truyền hành động (switchPage) thì chạy nó
                    onClick.run();
                } else {
                    // Dành cho nút Trợ giúp chưa có hành động
                    JOptionPane.showMessageDialog(panel, "Mở chức năng: " + text);
                }
            }
        });

        return panel;
    }
}