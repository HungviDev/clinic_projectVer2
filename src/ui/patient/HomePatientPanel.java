package ui.patient;

import ui.auth.MainDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import config.DBConnection;
import model.user.Appointment;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import service.AppointmentService;

import java.util.List;

public class HomePatientPanel extends JPanel {

    // ================= BẢNG MÀU PASTEL BLUE ĐỒNG BỘ =================
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color COLOR_GREEN_ICON = new Color(139, 195, 74); // Xanh lá pastel
    private final Color COLOR_BLUE_BADGE = new Color(33, 150, 243); // Xanh dương
    private final Color COLOR_TEXT_DARK = new Color(44, 62, 80);
    private final Color COLOR_TEXT_MUTED = new Color(149, 165, 166);
    private final Color COLOR_DANGER = new Color(231, 76, 60); // Đỏ pastel cho nút Hủy
    
    // Thêm màu sắc cho trạng thái trong Popup
    private final Color COLOR_SUCCESS = new Color(39, 174, 96);    // Xanh lá cho Completed
    private final Color COLOR_WARNING = new Color(243, 156, 18);   // Cam cho Pending
    private final Color COLOR_CANCELLED = new Color(149, 165, 166); // Xám cho Cancelled

    private YearMonth currentYearMonth;
    private JPanel calendarGrid;
    private JLabel lblMonthYear;
    private JLabel lblStatus; 
    
    private int loggedInUserId;
    private Set<LocalDate> bookedDates;
    private AppointmentService appointmentService = new AppointmentService();

