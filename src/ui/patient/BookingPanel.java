package ui.patient;

import config.DBConnection;
import controller.user.BookingController;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import model.Booking;
import model.Doctor;
import model.Service;
import ui.auth.MainDashboard;

public class BookingPanel extends JPanel {

    // ================= BẢNG MÀU PASTEL BLUE =================
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color TEXT_MUTED = new Color(149, 165, 166);
    private final Color PRIMARY_PASTEL = new Color(133, 193, 233);
    private final Color SELECTED_BG = new Color(228, 241, 250); 
    private final Color SELECTED_BORDER = new Color(93, 173, 226);

    // Màu trạng thái khung giờ
    private final Color COLOR_VANG = new Color(52, 152, 219);    // Xanh dương
    private final Color COLOR_BINHTHUONG = new Color(46, 204, 113); // Xanh lá
    private final Color COLOR_DONG = new Color(230, 126, 34);    // Cam

    // Biến lưu trữ dữ liệu người dùng chọn
    private int loggedInUserId;
    private JComboBox<ComboItem> cbBranch, cbService, cbDate;
    private JTextField txtNote;
    
    private JPanel doctorContainer, timeSlotContainer;
    private int selectedDoctorId = -1;
    private String selectedTimeSlot = "";
    private BookingController controller;

    // Danh sách khung giờ mẫu
    private final String[] timeSlots = {"08:30", "09:30", "10:30", "13:30", "14:30", "15:30"};

    public BookingPanel(int userId) {
        controller = new BookingController();
        this.loggedInUserId = userId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("Đặt lịch hẹn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(20, 20, 15, 20));
        add(lblTitle, BorderLayout.NORTH);

        // --- Khu vực Form chính (Scrollable) ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        // 1. Chi nhánh
        formPanel.add(createSectionLabel("Chi nhánh"));
        cbBranch = new JComboBox<>();
        cbBranch.addItem(new ComboItem(1, "Giáp Nhất, Thanh Xuân"));
        cbBranch.addItem(new ComboItem(2, "Trần Thái Tông, Cầu Giấy"));
        cbBranch.addItem(new ComboItem(3, "Đường 3/2, Sài Gòn"));
        styleComboBox(cbBranch);
        formPanel.add(cbBranch);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. Dịch vụ (Load từ DB)
        formPanel.add(createSectionLabel("Dịch vụ"));
        cbService = new JComboBox<>();
        styleComboBox(cbService);
        loadServices();
        formPanel.add(cbService);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. Bác sĩ (Load từ DB dưới dạng Card)
        formPanel.add(createSectionLabel("Bác sĩ"));
        doctorContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        doctorContainer.setBackground(Color.WHITE);
        doctorContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadDoctors();
        formPanel.add(doctorContainer);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 4. Ngày hẹn (14 ngày tới)
        formPanel.add(createSectionLabel("Ngày hẹn"));
        cbDate = new JComboBox<>();
        styleComboBox(cbDate);
        loadDates();
        cbDate.addActionListener(e -> updateTimeSlots()); // Khi đổi ngày thì tải lại khung giờ
        formPanel.add(cbDate);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 5. Khung giờ
        formPanel.add(createSectionLabel("Khung giờ"));
        timeSlotContainer = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 cột
        timeSlotContainer.setBackground(Color.WHITE);
        timeSlotContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        timeSlotContainer.setMaximumSize(new Dimension(600, 200));
        updateTimeSlots(); // Khởi tạo ban đầu
        formPanel.add(timeSlotContainer);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 6. Nội dung
        JLabel lblNote = new JLabel("Nội dung");
        lblNote.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNote.setForeground(TEXT_DARK);
        formPanel.add(lblNote);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtNote = new JTextField();
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNote.setPreferredSize(new Dimension(400, 40));
        txtNote.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtNote);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 7. Nút Tiếp tục
        JButton btnSubmit = new JButton("Tiếp tục");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSubmit.setBackground(PRIMARY_PASTEL);
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSubmit.addActionListener(e -> submitBooking());
        formPanel.add(btnSubmit);

        // Bọc vào ScrollPane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= HÀM XỬ LÝ LƯU ĐẶT LỊCH (DATABASE) ================= 
    private void submitBooking() {

        // Kiểm tra bác sĩ
        if (selectedDoctorId == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn bác sĩ"
            );
            return;
        }

        // Kiểm tra dịch vụ
        if (cbService.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn dịch vụ"
            );
            return;
        }

