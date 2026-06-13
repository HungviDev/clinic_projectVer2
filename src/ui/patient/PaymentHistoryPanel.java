package ui.patient;

import service.PaymentService;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import model.user.Payment;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import javax.swing.border.LineBorder;

public class PaymentHistoryPanel extends JPanel {

    // ================= BẢNG MÀU UI =================
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color COLOR_TEXT_DARK = new Color(44, 62, 80);
    private final Color COLOR_TEXT_MUTED = new Color(149, 165, 166);
    private final Color COLOR_PAID = new Color(52, 152, 219);     // Xanh dương cho Đã thanh toán
    private final Color COLOR_PENDING = new Color(231, 76, 60);   // Đỏ cho Chưa thanh toán

    private int loggedInUserId;
    private JPanel listPanel;
    
    // Các nhãn hiển thị tổng tiền tổng hợp
    private JLabel lblTotalCostVal;
    private JLabel lblTotalPaidVal;
    private JLabel lblTotalPendingVal;

    private DecimalFormat moneyFormat = new DecimalFormat("#,### đ");
    private PaymentService paymentService;

    public PaymentHistoryPanel(int userId) {
        paymentService = new PaymentService();
        this.loggedInUserId = userId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Tiêu đề trang ---
        JLabel lblTitle = new JLabel("Lịch sử thanh toán");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(20, 20, 15, 20));
        add(lblTitle, BorderLayout.NORTH);

        // --- KHU VỰC TỔNG HỢP (SUMMARY) ---
        JPanel summaryContainer = new JPanel(new BorderLayout());
        summaryContainer.setBackground(Color.WHITE);
        summaryContainer.setBorder(new MatteBorder(0, 0, 8, 0, COLOR_BG));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel pnlTotal = createSummaryColumn("Tổng chi phí", "0 đ", COLOR_TEXT_DARK);
        JPanel pnlPaid = createSummaryColumn("Đã thanh toán", "0 đ", COLOR_PAID);
        JPanel pnlPending = createSummaryColumn("Chưa thanh toán", "0 đ", COLOR_PENDING);

        lblTotalCostVal = (JLabel) pnlTotal.getComponent(2);
        lblTotalPaidVal = (JLabel) pnlPaid.getComponent(2);
        lblTotalPendingVal = (JLabel) pnlPending.getComponent(2);

        summaryPanel.add(pnlTotal);
        summaryPanel.add(pnlPaid);
        summaryPanel.add(pnlPending);
        summaryContainer.add(summaryPanel, BorderLayout.CENTER);

