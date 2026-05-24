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

    private JButton btnApprove;
    private JButton btnCancelAppointment;
    private JButton btnRefresh;

    // Màu sắc giao diện
    private final Color BG_COLOR = new Color(248, 250, 252);
    private final Color TITLE_COLOR = new Color(44, 62, 80);
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);

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

        btnApprove = createButton("Duyệt lịch", SUCCESS_COLOR);
        btnCancelAppointment = createButton("Hủy lịch", DANGER_COLOR);
        btnRefresh = createButton("Làm mới", PRIMARY_COLOR);

        // Ban đầu chưa chọn dòng hoặc dòng không hợp lệ thì vô hiệu hóa nút
        btnApprove.setEnabled(false);
        btnCancelAppointment.setEnabled(false);

        buttonPanel.add(btnApprove);
        buttonPanel.add(btnCancelAppointment);
        buttonPanel.add(btnRefresh);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // ================= TABLE =================
        String[] columns = {
                "ID",
                "Họ tên bệnh nhân",
                "Ngày khám",
                "Giờ khám",
                "Vấn đề khám",
                "Trạng thái"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
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

        // Ẩn cột ID lưu trữ dữ liệu ngầm
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // ================= EVENTS =================

        // Lắng nghe sự kiện click chọn dòng trên bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                checkRowSelection();
            }
        });

        // Xử lý nút Duyệt lịch nhanh sang trạng thái CSDL: "approved"
        btnApprove.addActionListener(e -> handleStatusUpdate("approved", "Xác nhận DUYỆT lịch hẹn này?"));

        // Xử lý nút Hủy lịch nhanh sang trạng thái CSDL: "reject"
        btnCancelAppointment.addActionListener(e -> handleStatusUpdate("reject", "Bạn có chắc chắn muốn HỦY lịch hẹn này?"));

        // Làm mới danh sách
        btnRefresh.addActionListener(e -> loadAppointments());

        // Double click dòng để mở hộp thoại lựa chọn thông minh Duyệt/Hủy
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
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    public void loadAppointments() {
        model.setRowCount(0);
        List<DentalAppointmentModel> list = controller.getAppointmentsByDoctor(doctorId);

        for (DentalAppointmentModel ap : list) {
            // Chuyển đổi từ dữ liệu CSDL thô sang tên hiển thị Tiếng Việt hiển thị lên bảng
            String displayStatus;
            switch (ap.getStatus().toLowerCase()) {
                case "pending":
                    displayStatus = "Chờ khám";
                    break;
                case "approved":
                    displayStatus = "Đã duyệt";
                    break;
                case "reject":
                    displayStatus = "Đã hủy";
                    break;
                case "done":
                    displayStatus = "Hoàn thành";
                    break;
                default:
                    displayStatus = ap.getStatus(); // Dự phòng hiển thị gốc
                    break;
            }

            model.addRow(new Object[]{
                    ap.getId(),
                    ap.getPatientName(),
                    ap.getAppointmentDate(),
                    ap.getAppointmentTime(),
                    ap.getProblem(),
                    displayStatus
            });
        }
        btnApprove.setEnabled(false);
        btnCancelAppointment.setEnabled(false);
    }

    // Kiểm tra ràng buộc: Chỉ cho phép click tác vụ khi lịch hẹn ở trạng thái "Chờ khám" (pending)
    private void checkRowSelection() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String status = model.getValueAt(row, 5).toString();
            if (status.equalsIgnoreCase("Chờ khám")) {
                btnApprove.setEnabled(true);
                btnCancelAppointment.setEnabled(true);
            } else {
                btnApprove.setEnabled(false);
                btnCancelAppointment.setEnabled(false);
            }
        }
    }

    // Hàm cập nhật nhanh từ thanh công cụ
   // Hàm cập nhật nhanh từ thanh công cụ (Đã bổ sung biến truyền doctorId)
    private void handleStatusUpdate(String nextStatusId, String confirmMessage) {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, confirmMessage, "Xác nhận thao tác", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // TRUYỀN THÊM biến doctorId vào cuối hàm xử lý của Controller
            boolean success = controller.updateStatus(id, nextStatusId, doctorId); 
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
                loadAppointments();
            } else {
                JOptionPane.showMessageDialog(this, "Thao tác cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // Gọi hộp thoại rút gọn khi double click vào bảng
    private void editAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        // Chỉ cho phép thao tác nếu lịch hẹn đang "Chờ khám"
        String status = model.getValueAt(row, 5).toString();
        if (!status.equalsIgnoreCase("Chờ khám")) {
            JOptionPane.showMessageDialog(this, "Lịch hẹn này đã được xử lý, không thể thay đổi nữa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DentalAppointmentModel ap = new DentalAppointmentModel();
        ap.setId((int) model.getValueAt(row, 0));
        ap.setPatientName(model.getValueAt(row, 1).toString());
        ap.setAppointmentDate(model.getValueAt(row, 2).toString());
        ap.setAppointmentTime(model.getValueAt(row, 3).toString());
        ap.setProblem(model.getValueAt(row, 4).toString());
        ap.setStatus("pending"); // Chuyển đổi chuẩn về chuỗi database trước khi gửi qua Dialog

        //  CODE MỚI ĐÃ SỬA:
AppointmentDialog dialog = new AppointmentDialog(this, ap, this.doctorId);
    }
}