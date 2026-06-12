package ui.admin;

import controller.admin.AppointmentController;
import model.admin.AppointmentModel;
import ui.admin.form.AppointmentEditForm;
import ui.admin.form.AppointmentForm;
import ui.admin.form.UserAddForm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class AppointmentView extends JPanel {

    // =====================================
    // COLOR
    // =====================================
    private final Color BACKGROUND_COLOR =
            new Color(240, 245, 250);

    private final Color PRIMARY_COLOR =
            new Color(0, 102, 204);

    private final Color SUCCESS_COLOR =
            new Color(46, 204, 113);

    private final Color DANGER_COLOR =
            new Color(231, 76, 60);

    private final Color WARNING_COLOR =
            new Color(241, 196, 15);

    // =====================================
    // TABLE
    // =====================================
    private JTable table;

    private DefaultTableModel model;

    // =====================================
    // CONTROLLER
    // =====================================
    private AppointmentController appointmentController =
            new AppointmentController();

    // =====================================
    // CONSTRUCTOR
    // =====================================
    public AppointmentView() {

        setLayout(new BorderLayout());

        setBackground(BACKGROUND_COLOR);

        // =====================================
        // TOP PANEL
        // =====================================
        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.setBackground(BACKGROUND_COLOR);

        topPanel.setBorder(
                new EmptyBorder(20, 20, 10, 20)
        );

        // =====================================
        // TITLE
        // =====================================
        JLabel lblTitle =
                new JLabel("QUẢN LÝ LỊCH HẸN");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        lblTitle.setForeground(PRIMARY_COLOR);

        topPanel.add(lblTitle, BorderLayout.WEST);

        // =====================================
        // BUTTON PANEL
        // =====================================
        JPanel buttonPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT,
                        10,
                        0
                ));

        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnAdd =
                createButton(
                        "Tạo lịch hẹn",
                        SUCCESS_COLOR
                );
        JButton btnUpdate =
                        createButton(
                                "Sửa lịch hẹn",
                                SUCCESS_COLOR
                        );
        JButton btnDelete =
                createButton(
                        "Xóa",
                        DANGER_COLOR
                );


        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        btnAdd.addActionListener(e ->{
            JFrame parentFrame =
                    (JFrame) SwingUtilities
                            .getWindowAncestor(this);
            AppointmentForm form =
                    new AppointmentForm(parentFrame);

            form.setVisible(true);
        });
        btnUpdate.addActionListener(e -> {
        JFrame parentFrame =
                    (JFrame) SwingUtilities
                            .getWindowAncestor(this);
        int row = table.getSelectedRow();
        if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn cuộc hẹn để sửa");
                return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        AppointmentEditForm appointmentEditForm = new AppointmentEditForm(parentFrame,this,id);
        appointmentEditForm.setVisible(true);
        
        });

        // =====================================
        // XÓA EVENT
        // =====================================
        btnDelete.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn cuộc hẹn để xóa");
                return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        
        int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Bạn có chắc chắn muốn xóa cuộc hẹn này (ID: " + id + ")?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
                boolean result = appointmentController.deleteAppointment(id);
                if (result) {
                JOptionPane.showMessageDialog(this, "Xóa cuộc hẹn thành công!");
                loadAllAppointment();
                } else {
                JOptionPane.showMessageDialog(this, "Xóa cuộc hẹn thất bại!");
                }
        }
        });

 

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // =====================================
        // TABLE
        // =====================================
        String[] columns = {

                "ID Cuộc Hẹn",

                "Tên Bác Sĩ",

                "Tên Bệnh Nhân",

                "Ngày Hẹn",

                "Trạng Thái"
        };

        model =
                new DefaultTableModel(columns, 0);

        table = new JTable(model);

        // =====================================
        // TABLE STYLE
        // =====================================
        table.setRowHeight(38);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        table.setSelectionBackground(
                new Color(184, 207, 229)
        );

        table.setGridColor(
                new Color(220, 220, 220)
        );

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        table.getTableHeader().setBackground(
                PRIMARY_COLOR
        );

        table.getTableHeader().setForeground(
                Color.WHITE
        );

        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                new EmptyBorder(10, 20, 20, 20)
        );

        // =====================================
        // LOAD DATA
        // =====================================
        loadAllAppointment();

        // =====================================
        // ADD COMPONENT
        // =====================================
        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
    }

    // =====================================
    // CREATE BUTTON
    // =====================================
    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setPreferredSize(
                new Dimension(120, 42)
        );

        return button;
    }

    // =====================================
    // LOAD ALL APPOINTMENT
    // =====================================
    public void loadAllAppointment() {
        appointmentController.cancelPastAppointments();
        try {
            model.setRowCount(0);
            List<AppointmentModel> appointmentList =
                    appointmentController.getAllAppointment();
            appointmentList.forEach(appointment -> {
                String status = appointment.getStatus();
                if ("pending".equalsIgnoreCase(status)) {
                        status = "Chờ duyệt";
                } else if ("approved".equalsIgnoreCase(status)) {
                        status = "Đã duyệt";
                } else if ("reject".equalsIgnoreCase(status)) {
                        status = "Từ chối";
                } else if ("completed".equalsIgnoreCase(status)) {
                        status = "Hoàn thành";
                }
                else if ("cancel".equalsIgnoreCase(status)) {
                        status = "Đã hủy";
                }
                model.addRow(new Object[]{

                        appointment.getId(),

                        appointment.getDoctorName(),

                        appointment.getPatientName(),

                        appointment.getAppointmentDate(),

                        status
                });
            });

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi load dữ liệu"
            );
        }
    }
}