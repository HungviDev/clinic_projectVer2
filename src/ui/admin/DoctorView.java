package ui.admin;

import controller.admin.DoctorController;
import global.GlobalData;
import model.admin.DoctorModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class DoctorView extends JPanel {
        
    private final Color BACKGROUND_COLOR =
            new Color(240, 245, 250);

    private final Color PRIMARY_COLOR =
            new Color(0, 102, 204);

    private final Color SUCCESS_COLOR =
            new Color(46, 204, 113);

    private final Color DANGER_COLOR =
            new Color(231, 76, 60);

    // =====================================
    // TABLE
    // =====================================
    private JTable table;

    private DefaultTableModel model;

    // =====================================
    // CONTROLLER
    // =====================================
    private DoctorController doctorController =
            new DoctorController();

    // =====================================
    // CONSTRUCTOR
    // =====================================
    public DoctorView() {

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
                new JLabel("QUẢN LÝ BÁC SĨ");

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
                        "Thêm",
                        SUCCESS_COLOR
                );

        JButton btnUpdate =
                createButton(
                        "Sửa",
                        PRIMARY_COLOR
                );

        JButton btnDelete =
                createButton(
                        "Xóa",
                        DANGER_COLOR
                );

        buttonPanel.add(btnAdd);

        buttonPanel.add(btnUpdate);

        buttonPanel.add(btnDelete);

        // =====================================
        // ADD EVENT
        // =====================================
        btnAdd.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ui.admin.form.DoctorAddForm form = new ui.admin.form.DoctorAddForm(parentFrame, this);
            form.setVisible(true);
        });

        // =====================================
        // UPDATE EVENT
        // =====================================
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bác sĩ");
                return;
            }
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ui.admin.form.DoctorEditForm form = new ui.admin.form.DoctorEditForm(parentFrame, this, id);
            form.setVisible(true);
        });

        // =====================================
        // DELETE EVENT
        // =====================================
        btnDelete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn bác sĩ"
                );

                return;
            }

            int id = Integer.parseInt(
                    table.getValueAt(row, 0).toString()
            );

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Bạn có chắc muốn xóa?",
                            "Xác nhận",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean result = doctorController.deleteDoctor(id);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Xóa bác sĩ thành công");
                    loadAllDoctor();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa bác sĩ thất bại");
                }
            }
        });

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // =====================================
        // TABLE
        // =====================================
        String[] columns = {
                "ID",
                "Họ và tên",
                "Số điện thoại",
                "Mật khẩu",
                "Ngày sinh",
                "Địa chỉ",
                "Email",
                "Chuyên ngành",
                "Năm kinh nghiệm"
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
        loadAllDoctor();

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
    // LOAD ALL DOCTOR
    // =====================================
    public void loadAllDoctor() {

        try {

            model.setRowCount(0);

            List<DoctorModel> doctorList =
                    doctorController.getAllDoctor();

            System.out.println(
                    "SIZE: " + doctorList.size()
            );

            doctorList.forEach(user -> {

                model.addRow(new Object[]{
                        user.getId(),
                        user.getFullName(),
                        user.getPhone(),
                        user.getPassword(),
                        user.getBirthDate(),
                        user.getAddress(),
                        user.getEmail(),
                        user.getSpecialization(),
                        user.getExperience()
                });

                System.out.println(user);
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