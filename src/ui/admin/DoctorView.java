package ui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import controller.admin.DoctorController;
import model.DoctorModel;

import java.awt.*;

public class DoctorView extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private DoctorController DoctorController = new DoctorController();
    public DoctorView() {
        setLayout(new BorderLayout());
        setBackground(new Color(220, 235, 250));
        // =====================================
        // TITLE PANEL
        // =====================================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(220, 235, 250));
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ BÁC SĨ");

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));

        lblTitle.setForeground(new Color(0, 51, 102));

        topPanel.add(lblTitle, BorderLayout.WEST);

        // =====================================
        // BUTTON PANEL
        // =====================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.setBackground(new Color(220, 235, 250));

        JButton btnAdd = createButton("Thêm");
        btnAdd.setForeground(Color.BLACK);
        JButton btnUpdate = createButton("Sửa");
        btnUpdate.setForeground(Color.BLACK);
        JButton btnDelete = createButton("Xóa");
        btnDelete.setForeground(Color.BLACK);
        JButton btnRefresh = createButton("Làm mới");
        btnRefresh.setForeground(Color.BLACK);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

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
                "Avatar",
                "Email",
                "Chuyên ngành",
                "Năm kinh nghiệm"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(35);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        table.getTableHeader().setBackground(
                new Color(0, 76, 153)
        );

        table.setSelectionBackground(
                new Color(184, 207, 229)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));
        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
        loadAllUser();
        btnRefresh.addActionListener(e -> loadAllUser());
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFocusPainted(false);

        button.setBackground(new Color(0, 76, 153));

        button.setForeground(Color.WHITE);

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(110, 40));

        return button;
    }
     private void loadAllUser() {

        model.setRowCount(0);

        List<DoctorModel> userList =
                DoctorController.getAllDoctor();
        System.out.println("SIZE: " + userList.size());
        userList.forEach(user -> {
            model.addRow(new Object[]{

                    user.getId(),

                    user.getFullName(),

                    user.getPhone(),

                    user.getPassword(),

                    user.getBirthDate(),

                    user.getAddress(),

                    user.getAvatar(),
                    user.getEmail(),
                    user.getSpecialization(),
                    user.getExperience()
            });
        });
    }
}