        // --- KHU VỰC DANH SÁCH GIAO DỊCH ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(summaryContainer, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        // --- Tải dữ liệu từ database ---
        loadPaymentData();
    }

    // ================= LOAD DỮ LIỆU TỪ DATABASE =================
    private void loadPaymentData() {
        listPanel.removeAll();

        double totalPaid = 0;
        double totalPending = 0;

        // Lấy dữ liệu thông qua tầng Service
        List<Payment> payments = paymentService.getPaymentsByUserId(loggedInUserId);
        boolean hasData = !payments.isEmpty();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Payment payment : payments) {
            double amount = payment.getAmount();
            String method = payment.getMethod();
            String status = payment.getStatus();
            
            // Lấy tên Giai đoạn điều trị (stage_name) đã được Mapping qua trường ServiceName
            String stageName = payment.getServiceName();
            if (stageName == null || stageName.trim().isEmpty()) {
                stageName = "Giai đoạn điều trị";
            }

            // ĐỒNG BỘ LOGIC: So sánh trạng thái theo chuỗi Tiếng Việt thực tế trong DB của bạn
            if ("Đã thanh toán".equalsIgnoreCase(status)) {
                totalPaid += amount;
            } else {
                totalPending += amount;
            }

            // Định dạng ngày tháng hiển thị
            String dateStr = "Không rõ";
            if (payment.getCreatedAt() != null) {
                dateStr = payment.getCreatedAt().toLocalDateTime().format(dateFormatter);
            }

            // Định dạng phương thức thanh toán hiển thị
            String methodDisplay = "CARD".equalsIgnoreCase(method) ? "Chuyển khoản" : "Tiền mặt";

            // Render thẻ hiển thị dòng giao dịch (Truyền trực tiếp tên Giai đoạn vào nhãn mô tả)
            listPanel.add(
                    createTransactionCard(
                            dateStr,
                            method,
                            methodDisplay,
                            stageName, // Hiển thị chuẩn xác tên giai đoạn điều trị dưới tên phương thức
                            amount
                    )
            );
        }

        // Tính tổng chi phí phát sinh
        double totalCost = totalPaid + totalPending;

        // Cập nhật giá trị lên các nhãn Panel Summary
        lblTotalCostVal.setText(moneyFormat.format(totalCost));
        lblTotalPaidVal.setText(moneyFormat.format(totalPaid));
        lblTotalPendingVal.setText(moneyFormat.format(totalPending));

        // Xử lý giao diện trường hợp danh sách trống
        if (!hasData) {
            JLabel lblEmpty = new JLabel("Bạn chưa có lịch sử thanh toán nào.");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            lblEmpty.setForeground(COLOR_TEXT_MUTED);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblEmpty.setBorder(new EmptyBorder(30, 0, 0, 0));
            listPanel.add(lblEmpty);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ================= UI BUILDER: CỘT TỔNG HỢP =================
    private JPanel createSummaryColumn(String title, String value, Color valueColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(COLOR_TEXT_MUTED);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValue.setForeground(valueColor);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblValue);
        
        return panel;
    }

    // ================= UI BUILDER: THẺ GIAO DỊCH (TRANSACTION CARD) =================
    private JPanel createTransactionCard(String date, String methodCode, String methodText, String stageName, double amount) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95)); // Tăng nhẹ chiều cao để hiển thị chữ đẹp hơn

        // --- Hàng 1: Ngày tháng khám/thanh toán ---
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDate.setForeground(COLOR_TEXT_MUTED);
        card.add(lblDate, BorderLayout.NORTH);

        // --- Hàng 2: Khối hiển thị chi tiết ---
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setBorder(new EmptyBorder(6, 0, 0, 0));

        // Khối bên trái: Chứa Icon hình tròn phương thức + Cụm chữ thông tin
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);

        // Xử lý đồ họa cho Icon tròn (CK: Chuyển khoản / TM: Tiền mặt)
        String iconText = "CARD".equalsIgnoreCase(methodCode) ? "CK" : "TM";
        Color iconBgColor = "CARD".equalsIgnoreCase(methodCode) ? new Color(93, 173, 226) : new Color(243, 156, 18);
        
        JLabel lblIcon = new JLabel(iconText, SwingConstants.CENTER);
        lblIcon.setOpaque(true);
        lblIcon.setBackground(iconBgColor);
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblIcon.setPreferredSize(new Dimension(38, 38));
        lblIcon.setBorder(new LineBorder(iconBgColor, 1, true));

        // Cụm văn bản hiển thị thông tin chi tiết dịch vụ
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel lblMethod = new JLabel(methodText);
        lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMethod.setForeground(COLOR_TEXT_DARK);
        
        // Nhãn hiển thị chính xác tên giai đoạn điều trị (Ví dụ: nhổ răng, cấy implant,...)
        JLabel lblStage = new JLabel(stageName);
        lblStage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStage.setForeground(COLOR_TEXT_MUTED);

        textPanel.add(lblMethod);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(lblStage);

        leftPanel.add(lblIcon);
        leftPanel.add(textPanel);

        // Khối bên phải: Giá trị số tiền thanh toán
        JLabel lblAmount = new JLabel(moneyFormat.format(amount));
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAmount.setForeground(COLOR_TEXT_DARK);

        bodyPanel.add(leftPanel, BorderLayout.WEST);
        bodyPanel.add(lblAmount, BorderLayout.EAST);

        card.add(bodyPanel, BorderLayout.CENTER);

        return card;
    }
}