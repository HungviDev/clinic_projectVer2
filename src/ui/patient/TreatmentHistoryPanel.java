package ui.patient;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import model.TreatmentHistory;
import controller.TreatmentHistoryController;
import dao.TreatmentHistoryDAO;

public class TreatmentHistoryPanel extends JPanel {

    // ================= BẢNG MÀU PASTEL BLUE ĐỒNG BỘ =================
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color TEXT_MUTED = new Color(117, 117, 117);
    
    private final Color HEADER_COLOR_PASTEL = new Color(133, 193, 233);
    private final Color CARD_HEADER_BG = new Color(228, 241, 250); // Xanh nhạt cho thanh tiêu đề thẻ
    private final Color ICON_COLOR = new Color(93, 173, 226);

    private JPanel listPanel;

    public TreatmentHistoryPanel(int patientId) {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // --- Tiêu đề trang ---
        JLabel lblTitle = new JLabel("Hồ sơ bệnh án / Lịch sử điều trị");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_COLOR_PASTEL);
        lblTitle.setBorder(new EmptyBorder(25, 30, 15, 30));
        add(lblTitle, BorderLayout.NORTH);

        // --- Vùng chứa danh sách ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(COLOR_BG);
        listPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- Load Dữ liệu ---
        loadHistoryFromDatabase(patientId);
    }

    // ================= LẤY DỮ LIỆU TỪ DATABASE =================
// ================= LẤY DỮ LIỆU TỪ DATABASE (KHÔNG DÙNG DỮ LIỆU GIẢ) =================
private void loadHistoryFromDatabase(int patientId) {

    listPanel.removeAll();

    TreatmentHistoryController controller =
            new TreatmentHistoryController();

    var histories = controller.getByPatientId(patientId);

    if (histories.isEmpty()) {

        JLabel lblEmpty =
                new JLabel("Bạn chưa có hồ sơ bệnh án hay lịch sử điều trị nào.");

        lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblEmpty.setForeground(TEXT_MUTED);
        lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        listPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        listPanel.add(lblEmpty);

    } else {

        SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy - HH:mm");

        for (var h : histories) {

            String dateStr =
                    h.getCreatedAt() != null
                            ? sdf.format(h.getCreatedAt())
                            : "Không rõ thời gian";

            listPanel.add(
                    createRecordCard(
                            dateStr,
                            h.getDoctorName(),
                            h.getDiagnosis(),
                            h.getTreatmentPlan()
                    )
            );

            listPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
    }

    listPanel.revalidate();
    listPanel.repaint();
}

    // ================= TẠO THẺ HỒ SƠ (CARD DESIGN) =================
    private JPanel createRecordCard(String date, String doctorName, String diagnosis, String treatment) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(800, 180));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Viền thẻ
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 230, 240), 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // --- 1. THANH TIÊU ĐỀ (Hiện ngày tháng) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setBackground(CARD_HEADER_BG); // Màu nền xanh nhạt
        
        JLabel lblDateIcon = new JLabel("📅");
        lblDateIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JLabel lblDateText = new JLabel("Ngày khám: " + date);
        lblDateText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDateText.setForeground(TEXT_DARK);
        
        headerPanel.add(lblDateIcon);
        headerPanel.add(lblDateText);
        card.add(headerPanel, BorderLayout.NORTH);

        // --- 2. NỘI DUNG CHÍNH ---
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Bác sĩ
        bodyPanel.add(createInfoRow("👨‍⚕️", "Bác sĩ phụ trách:", doctorName));
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Chẩn đoán
        bodyPanel.add(createInfoRow("🩺", "Chẩn đoán:", diagnosis));
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Kế hoạch điều trị
        bodyPanel.add(createInfoRow("📋", "Kế hoạch điều trị:", treatment));

        card.add(bodyPanel, BorderLayout.CENTER);

        return card;
    }

    // Hàm phụ trợ tạo từng dòng thông tin (Icon + Label + Text)
    private JPanel createInfoRow(String icon, String title, String content) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblIcon.setForeground(ICON_COLOR);
        
        // Tiêu đề (Chữ nhạt hơn)
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(TEXT_MUTED);
        lblTitle.setPreferredSize(new Dimension(140, 20)); // Cố định chiều rộng để các dòng căn hàng dọc bằng nhau

        // Nội dung (Chữ đậm)
        JLabel lblContent = new JLabel("<html><b>" + (content != null ? content : "Đang cập nhật...") + "</b></html>");
        lblContent.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblContent.setForeground(TEXT_DARK);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(lblIcon);
        leftPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        leftPanel.add(lblTitle);

        row.add(leftPanel, BorderLayout.WEST);
        row.add(lblContent, BorderLayout.CENTER);

        return row;
    }
}