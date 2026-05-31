package ui.doctor.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import ui.doctor.Controller.AppointmentController;
import ui.doctor.Model.DentalAppointmentModel;

public class AppointmentView extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private AppointmentController controller;
    private int doctorId;

    private JButton btnAddAppointment;

    // Màu sắc giao diện
    private final Color BG_COLOR = new Color(248, 250, 252);
    private final Color TITLE_COLOR = new Color(44, 62, 80);
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);

    public AppointmentView(int doctorId) {
        this.doctorId = doctorId;
        controller = new AppointmentController();

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // ================= HEADER =================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_COLOR);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ LỊCH KHÁM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(TITLE_COLOR);

        JLabel lblSubTitle = new JLabel("Theo dõi, phê duyệt và hủy lịch hẹn trực tiếp từ bệnh nhân");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTitle.setForeground(new Color(120, 120, 120));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblSubTitle);

        topPanel.add(titlePanel, BorderLayout.WEST);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        btnAddAppointment = createButton("Thêm lịch mới", PRIMARY_COLOR);
        btnAddAppointment.addActionListener(e -> {
            new AppointmentDialog(this, null, doctorId);
            loadAppointments();
        });
        buttonPanel.add(btnAddAppointment);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // ================= TABLE =================
        String[] columns = {"ID", "Họ tên bệnh nhân", "Ngày khám", "Giờ khám", "Vấn đề khám", "Trạng thái", "Giai đoạn điều trị"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(214, 234, 248));
        table.setGridColor(new Color(230, 230, 230));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ================= EVENTS =================
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editAppointment();
                }
            }
        });

        loadAppointments();
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    public void loadAppointments() {
        model.setRowCount(0);
        List<DentalAppointmentModel> list = controller.getAppointmentsByDoctor(doctorId);
        for (DentalAppointmentModel ap : list) {
            String displayStatus = switch (ap.getStatus().toLowerCase()) {
                case "pending" -> "Chờ khám";
                case "approved" -> "Đã duyệt";
                case "reject" -> "Đã hủy";
                case "completed" -> "Đã hoàn thành";
                default -> ap.getStatus();
            };

            model.addRow(new Object[]{
                ap.getId(), ap.getPatientName(), ap.getAppointmentDate(), 
                ap.getAppointmentTime(), ap.getProblem(), displayStatus, ap.getStageName()
            });
        }
    }

    private void editAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String status = model.getValueAt(row, 5).toString();
        // Sửa ở đây: Chỉ chặn nếu là "Đã hủy" hoặc "Đã hoàn thành", cho phép mở "Đã duyệt"
        if (status.equalsIgnoreCase("Đã hủy") || status.equalsIgnoreCase("Đã hoàn thành")) {
            JOptionPane.showMessageDialog(this, "Lịch hẹn đã kết thúc, không thể chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DentalAppointmentModel ap = new DentalAppointmentModel();
        ap.setId((int) model.getValueAt(row, 0));
        ap.setPatientName(model.getValueAt(row, 1).toString());
        ap.setAppointmentDate(model.getValueAt(row, 2).toString());
        ap.setAppointmentTime(model.getValueAt(row, 3).toString());
        ap.setProblem(model.getValueAt(row, 4).toString());
        // Truyền đúng trạng thái gốc vào để Dialog xử lý
        ap.setStatus(status.equals("Đã duyệt") ? "approved" : "pending");
        ap.setStageName(model.getValueAt(row, 6).toString());
        
        new AppointmentDialog(this, ap, this.doctorId);
        loadAppointments();
    }
}