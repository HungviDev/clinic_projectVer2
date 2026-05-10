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
    private AppointmentDetailController controller;

    // Tham số thứ 3 là một "Hành động" (Runnable) để báo cho trang gốc biết cần làm mới dữ liệu
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

    controller =
            new AppointmentDetailController();
        setSize(450, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_BG);

        AppointmentDetail detail =
                controller.getDetail(appointmentId);

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

        mainPanel.add(pnlCustomer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        mainPanel.add(pnlTreatment);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        // NÚT HỦY LỊCH HẸN
        if (controller.canCancel(
                detail.getStatus()
        )) {
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
}