        // Kiểm tra ngày hẹn
        if (cbDate.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn ngày hẹn"
            );
            return;
        }

        // Kiểm tra khung giờ
        if (selectedTimeSlot == null || selectedTimeSlot.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn khung giờ"
            );
            return;
        }

        // Kiểm tra nội dung
        if (txtNote.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập nội dung khám"
            );
            return;
        }

        try {

            ComboItem srv =
                    (ComboItem) cbService.getSelectedItem();

            ComboItem dt =
                    (ComboItem) cbDate.getSelectedItem();

            String dateStr =
                    dt.getValue();

            String dateTimeStr =
                    dateStr + " " +
                    selectedTimeSlot + ":00";

            Booking booking =
                    new Booking(
                            loggedInUserId,
                            selectedDoctorId,
                            srv.getId(),
                            Timestamp.valueOf(dateTimeStr),
                            txtNote.getText().trim()
                    );

            boolean success =
                    controller.submitBooking(booking);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Đặt lịch thành công"
                );

                Window window =
                        SwingUtilities.getWindowAncestor(this);

                if (window instanceof MainDashboard) {

                    ((MainDashboard) window)
                            .refreshAppointments();
                }

                // Reset form
                selectedDoctorId = -1;
                selectedTimeSlot = "";
                txtNote.setText("");

                loadDoctors();
                updateTimeSlots();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Đặt lịch thất bại"
                );
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Có lỗi xảy ra khi đặt lịch"
            );
        }
    }

    // ================= HÀM LOAD DỊCH VỤ =================
    private void loadServices() {

        cbService.removeAllItems();

        for (Service s : controller.getServices()) {

            cbService.addItem(
                    new ComboItem(
                            s.getId(),
                            s.getName()
                    )
            );
        }
    }

    // ================= HÀM LOAD BÁC SĨ (DẠNG CARD) =================
    private void loadDoctors() {

        doctorContainer.removeAll();

        for (Doctor d : controller.getDoctors()) {

            doctorContainer.add(
                    createDoctorCard(
                            d.getId(),
                            d.getFullName()
                    )
            );
        }

        doctorContainer.revalidate();
        doctorContainer.repaint();
    }

    // ================= HÀM LOAD NGÀY (14 NGÀY TỚI) =================
    private void loadDates() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy"); // Viết hoa EEEE để ra Thứ
        
        for (int i = 0; i < 14; i++) {
            LocalDate date = today.plusDays(i);
            String display = date.format(displayFormatter);
            String value = date.toString(); // "yyyy-MM-dd" để dễ parse lưu DB
            cbDate.addItem(new ComboItem(i, display, value));
        }
    }

    // ================= HÀM TÍNH TOÁN & CẬP NHẬT KHUNG GIỜ =================
    private void updateTimeSlots() {
        timeSlotContainer.removeAll();
        selectedTimeSlot = ""; // Reset chọn

        if (cbDate.getSelectedItem() == null) return;
        ComboItem selectedDateItem = (ComboItem) cbDate.getSelectedItem();
        String dateVal = selectedDateItem.getValue(); // "yyyy-MM-dd"

        // Lấy số lượng lịch hẹn cho từng khung giờ từ DB
        for (String time : timeSlots) {
            int count = getAppointmentCountForSlot(dateVal, time);
            
            // Logic màu sắc: 0-1 (Vắng), 2-3 (Bình thường), >=4 (Đông)
            String status = "Vắng";
            Color statusColor = COLOR_VANG;
            
            if (count >= 2 && count <= 3) {
                status = "Bình thường";
                statusColor = COLOR_BINHTHUONG;
            } else if (count >= 4) {
                status = "Đông";
                statusColor = COLOR_DONG;
            }

            timeSlotContainer.add(createTimeSlotCard(time, status, statusColor));
        }

        timeSlotContainer.revalidate();
        timeSlotContainer.repaint();
    }

    // Hàm đếm số lịch hẹn đã đặt trong 1 khung giờ
    private int getAppointmentCountForSlot(String dateStr, String timeStr) {
        String exactDateTime = dateStr + " " + timeStr + ":00";
        String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(exactDateTime));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            // Mock data ngẫu nhiên nếu không có DB để test giao diện (trả về từ 0 đến 5)
            return (int)(Math.random() * 6);
        }
        return 0;
    }

    // ================= UI COMPONENTS BUILDERS =================
    
    // Nhãn có dấu * đỏ
    private JLabel createSectionLabel(String text) {
        JLabel lbl = new JLabel("<html>" + text + " <font color='red'>*</font></html>");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        return lbl;
    }

    private void styleComboBox(JComboBox cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // Thẻ Bác Sĩ (Clickable)
    // ================= HÀM TẠO THẺ BÁC SĨ (Đã xóa Emoji gây lỗi) =================
    private JPanel createDoctorCard(int docId, String name) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // THAY ĐỔI: Dùng chữ "BS." thay cho Emoji 👤 để không bị lỗi ô vuông
        JLabel lblAvatar = new JLabel("BS.", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAvatar.setForeground(PRIMARY_PASTEL); // Chữ BS. màu xanh biển pastel

        // Xử lý tên: Thay thế khoảng trắng bằng thẻ <br> để tên tự động xuống dòng trong thẻ
        String displayName = name != null ? name : "Ẩn danh";
        JLabel lblName = new JLabel("<html><center>" + displayName.replace(" ", "<br>") + "</center></html>", SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblName.setForeground(TEXT_DARK);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblAvatar);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblName);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedDoctorId = docId; 
                // Reset viền tất cả các thẻ
                for (Component c : doctorContainer.getComponents()) {
                    if (c instanceof JPanel) {
                        ((JPanel)c).setBorder(BorderFactory.createCompoundBorder(
                                new LineBorder(new Color(230, 230, 230), 1, true),
                                new EmptyBorder(10, 15, 10, 15)));
                        ((JPanel)c).setBackground(Color.WHITE);
                    }
                }
                // Tô viền thẻ được chọn
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(SELECTED_BORDER, 2, true),
                        new EmptyBorder(9, 14, 9, 14)));
                card.setBackground(SELECTED_BG);
                
                // Cập nhật lại khung giờ khi đổi bác sĩ
                updateTimeSlots();
            }
        });
        return card;
    }

    // Thẻ Khung Giờ (Clickable)
    private JPanel createTimeSlotCard(String time, String status, Color statusColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTime = new JLabel(time + " - " + calculateEndTime(time), SwingConstants.CENTER);
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTime.setForeground(TEXT_DARK);
        lblTime.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblStatus = new JLabel(status, SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(statusColor);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTime);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblStatus);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedTimeSlot = time;
                for (Component c : timeSlotContainer.getComponents()) {
                    ((JPanel)c).setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(new Color(230, 230, 230), 1, true),
                            new EmptyBorder(10, 10, 10, 10)));
                    ((JPanel)c).setBackground(Color.WHITE);
                }
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(SELECTED_BORDER, 2, true),
                        new EmptyBorder(9, 9, 9, 9)));
                card.setBackground(SELECTED_BG);
            }
        });
        return card;
    }

    private String calculateEndTime(String startTime) {
        String[] parts = startTime.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        m += 30;
        if (m >= 60) { h += 1; m -= 60; }
        return String.format("%02d:%02d", h, m);
    }

    // Class phụ trợ chứa dữ liệu cho ComboBox
    class ComboItem {
        private int id;
        private String display;
        private String value;

        public ComboItem(int id, String display) { this.id = id; this.display = display; }
        public ComboItem(int id, String display, String value) { this.id = id; this.display = display; this.value = value; }

        public int getId() { return id; }
        public String getValue() { return value; }
        @Override
        public String toString() { return display; }
    }
}