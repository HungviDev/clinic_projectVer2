package ui.patient;

import config.DBConnection;
import controller.user.AppointmentDetailController;
import model.user.AppointmentDetail;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppointmentDetailDialog extends JDialog {

    private final Color COLOR_BG = new Color(248, 250, 252);
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color TEXT_MUTED = new Color(149, 165, 166);
    private final Color COLOR_SUCCESS = new Color(39, 174, 96); // Màu xanh lá cây cho trạng thái Hoàn thành
    private final Color STATUS_BLUE = new Color(52, 152, 219);     // Màu xanh dương cho "Đã duyệt/Chờ duyệt"
    private final Color STATUS_GRAY = new Color(189, 195, 199);    // Màu xám cho "Kết thúc/Hủy"
    
    private AppointmentDetailController controller;

    public AppointmentDetailDialog(
            Window parent,
            int appointmentId,
            Runnable onCancelSuccess
    ) {

        super(
                parent,
                "Chi tiết lịch hẹn",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        controller = new AppointmentDetailController();
        setSize(450, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_BG);

        AppointmentDetail detail = controller.getDetail(appointmentId);

        // TẠO GIAO DIỆN KHÁCH HÀNG
        JPanel pnlCustomer = new JPanel();
        pnlCustomer.setLayout(new BoxLayout(pnlCustomer, BoxLayout.Y_AXIS));
        pnlCustomer.setBackground(Color.WHITE);
        pnlCustomer.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblHead1 = new JLabel("Thông tin khách hàng");
        lblHead1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlCustomer.add(lblHead1);
        pnlCustomer.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlCustomer.add(createDataRow("Họ và tên:", detail.getPatientName()));
        pnlCustomer.add(createDataRow("Điện thoại:", detail.getPhone()));
        pnlCustomer.add(createDataRow("Ngày sinh:", detail.getDob()));
        pnlCustomer.add(createDataRow("Địa chỉ:", detail.getAddress()));

        // TẠO GIAO DIỆN ĐIỀU TRỊ
        JPanel pnlTreatment = new JPanel();
        pnlTreatment.setLayout(new BoxLayout(pnlTreatment, BoxLayout.Y_AXIS));
        pnlTreatment.setBackground(Color.WHITE);
        pnlTreatment.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblHead2 = new JLabel("Thông tin điều trị");
        lblHead2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlTreatment.add(lblHead2);
        pnlTreatment.add(Box.createRigidArea(new Dimension(0, 15)));
        // pnlTreatment.add(createDataRow("Chi nhánh:", branch));
        pnlTreatment.add(createDataRow("Dịch vụ:", detail.getService()));
        pnlTreatment.add(createDataRow("Bác sĩ:", detail.getDoctor()));
        pnlTreatment.add(createDataRow("Ngày hẹn:", detail.getDate()));
        pnlTreatment.add(createDataRow("Khung giờ:", detail.getTime()));
        
        // HIỂN THỊ TRẠNG THÁI (MỚI THÊM)
        pnlTreatment.add(createStatusRow("Trạng thái:", detail.getStatus()));

        mainPanel.add(pnlCustomer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        mainPanel.add(pnlTreatment);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        // NÚT HỦY LỊCH HẸN
        String currentStatus = detail.getStatus();
        
        // Chặn hủy nếu trạng thái là "Completed"
        if (!"Completed".equalsIgnoreCase(currentStatus) && controller.canCancel(currentStatus)) {
            JButton btnCancel = new JButton("Huỷ lịch hẹn");
            btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnCancel.setBackground(new Color(231, 76, 60));
            btnCancel.setForeground(Color.WHITE);
            btnCancel.setFocusPainted(false);
            btnCancel.setPreferredSize(new Dimension(450, 50));
            btnCancel.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy lịch hẹn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.cancelAppointment(appointmentId);
                    
                    dispose(); // Đóng popup
                    
                    // GỌI HÀM LÀM MỚI DỮ LIỆU Ở TRANG GỐC
                    if (onCancelSuccess != null) {
                        onCancelSuccess.run();
                    }
                }
            });

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
            bottomPanel.setBackground(COLOR_BG);
            bottomPanel.add(btnCancel, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }

    // Hàm tạo dòng dữ liệu bình thường
    private JPanel createDataRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(5, 0, 10, 0));
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLabel.setForeground(TEXT_MUTED); 
        JLabel lblVal = new JLabel("<html><div style='text-align: right;'>" + (value != null ? value : "") + "</div></html>", SwingConstants.RIGHT);
        lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblVal.setForeground(TEXT_DARK);
        row.add(lblLabel, BorderLayout.WEST);
        row.add(lblVal, BorderLayout.CENTER);
        return row;
    }

    // Hàm tạo dòng dữ liệu ĐẶC BIỆT DÀNH RIÊNG CHO TRẠNG THÁI (Đổi chữ + Đổi màu)
    private JPanel createStatusRow(String label, String statusValue) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(5, 0, 10, 0));
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLabel.setForeground(TEXT_MUTED); 
        
        String displayValue = statusValue != null ? statusValue : "Không rõ";
        Color statusColor = TEXT_DARK; // Mặc định là màu chữ tối
        
        // Logic kiểm tra: Nếu là "Completed" thì đổi chữ và tô màu xanh lá
        if ("completed".equalsIgnoreCase(statusValue)) {
            displayValue = "Đã hoàn thành";
            statusColor = COLOR_SUCCESS;
        } else if ("pending".equalsIgnoreCase(statusValue)) {
            displayValue = "Chờ duyệt";
            statusColor = new Color(243, 156, 18); // Màu cam cho lịch chờ (Bonus thêm cho bạn)
        } else if ("approved".equalsIgnoreCase(statusValue)) {
            displayValue = "Đã duyệt";
            statusColor = STATUS_BLUE; 
        } else if ("cancelled".equalsIgnoreCase(displayValue)) {
            displayValue = "Đã hủy";
            statusColor = STATUS_GRAY;
        }

        JLabel lblVal = new JLabel("<html><div style='text-align: right;'>" + displayValue + "</div></html>", SwingConstants.RIGHT);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 16)); // In đậm chữ trạng thái
        lblVal.setForeground(statusColor); // Áp dụng màu sắc
        
        row.add(lblLabel, BorderLayout.WEST);
        row.add(lblVal, BorderLayout.CENTER);
        return row;
    }
}