package ui.patient;

import config.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class ContactPanel extends JPanel {

    // ================= BẢNG MÀU PASTEL BLUE ĐỒNG BỘ =================
    private final Color COLOR_BG = new Color(248, 250, 252);      // Nền chính xám pha xanh nhạt
    private final Color TEXT_DARK = new Color(44, 62, 80);        // Chữ xanh than đậm (dễ đọc)
    private final Color TEXT_MUTED = new Color(117, 117, 117);    // Chữ xám mờ

    private final Color HEADER_COLOR_PASTEL = new Color(133, 193, 233);  // Xanh biển pastel vừa (Logo)
    private final Color SIDEBAR_COLOR_PASTEL = new Color(214, 234, 248); // Xanh biển pastel cực nhạt (Badge)
    private final Color HOVER_COLOR_PASTEL = new Color(93, 173, 226);    // Xanh pastel đậm hơn (Hover)

    public ContactPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // --- Tiêu đề trang (Dùng tông màu Pastel) ---
        JLabel lblTitle = new JLabel("Danh sách chi nhánh Liên Hệ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(HEADER_COLOR_PASTEL); // Đổi màu tiêu đề chính
        lblTitle.setBorder(new EmptyBorder(25, 30, 15, 30));
        add(lblTitle, BorderLayout.NORTH);

        // --- Danh sách chi nhánh (Tông Pastel) ---
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(COLOR_BG);
        listPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        // Thêm các thẻ chi nhánh theo ảnh
        listPanel.add(createBranchCard("Giáp Nhất", 
                "Số 229, Giáp Nhất, Thanh Xuân, Hà Nội", 
                new String[]{"0346 397 399"}));
        listPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Khoảng cách giữa các card

        listPanel.add(createBranchCard("Sài Gòn", 
                "Số 26-28 đường số 6 KĐT Hà Đô 118 đường 3/2, Quận 10, HCM", 
                new String[]{"0336 637 383", "0329 637 383"}));
        listPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        listPanel.add(createBranchCard("Cầu Giấy", 
                "Số 70, Trần Thái Tông, Cầu Giấy, Hà Nội", 
                new String[]{"0869 079 800", "0383 986 813"}));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBranchCard(String branchName, String address, String[] phones) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE); // Giữ card màu trắng để nổi bật
        card.setBorder(BorderFactory.createCompoundBorder(
                // Viền xám mờ pha chút xanh
                new LineBorder(new Color(230, 235, 240), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250)); // Chiếm hết chiều ngang
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Tên chi nhánh (Tông Pastel)
        JLabel lblName = new JLabel("Chi nhánh: " + branchName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblName.setForeground(HOVER_COLOR_PASTEL); // Đổi màu nhấn mạnh tên chi nhánh
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblName);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Địa chỉ (Màu than đậm dễ đọc)
        JLabel lblAddress = new JLabel("<html> <b>Địa chỉ:</b> " + address + "</html>");
        lblAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblAddress.setForeground(TEXT_DARK);
        lblAddress.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblAddress);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Liên hệ ngay (Dùng màu Muted nhạt hơn)
        JLabel lblContact = new JLabel("Liên hệ ngay:");
        lblContact.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblContact.setForeground(TEXT_MUTED);
        lblContact.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblContact);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        // Các nút số điện thoại (Tông Pastel cực nhạt)
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        phonePanel.setBackground(Color.WHITE);
        phonePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        for (String phone : phones) {
            JLabel lblPhone = new JLabel(phone + "");
            lblPhone.setOpaque(true);
            lblPhone.setBackground(SIDEBAR_COLOR_PASTEL); // Nền xanh biển cực nhạt (Badge)
            lblPhone.setForeground(HOVER_COLOR_PASTEL);  // Chữ xanh pastel đậm
            lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblPhone.setBorder(new EmptyBorder(8, 15, 8, 15));
            lblPhone.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Xử lý sự kiện click
            lblPhone.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("tel:" + phone.replace(" ", "")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ContactPanel.this, "Đang gọi tới số: " + phone);
                    }
                }
            });

            phonePanel.add(lblPhone);
            phonePanel.add(Box.createRigidArea(new Dimension(12, 0))); // Khoảng cách giữa các sđt
        }
        card.add(phonePanel);

        return card;
    }
}