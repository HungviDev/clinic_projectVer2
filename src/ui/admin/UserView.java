package ui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.admin.UserController;
import model.User;
// import model.UserModel;
import ui.admin.form.UserAddForm;

import java.awt.*;
import java.util.List;

public class UserView extends JPanel {

    private JTable table;

    private DefaultTableModel model;
        private final Color SIDEBAR_COLOR = new Color(214, 234, 248); // Xanh biển pastel (sáng, nhạt)

    private UserController userController =
            new UserController();

    // =====================================
    // CONSTRUCTOR
    // =====================================
    public UserView() {

        setLayout(new BorderLayout());

        setBackground(new Color(220, 235, 250));

        // =====================================
        // TITLE PANEL
        // =====================================
        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.setBackground(
                new Color(220, 235, 250)
        );

        topPanel.setBorder(
                new EmptyBorder(20, 20, 10, 20)
        );

        JLabel lblTitle =
                new JLabel("QUẢN LÝ NGƯỜI DÙNG");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        lblTitle.setForeground(
                new Color(0, 51, 102)
        );

        topPanel.add(lblTitle, BorderLayout.WEST);

        // =====================================
        // BUTTON PANEL
        // =====================================
        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.setBackground(
                SIDEBAR_COLOR
        );

        JButton btnAdd = createButton("Thêm");

        JButton btnUpdate = createButton("Sửa");

        JButton btnDelete = createButton("Xóa");

        btnAdd.setBackground(SIDEBAR_COLOR);

        btnUpdate.setBackground(SIDEBAR_COLOR);

        btnDelete.setBackground(SIDEBAR_COLOR);


        buttonPanel.add(btnAdd);

        buttonPanel.add(btnUpdate);

        buttonPanel.add(btnDelete);


        // =====================================
        // ADD EVENT
        // =====================================
        btnAdd.addActionListener(e -> {

            JFrame parentFrame =
                    (JFrame) SwingUtilities
                            .getWindowAncestor(this);

            UserAddForm form =
                    new UserAddForm(parentFrame);

            form.setVisible(true);
        });

        // =====================================
        // REFRESH EVENT
        // =====================================

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

                "Email"
        };

        model =
                new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(35);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        table.getTableHeader().setBackground(
                SIDEBAR_COLOR
        );

        table.setSelectionBackground(
                new Color(184, 207, 229)
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                new EmptyBorder(10, 20, 20, 20)
        );

        // =====================================
        // LOAD DATA
        // =====================================
        loadAllUser();

        // =====================================
        // ADD COMPONENT
        // =====================================
        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
              btnDelete.addActionListener(e ->{
            int row = table.getSelectedRow();
            int id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
            userController.deleteUser(id);
            loadAllUser();
        });
      
    }

    // =====================================
    // CREATE BUTTON
    // =====================================
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFocusPainted(false);

        button.setBackground(
                new Color(0, 76, 153)
        );

        button.setForeground(Color.BLACK);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(
                new Dimension(110, 40)
        );

        return button;
    }

    public void loadAllUser() {

        try {
            model.setRowCount(0);
            List<User> userList =
                    userController.getAllUsers();

            System.out.println(
                    "SIZE: " + userList.size()
            );

            userList.forEach(user -> {

                model.addRow(new Object[]{

                        user.getId(),

                        user.getFullName(),

                        user.getPhone(),

                        user.getPassword(),

                        user.getBirthDate(),

                        user.getAddress(),

                        user.getAvatar(),

                        user.getEmail()
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
    private boolean deleteUser(int id){
        return userController.deleteUser(id);
    } 
}