    public HomePatientPanel(int userId, String patientName) {
        this.loggedInUserId = userId;
        setLayout(new BorderLayout(10, 10)); 
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        currentYearMonth = YearMonth.now();
        bookedDates = new HashSet<>();
        
        // Tải lịch hẹn từ Database lần đầu
        loadAppointmentsFromDatabase();

        // ================= PHẦN TRÊN: HEADER & QUICK ACTIONS =================
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(COLOR_BG);

        // 1. Lời chào & Trạng thái
        JLabel lblWelcome = new JLabel("Xin chào " + patientName);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(COLOR_TEXT_DARK);
        
        lblStatus = new JLabel();
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        updateTodayStatus(); 
        
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
        
        // Thay đường dẫn ảnh /resources/ của bạn vào đây nếu cần
        gridActions.add(createActionButton("Đặt Lịch", "/resources/dat_lich.png", () -> switchPage("BOOKING")));
        gridActions.add(createActionButton("Lịch hẹn của tôi", "/resources/lich_hen.png", () -> switchPage("SCHEDULE"))); 
        gridActions.add(createActionButton("Liên hệ", "/resources/phone.png", () -> switchPage("CONTACT")));
        gridActions.add(createActionButton("Lịch sử điều trị", "/resources/lock.png", () -> switchPage("MEDICAL_RECORD")));
        gridActions.add(createActionButton("Cá nhân", "/resources/ca_nhan.png", () -> switchPage("PROFILE")));
        gridActions.add(createActionButton("Danh mục dịch vụ", "/resources/dich_vu.png", () -> switchPage("SERVICE")));

        JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setBackground(COLOR_BG);
        gridWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); 
        gridWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); 
        gridWrapper.add(gridActions);

        topPanel.add(gridWrapper); 
        add(topPanel, BorderLayout.NORTH);

        // ================= PHẦN DƯỚI: LỊCH (CALENDAR) =================
        JPanel calendarContainer = new JPanel(new BorderLayout());
        calendarContainer.setBackground(Color.WHITE);
        calendarContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(10, 10, 10, 10) 
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

        renderCalendar();
        add(calendarContainer, BorderLayout.CENTER);

        // ================= AUTO REFRESH KHI MỞ TRANG =================
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                // Tải lại DB và vẽ lại lịch khi người dùng vừa chuyển từ màn Đặt Lịch về màn Home
                loadAppointmentsFromDatabase();
                updateTodayStatus();
                renderCalendar();
            }
        });
    }

    // ================= LOAD DỮ LIỆU TỪ DATABASE =================
    private void loadAppointmentsFromDatabase() {
        bookedDates.clear();

        bookedDates.addAll(
                appointmentService.getBookedDates(
                        loggedInUserId
                )
        );
    }

    // ================= RENDER LỊCH VÀ XỬ LÝ CLICK =================
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

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            JPanel cellPanel = new JPanel(new BorderLayout());
            cellPanel.setBackground(Color.WHITE);
            cellPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel lblDate = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            lblDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDate.setForeground(COLOR_TEXT_DARK);

            boolean hasAppointment = bookedDates.contains(date);

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

            // Xử lý sự kiện click vào ngày
            cellPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (hasAppointment) {
                        showDailyAppointmentsDialog(date); // Hiện popup Danh sách lịch hẹn trong ngày
                    } else {
                        int choice = JOptionPane.showConfirmDialog(HomePatientPanel.this, 
                            "Ngày " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " chưa có lịch. Bạn có muốn đặt lịch không?", 
                            "Đặt lịch hẹn", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            switchPage("BOOKING");
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!date.equals(today)) cellPanel.setBorder(BorderFactory.createLineBorder(COLOR_BLUE_BADGE, 1));
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

    // ================= POPUP 1: DANH SÁCH LỊCH TRONG NGÀY (THẬT 100%) =================
    private void showDailyAppointmentsDialog(LocalDate date) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Lịch hẹn ngày " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(COLOR_BG);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(COLOR_BG);
        listPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblHeader = new JLabel("Ngày " + date.getDayOfMonth() + " Tháng " + date.getMonthValue() + " Năm " + date.getYear());
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        listPanel.add(lblHeader);

        boolean hasData = false;
        List<Appointment> appointments =
                appointmentService.getAppointmentsByDate(
                        loggedInUserId,
                        date
                );

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("HH:mm");

        for (Appointment app : appointments) {

            hasData = true;

            String serviceName = app.getServiceName();
            if (serviceName == null) {
                serviceName = "Khám tổng quát";
            }

            String status = app.getStatus();

            String timeStr = "Không rõ";
            if(app.getAppointmentDate() != null) {
                timeStr = app.getAppointmentDate()
                            .toLocalDateTime()
                            .format(timeFormatter);
            }

            JPanel card =
                    createDailyItemCard(
                            serviceName,
                            timeStr,
                            status
                    );

            int appId = app.getId();

            card.addMouseListener(
                    new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            dialog.dispose();

                            showAppointmentDetailDialog(appId);
                        }
                    }
            );

            listPanel.add(card);

            listPanel.add(
                    Box.createRigidArea(
                            new Dimension(0, 10)
                    )
            );
        }

        if (!hasData) {
            JLabel lblEmpty = new JLabel("Không có lịch hẹn nào.");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblEmpty.setForeground(COLOR_TEXT_MUTED);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(lblEmpty);
        }

        dialog.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ================= TẠO THẺ ITEM TRONG POPUP NGÀY (CẬP NHẬT TRẠNG THÁI) =================
    private JPanel createDailyItemCard(String serviceName, String time, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(400, 80));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setBackground(Color.WHITE);
        JLabel lblService = new JLabel(serviceName);
        lblService.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblTime = new JLabel(time);
        lblTime.setForeground(COLOR_TEXT_MUTED);
        left.add(lblService);
        left.add(lblTime);

        // LOGIC CHUYỂN ĐỔI NGÔN NGỮ VÀ MÀU SẮC
        String statusText = "Không rõ";
        Color statusColor = COLOR_TEXT_MUTED;

        if (status != null) {
            if (status.equalsIgnoreCase("Pending")) {
                statusText = "Chờ duyệt";
                statusColor = COLOR_WARNING; // Màu cam
            } else if (status.equalsIgnoreCase("Approved")) {
                statusText = "Đã duyệt";
                statusColor = COLOR_BLUE_BADGE; // Màu xanh dương
            } else if (status.equalsIgnoreCase("Completed")) {
                statusText = "Đã hoàn thành";
                statusColor = COLOR_SUCCESS; // Màu xanh lá cây
            } else if (status.equalsIgnoreCase("Cancelled")) {
                statusText = "Đã hủy";
                statusColor = COLOR_CANCELLED; // Màu xám
            }
        }

        JLabel lblStatus = new JLabel(statusText);
        lblStatus.setForeground(statusColor);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(left, BorderLayout.WEST);
        card.add(lblStatus, BorderLayout.EAST);
        return card;
    }

    // ================= POPUP 2: CHI TIẾT LỊCH HẸN (DÙNG CLASS CHUNG) =================
    private void showAppointmentDetailDialog(int appointmentId) {
        Window window = SwingUtilities.getWindowAncestor(this);
        
        // Gọi cửa sổ Popup dùng chung đã tạo ở file AppointmentDetailDialog.java
        new AppointmentDetailDialog(window, appointmentId, () -> {
            // Hành động này sẽ tự động chạy nếu bệnh nhân nhấn "Hủy lịch" thành công
            loadAppointmentsFromDatabase();
            updateTodayStatus();
            renderCalendar();
        }).setVisible(true);
    }
    

    // ================= HÀM TẠO DÒNG DỮ LIỆU SẠCH =================
    private JPanel createDataRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(5, 0, 10, 0));
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLabel.setForeground(COLOR_TEXT_MUTED); 

        JLabel lblVal = new JLabel("<html><div style='text-align: right;'>" + (value != null ? value : "") + "</div></html>", SwingConstants.RIGHT);
        lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblVal.setForeground(COLOR_TEXT_DARK);

        row.add(lblLabel, BorderLayout.WEST);
        row.add(lblVal, BorderLayout.CENTER);
        return row;
    }

    // ================= HÀM KIỂM TRA TRẠNG THÁI HÔM NAY =================
    private void updateTodayStatus() {
        LocalDate today = LocalDate.now();
        if (appointmentService.hasAppointmentToday(
                loggedInUserId
        )) {
            lblStatus.setText("Hôm nay bạn CÓ lịch hẹn!");
            lblStatus.setForeground(COLOR_BLUE_BADGE); 
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        } else {
            lblStatus.setText("Hôm nay bạn không có lịch hẹn");
            lblStatus.setForeground(COLOR_TEXT_MUTED);
            lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
    }

    // ================= TẠO NÚT ACTION (Load ảnh an toàn) =================
    private JPanel createActionButton(String title, String iconPath, Runnable onClick) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 240, 240), 1, true),
                new EmptyBorder(10, 5, 10, 5) 
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon finalIcon = loadScaledImage(iconPath, 32, 32);
        
        if (finalIcon != null) {
            lblIcon.setIcon(finalIcon);
        } else {
            lblIcon.setText("X");
            lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblIcon.setForeground(Color.LIGHT_GRAY);
        }

        JLabel lblTitle = new JLabel("<html><center>" + title + "</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12)); 
        lblTitle.setForeground(COLOR_TEXT_DARK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblIcon);
        panel.add(Box.createRigidArea(new Dimension(0, 5))); 
        panel.add(lblTitle);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
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

    // ================= HÀM TRỢ GIÚP =================
    private ImageIcon loadScaledImage(String iconPath, int width, int height) {
        try {
            URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImg = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); 
                return new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            System.err.println("Lỗi load ảnh tại: " + iconPath);
        }
        return null; 
    }

    private void switchPage(String pageName) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof MainDashboard) {
            ((MainDashboard) window).showPage(pageName);
        }
    }

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
}