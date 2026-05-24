package ui.patient;

import config.DBConnection;
import controller.user.ServiceController;
import ui.auth.MainDashboard;
import model.Service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.DecimalFormat;

public class ServicePatientPanel extends JPanel {

    // ================= BẢNG MÀU PASTEL HIỆN ĐẠI =================
    private final Color COLOR_BG = new Color(248, 250, 252);      // Nền chính xám pha xanh nhạt
    private final Color TEXT_DARK = new Color(44, 62, 80);        // Chữ xanh than đậm
    private final Color TEXT_MUTED = new Color(149, 165, 166);    // Chữ xám mờ
    private final Color PRICE_COLOR = new Color(231, 76, 60);     // Đỏ cam nổi bật cho giá tiền
    
    private final Color HEADER_COLOR_PASTEL = new Color(133, 193, 233);  // Xanh biển pastel (Tiêu đề)
    
    // Màu cho nút Đặt Lịch
    private final Color BTN_BG_COLOR = new Color(162, 217, 139); 
    private final Color BTN_HOVER_COLOR = new Color(139, 195, 74);
    private final Color BTN_TEXT_COLOR = new Color(46, 125, 50);

    private JPanel listPanel;
    private DecimalFormat moneyFormat = new DecimalFormat("#,### đ"); // Định dạng tiền tệ

    public ServicePatientPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // --- Tiêu đề trang ---
        JLabel lblTitle = new JLabel("Dịch vụ Nha khoa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(HEADER_COLOR_PASTEL);
        lblTitle.setBorder(new EmptyBorder(25, 30, 20, 30));
        add(lblTitle, BorderLayout.NORTH);

        // --- Vùng chứa danh sách Dịch Vụ ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(COLOR_BG); // Nền xám nhạt để nổi bật các thẻ trắng
        listPanel.setBorder(new EmptyBorder(0, 30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Lăn chuột mượt hơn
        scrollPane.getViewport().setBackground(COLOR_BG);
        add(scrollPane, BorderLayout.CENTER);

        // --- Load Dữ liệu từ Database ---
        loadServicesFromDatabase();
    }

    // ================= HÀM LẤY DỮ LIỆU TỪ DB =================
    private void loadServicesFromDatabase() {
        listPanel.removeAll();

        ServiceController controller = new ServiceController();
        var services = controller.getAllServices();

        if (services.isEmpty()) {
            JLabel lblEmpty = new JLabel("Hiện chưa có dịch vụ nào trên hệ thống.");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(TEXT_MUTED);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

            listPanel.add(Box.createRigidArea(new Dimension(0, 50)));
            listPanel.add(lblEmpty);
        } else {
            for (var s : services) {
                // Đảm bảo model Service của bạn có hàm getPrice()
                listPanel.add(
                        createServiceCard(
                                s.getId(),
                                s.getName(),
                                s.getDescription(),
                                s.getImage(),
                                s.getPrice() // Thêm giá tiền vào đây
                        )
                );
                // Khoảng cách giữa các thẻ
                listPanel.add(Box.createRigidArea(new Dimension(0, 15))); 
            }
        }

        listPanel.add(Box.createVerticalGlue()); // Đẩy các thẻ lên trên cùng
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ================= HÀM TẠO 1 THẺ DỊCH VỤ CẢI TIẾN =================
    private JPanel createServiceCard(int serviceId, String name, String description, String imagePath, double price) {
        // Card Panel bọc ngoài cùng
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(1000, 150)); // Giới hạn chiều cao thẻ
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Bo viền và tạo bóng nhẹ (Padding)
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // 1. KHOANG BÊN TRÁI: ẢNH DỊCH VỤ
        JLabel lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(110, 110));
        lblImage.setMinimumSize(new Dimension(110, 110));
        lblImage.setMaximumSize(new Dimension(110, 110));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Load ảnh hoặc hiển thị icon mặc định nếu rỗng
        ImageIcon icon = loadScaledImage(imagePath, 110, 110);
        if (icon != null) {
            lblImage.setIcon(icon);
        } else {
            lblImage.setOpaque(true);
            lblImage.setBackground(new Color(240, 244, 248)); // Nền xám xanh nhạt
            lblImage.setText("🦷"); // Icon cái răng làm mặc định
            lblImage.setFont(new Font("Segoe UI", Font.PLAIN, 45));
        }
        card.add(lblImage, BorderLayout.WEST);

        // 2. KHOANG Ở GIỮA: THÔNG TIN (TÊN & MÔ TẢ)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblName.setForeground(TEXT_DARK);
        
        // Dùng HTML để giới hạn chiều rộng chữ và tự động xuống dòng
        String safeDesc = (description != null) ? description : "Đang cập nhật mô tả...";
        JLabel lblDesc = new JLabel("<html><div style='width: 350px; line-height: 1.5;'>" + safeDesc + "</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(TEXT_MUTED);
        lblDesc.setAlignmentY(Component.TOP_ALIGNMENT);
        
        infoPanel.add(lblName);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(lblDesc);
        
        card.add(infoPanel, BorderLayout.CENTER);

        // 3. KHOANG BÊN PHẢI: GIÁ TIỀN & NÚT ĐẶT LỊCH
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setPreferredSize(new Dimension(160, 110)); // Cố định chiều rộng cột phải
        
        // Giá tiền (Góc trên cùng bên phải)
        JLabel lblPrice = new JLabel(moneyFormat.format(price));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblPrice.setForeground(PRICE_COLOR);
        lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Nút Đặt lịch (Góc dưới cùng bên phải)
        JButton btnBook = new JButton("Đặt lịch");
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBook.setBackground(BTN_BG_COLOR);
        btnBook.setForeground(BTN_TEXT_COLOR);
        btnBook.setFocusPainted(false);
        btnBook.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnBook.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng Hover
        btnBook.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBook.setBackground(BTN_HOVER_COLOR);
                btnBook.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnBook.setBackground(BTN_BG_COLOR);
                btnBook.setForeground(BTN_TEXT_COLOR);
            }
        });

        btnBook.addActionListener(e -> switchPage("BOOKING"));
        
        actionPanel.add(lblPrice, BorderLayout.NORTH);
        actionPanel.add(btnBook, BorderLayout.SOUTH);
        
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    // ================= HÀM XỬ LÝ ẢNH (CHỐNG LỖI) =================
    private ImageIcon loadScaledImage(String imagePath, int width, int height) {
        if (imagePath == null || imagePath.trim().isEmpty()) return null;
        
        try {
            // Thử load ảnh từ Resources
            URL url = getClass().getResource(imagePath);
            if (url != null) {
                ImageIcon originalIcon = new ImageIcon(url);
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
            
            // Hoặc thử load ảnh trực tiếp từ đường dẫn tuyệt đối
            ImageIcon originalIcon = new ImageIcon(imagePath);
            if (originalIcon.getIconWidth() > 0) {
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + imagePath);
        }
        return null; 
    }

    // ================= HÀM HỖ TRỢ CHUYỂN TRANG =================
    private void switchPage(String pageName) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof MainDashboard) {
            ((MainDashboard) window).showPage(pageName);
        } else {
            System.out.println("Lỗi: Không tìm thấy MainDashboard để chuyển trang!");
        }
    }
}