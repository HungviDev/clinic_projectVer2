package ui.doctor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class HomeDoctorPanel extends JPanel {

    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color COLOR_GREEN_ICON = new Color(139, 195, 74); // Xanh lá pastel
    private final Color COLOR_BLUE_BADGE = new Color(33, 150, 243); // Xanh dương
    private final Color COLOR_TEXT_DARK = new Color(44, 62, 80);
    private final Color COLOR_TEXT_MUTED = new Color(149, 165, 166);

    private YearMonth currentYearMonth;
    private JPanel calendarGrid;
    private JLabel lblMonthYear;
    private JLabel lblStatus; // Label trạng thái hôm nay
    
    // Danh sách các ngày có lịch hẹn
    private Set<LocalDate> bookedDates;

    public HomeDoctorPanel(String patientName) {
        setLayout(new BorderLayout(10, 10)); // Giảm khoảng cách tổng thể
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // Khởi tạo dữ liệu lịch
        currentYearMonth = YearMonth.now();
        bookedDates = new HashSet<>();
        
        // Giả lập: Thêm lịch hẹn vào ngày hôm nay, ngày 20 và 25 của tháng hiện tại
        bookedDates.add(LocalDate.now()); // Thêm ngày hôm nay để test
        bookedDates.add(currentYearMonth.atDay(20));
        bookedDates.add(currentYearMonth.atDay(25));

        // ================= PHẦN TRÊN: HEADER & QUICK ACTIONS =================
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(COLOR_BG);

        // 1. Lời chào & Trạng thái
        JLabel lblWelcome = new JLabel("Xin chào con chos " + patientName);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(COLOR_TEXT_DARK);
        
        lblStatus = new JLabel();
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        updateTodayStatus(); // Gọi hàm cập nhật trạng thái hôm nay
        
        JPanel headerTextPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerTextPanel.setBackground(COLOR_BG);
        headerTextPanel.add(lblWelcome);
        headerTextPanel.add(lblStatus);
        headerTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topPanel.add(headerTextPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. Lưới các nút chức năng
        JPanel gridActions = new JPanel(new GridLayout(2, 3, 15, 15)); 
        gridActions.setBackground(COLOR_BG);
        gridActions.setPreferredSize(new Dimension(750, 180)); 
        gridActions.add(createActionButton("Đặt Lịch", "📅"));
        gridActions.add(createActionButton("Lịch hẹn của tôi", "🕒"));
        gridActions.add(createActionButton("Quá trình điều trị", "🌍"));
        gridActions.add(createActionButton("Ảnh điều trị", "🖼️"));
        gridActions.add(createActionButton("Sản phẩm", "🧴"));
        gridActions.add(createActionButton("Danh mục dịch vụ", "📋"));

        // ===== ĐIỂM MỚI: Bọc gridActions vào một Wrapper Panel để ép ra giữa =====
        JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setBackground(COLOR_BG);
        gridWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Ép wrapper chiếm hết chiều ngang
        gridWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); // Giữ luồng BoxLayout không bị giật cục
        gridWrapper.add(gridActions);

        topPanel.add(gridWrapper); // Thêm Wrapper vào thay vì thêm trực tiếp gridActions
        
        add(topPanel, BorderLayout.NORTH);

        // ================= PHẦN DƯỚI: LỊCH (CALENDAR) =================
        JPanel calendarContainer = new JPanel(new BorderLayout());
        calendarContainer.setBackground(Color.WHITE);
        calendarContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(10, 10, 10, 10) // Giảm padding của lịch
        ));

        // Calendar Header (< Tháng/Năm >)
        JPanel calendarHeader = new JPanel(new BorderLayout());
        calendarHeader.setBackground(Color.WHITE);
        
        JButton btnPrev = new JButton("<");
        styleNavButton(btnPrev);
        btnPrev.addActionListener(e -> changeMonth(-1));

        lblMonthYear = new JLabel("", SwingConstants.CENTER);
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMonthYear.setForeground(COLOR_TEXT_DARK);

        JButton btnNext = new JButton(">");
        styleNavButton(btnNext);
        btnNext.addActionListener(e -> changeMonth(1));

        calendarHeader.add(btnPrev, BorderLayout.WEST);
        calendarHeader.add(lblMonthYear, BorderLayout.CENTER);
        calendarHeader.add(btnNext, BorderLayout.EAST);
        calendarContainer.add(calendarHeader, BorderLayout.NORTH);

        // Calendar Grid (Thứ & Ngày)
        calendarGrid = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarGrid.setBackground(Color.WHITE);
        calendarGrid.setBorder(new EmptyBorder(10, 0, 0, 0));
        calendarContainer.add(calendarGrid, BorderLayout.CENTER);

        // Render Lịch lần đầu
        renderCalendar();

        add(calendarContainer, BorderLayout.CENTER);
    }

    // ================= HÀM KIỂM TRA TRẠNG THÁI HÔM NAY =================
    private void updateTodayStatus() {
        LocalDate today = LocalDate.now();
        if (bookedDates.contains(today)) {
            lblStatus.setText("Hôm nay bạn CÓ lịch hẹn!");
            lblStatus.setForeground(COLOR_BLUE_BADGE); // Đổi màu xanh nhấn mạnh
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        } else {
            lblStatus.setText("Hôm nay bạn không có lịch hẹn");
            lblStatus.setForeground(COLOR_TEXT_MUTED);
            lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
    }

    // ================= HÀM TẠO NÚT ACTION (Đã thu nhỏ) =================
    private JPanel createActionButton(String title, String emojiIcon) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 240, 240), 1, true),
                new EmptyBorder(10, 5, 10, 5) // Giảm padding trong nút
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcon = new JLabel(emojiIcon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28)); // Thu nhỏ icon từ 36 xuống 28
        lblIcon.setForeground(COLOR_GREEN_ICON);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("<html><center>" + title + "</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Thu nhỏ chữ từ 14 xuống 12
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblIcon);
        panel.add(Box.createRigidArea(new Dimension(0, 5))); // Giảm khoảng cách giữa icon và chữ
        panel.add(lblTitle);

        // Hiệu ứng hover cho nút Action
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(248, 250, 252));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });

        return panel;
    }

    // ================= CÁC HÀM XỬ LÝ LỊCH =================
    private void styleNavButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(null);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void changeMonth(int offset) {
        currentYearMonth = currentYearMonth.plusMonths(offset);
        renderCalendar();
    }

    private void renderCalendar() {
        calendarGrid.removeAll();
        lblMonthYear.setText(String.format("%02d/%d", currentYearMonth.getMonthValue(), currentYearMonth.getYear()));

        String[] daysOfWeek = {"CN", "Th 2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7"};
        for (String day : daysOfWeek) {
            JLabel lblDay = new JLabel(day, SwingConstants.CENTER);
            lblDay.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDay.setForeground(COLOR_TEXT_MUTED);
            calendarGrid.add(lblDay);
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue(); 
        int offset = (dayOfWeekValue == 7) ? 0 : dayOfWeekValue; 

        for (int i = 0; i < offset; i++) {
            calendarGrid.add(new JLabel(""));
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            JPanel cellPanel = new JPanel(new BorderLayout());
            cellPanel.setBackground(Color.WHITE);
            cellPanel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hiện con trỏ chuột khi trỏ vào ngày

            JLabel lblDate = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            lblDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDate.setForeground(COLOR_TEXT_DARK);

            boolean hasAppointment = bookedDates.contains(date);

            // Xử lý Giao diện ô ngày
            if (date.equals(today)) {
                cellPanel.setBackground(COLOR_GREEN_ICON);
                lblDate.setForeground(Color.WHITE);
            }
            if (hasAppointment) {
                JLabel lblBadge = new JLabel("Lịch", SwingConstants.CENTER);
                lblBadge.setOpaque(true);
                lblBadge.setBackground(COLOR_BLUE_BADGE);
                lblBadge.setForeground(Color.WHITE);
                lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
                if (!date.equals(today)) cellPanel.setBackground(new Color(240, 248, 255)); 
                cellPanel.add(lblBadge, BorderLayout.SOUTH);
            }

            // TƯƠNG TÁC: Xử lý sự kiện click chuột vào ngày
            cellPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (hasAppointment) {
                        JOptionPane.showMessageDialog(HomeDoctorPanel.this, 
                            "Bạn có lịch hẹn vào ngày: " + date.format(formatter) + "\nChi tiết: Khám tổng quát (Demo)", 
                            "Thông tin lịch hẹn", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        int choice = JOptionPane.showConfirmDialog(HomeDoctorPanel.this, 
                            "Ngày " + date.format(formatter) + " chưa có lịch hẹn. Bạn có muốn đặt lịch không?", 
                            "Đặt lịch hẹn", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            // Chỗ này bạn có thể gọi cardLayout để chuyển hướng sang trang "BOOKING"
                            JOptionPane.showMessageDialog(HomeDoctorPanel.this, "Chuyển đến trang Đặt Lịch...");
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Highlight ô khi di chuột vào (trừ ô hôm nay đã đổi màu nền)
                    if (!date.equals(today)) {
                        cellPanel.setBorder(BorderFactory.createLineBorder(COLOR_BLUE_BADGE, 1));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cellPanel.setBorder(null);
                }
            });

            cellPanel.add(lblDate, BorderLayout.CENTER);
            calendarGrid.add(cellPanel);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }
}