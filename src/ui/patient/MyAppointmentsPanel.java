package ui.patient;

import config.*;
import model.user.Appointment;
import ui.auth.MainDashboard;
import service.AppointmentService;

import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

public class MyAppointmentsPanel extends JPanel {

    // ================= BẢNG MÀU PASTEL BLUE =================
    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color TEXT_MUTED = new Color(149, 165, 166);
    private final Color PRIMARY_PASTEL = new Color(133, 193, 233);
    
    // Các màu trạng thái
    private final Color STATUS_BLUE = new Color(52, 152, 219);     // Màu xanh dương cho "Đã duyệt/Chờ duyệt"
    private final Color STATUS_GRAY = new Color(189, 195, 199);    // Màu xám cho "Kết thúc/Hủy"
    private final Color COLOR_SUCCESS = new Color(39, 174, 96);    // Màu xanh lá cây cho "Đã hoàn thành"

    private int loggedInUserId;
    private JPanel listPanel;
    private AppointmentService appointmentService;

    public MyAppointmentsPanel(int userId) {
        appointmentService = new AppointmentService();
        this.loggedInUserId = userId;
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("Lịch hẹn của tôi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(20, 25, 15, 25));
        add(lblTitle, BorderLayout.NORTH);

        // --- Danh sách lịch (Scrollable) ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(COLOR_BG);
        listPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Tải dữ liệu từ DB
        loadData();
    }

    // ================= TRUY VẤN DATABASE THẬT =================
    public void loadData() {

        listPanel.removeAll();

        List<Appointment> appointments =
                appointmentService.getAppointmentsByUserId(loggedInUserId);

        if (appointments.isEmpty()) {

            JLabel lblEmpty = new JLabel("Bạn chưa có lịch hẹn nào.");

            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            lblEmpty.setForeground(TEXT_MUTED);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

            listPanel.add(lblEmpty);

        } else {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");           

            for (Appointment app : appointments) {

                String timeStr = "Chưa xếp lịch";
                if (app.getAppointmentDate() != null) {
                    timeStr = app.getAppointmentDate()
                                    .toLocalDateTime()
                                    .format(formatter);
                }

                listPanel.add(createAppointmentCard(
                        app.getId(),
                        timeStr,
                        app.getStatus(),
                        app.getServiceName(),
                        app.getDoctorName(),
                        app.getNote()
                ));

                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ================= TẠO THẺ LỊCH HẸN =================
    private JPanel createAppointmentCard(int id, String time, String status, String service, String doctor, String note) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // 1. Thời gian
        JLabel lblTime = new JLabel(time);
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTime.setForeground(TEXT_DARK);
        
        // 2. Trạng thái (Badge) - HIỂN THỊ CHỮ DỊCH
        JLabel lblBadge = new JLabel(" " + translateStatus(status) + " ");
        lblBadge.setOpaque(true);
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBadge.setForeground(Color.WHITE);
        
        // PHÂN LOẠI MÀU SẮC DỰA TRÊN TRẠNG THÁI GỐC (status)
        if ("Completed".equalsIgnoreCase(status)) {
            lblBadge.setBackground(COLOR_SUCCESS); // Xanh lá
        } else if ("Approved".equalsIgnoreCase(status) ) {
            lblBadge.setBackground(STATUS_BLUE); // Xanh dương
        } else if ("Pending".equalsIgnoreCase(status) ){
            lblBadge.setBackground(new Color(243, 156, 18));
        } else {
            lblBadge.setBackground(STATUS_GRAY); // Xám (Hủy/Kết thúc)
        }
            
        

        // 3. Các dòng thông tin 
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(createRow("Chi nhánh:", "Cơ sở chính")); 
        infoPanel.add(createRow("Bác sĩ:", doctor != null ? doctor : "Đang cập nhật"));
        infoPanel.add(createRow("Dịch vụ:", service != null ? service : "Khám tổng quát"));
        infoPanel.add(createRow("Nội dung:", note != null ? note : "Không có ghi chú"));

        // Gộp vào Card
        // Gom Time và Badge vào 1 panel chung để có giao diện đẹp hơn, badge nằm ngay dưới time
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblTime.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        topPanel.add(lblTime);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(lblBadge);

        card.add(topPanel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(infoPanel);

        // Sự kiện Click: Nhảy sang trang chi tiết
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPageToDetail(id);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 250, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private JPanel createRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        JLabel lblL = new JLabel(label + " ");
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblL.setForeground(TEXT_MUTED);
        JLabel lblV = new JLabel(value);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblV.setForeground(TEXT_DARK);
        row.add(lblL);
        row.add(lblV);
        return row;
    }

    // ĐÃ THÊM: Dịch chuỗi Completed -> Đã hoàn thành
    private String translateStatus(String st) {
        if (st == null) return "Không rõ";
        if (st.equalsIgnoreCase("Completed")) return "Đã hoàn thành";
        if (st.equalsIgnoreCase("Pending")) return "Chờ duyệt";
        if (st.equalsIgnoreCase("Approved")) return "Đã duyệt";
        if (st.equalsIgnoreCase("Done")) return "Kết thúc";
        if (st.equalsIgnoreCase("Cancelled")) return "Đã hủy";
        return st;
    }

    private void switchPageToDetail(int appointmentId) {
        Window window = SwingUtilities.getWindowAncestor(this);
        new AppointmentDetailDialog(window, appointmentId, () -> loadData()).setVisible(true);
    }
}