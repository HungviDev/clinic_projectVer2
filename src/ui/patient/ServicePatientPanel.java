package ui.patient;

import config.DBConnection; // Import kết nối Database của bạn
import controller.user.ServiceController;
import dao.user.ServiceDAO;
import model.user.Service;
import ui.auth.MainDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServicePatientPanel extends JPanel {

    // ================= BẢNG MÀU PASTEL BLUE ĐỒNG BỘ =================
    private final Color COLOR_BG = new Color(248, 250, 252);      // Nền chính xám pha xanh nhạt
    private final Color TEXT_DARK = new Color(44, 62, 80);        // Chữ xanh than đậm
    private final Color TEXT_MUTED = new Color(117, 117, 117);    // Chữ xám mờ
    
    private final Color HEADER_COLOR_PASTEL = new Color(133, 193, 233);  // Xanh biển pastel (Tiêu đề)
    
    // Màu cho nút Đặt Lịch (Xanh lá pastel cho giống ảnh mẫu nhưng vẫn nhẹ nhàng)
    private final Color BTN_BG_COLOR = new Color(162, 217, 139); 
    private final Color BTN_HOVER_COLOR = new Color(139, 195, 74);
    private final Color BTN_TEXT_COLOR = new Color(46, 125, 50);

    private JPanel listPanel;

    public ServicePatientPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // --- Tiêu đề trang ---
        JLabel lblTitle = new JLabel("Dịch vụ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR_PASTEL);
        lblTitle.setBorder(new EmptyBorder(25, 30, 15, 30));
        add(lblTitle, BorderLayout.NORTH);

        // --- Vùng chứa danh sách Dịch Vụ ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE); // Nền trắng để nổi bật các vạch chia

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
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

            JLabel lblEmpty = new JLabel("Không có dịch vụ nào.");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(TEXT_MUTED);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

            listPanel.add(Box.createRigidArea(new Dimension(0, 40)));
            listPanel.add(lblEmpty);

        } else {

            for (var s : services) {

                listPanel.add(
                        createServiceCard(
                                s.getId(),
                                s.getName(),
                                s.getDescription(),
                                s.getImage()
                        )
                );
            }
        }

        listPanel.add(Box.createVerticalGlue());

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ================= HÀM TẠO 1 THẺ DỊCH VỤ =================
    private JPanel createServiceCard(int serviceId, String name, String description, String imagePath) {
        // Card Panel bọc ngoài cùng
        JPanel card = new JPanel(new BorderLayout(20, 0)); // Khoảng cách giữa ảnh và chữ là 20px
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140)); // Chiều cao tối đa
        
        // Vạch phân cách màu xám mờ ở đáy
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                new EmptyBorder(20, 30, 20, 30) // Căn lề padding (Trên, Trái, Dưới, Phải)
        ));

        // 1. Ảnh bên trái
        JLabel lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(100, 100)); // Kích thước ảnh vuông
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Cố gắng load ảnh, nếu không có sẽ hiển thị màu nền thay thế
        ImageIcon icon = loadScaledImage(imagePath, 100, 100);
        if (icon != null) {
            lblImage.setIcon(icon);
        } else {
            lblImage.setOpaque(true);
            lblImage.setBackground(new Color(230, 240, 250)); // Màu nền pastel nếu thiếu ảnh
            lblImage.setText("📷");
            lblImage.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            lblImage.setForeground(HEADER_COLOR_PASTEL);
        }
        card.add(lblImage, BorderLayout.WEST);

        // 2. Nội dung chữ ở giữa
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Đẩy text xuống một xíu cho cân đối
        
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(TEXT_DARK);
        
        JLabel lblDesc = new JLabel("<html>" + (description != null ? description : "") + "</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDesc.setForeground(TEXT_MUTED);
        
        textPanel.add(lblName);
        textPanel.add(Box.createRigidArea(new Dimension(0, 8))); // Khoảng cách giữa tên và mô tả
        textPanel.add(lblDesc);
        
        card.add(textPanel, BorderLayout.CENTER);

        // 3. Nút "Đặt lịch" ở góc phải dưới
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnBook = new JButton("Đặt lịch");
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBook.setBackground(BTN_BG_COLOR);
        btnBook.setForeground(BTN_TEXT_COLOR);
        btnBook.setFocusPainted(false);
        btnBook.setBorder(new EmptyBorder(8, 20, 8, 20)); // Padding cho nút
        btnBook.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng Hover cho nút
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

        // Xử lý sự kiện click
        btnBook.addActionListener(e -> {
            switchPage("BOOKING");
        });
        
        // Add nút vào SOUTH của actionPanel để nó nằm ở dưới cùng bên phải
        actionPanel.add(btnBook, BorderLayout.SOUTH);
        
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    // ================= HÀM XỬ LÝ ẢNH (CHỐNG LỖI) =================
    private ImageIcon loadScaledImage(String imagePath, int width, int height) {
        if (imagePath == null || imagePath.trim().isEmpty()) return null;
        
        try {
            // Thử load ảnh từ Resources (src/img/...)
            URL url = getClass().getResource(imagePath);
            if (url != null) {
                ImageIcon originalIcon = new ImageIcon(url);
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
            
            // Hoặc thử load ảnh trực tiếp từ đường dẫn máy tính (nếu lưu dạng D:/images/...)
            ImageIcon originalIcon = new ImageIcon(imagePath);
            if (originalIcon.getIconWidth() > 0) {
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + imagePath);
        }
        return null; // Trả về null nếu ảnh bị lỗi
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