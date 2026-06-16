package ui.admin;

import model.admin.OrderModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class OrderView extends JPanel {

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


    // =====================================
    // CONSTRUCTOR
    // =====================================
    public OrderView() {

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
                new JLabel("QUẢN LÝ ĐƠN HÀNG");

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

        JButton btnDelete =
                createButton(
                        "Xóa",
                        DANGER_COLOR
                );

        JButton btnRefresh =
                createButton(
                        "Làm mới",
                        WARNING_COLOR
                );

        buttonPanel.add(btnDelete);

        buttonPanel.add(btnRefresh);

        // =====================================
        // REFRESH EVENT
        // =====================================
        btnRefresh.addActionListener(e -> {


            JOptionPane.showMessageDialog(
                    this,
                    "Đã tải lại dữ liệu"
            );
        });

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // =====================================
        // TABLE
        // =====================================
        String[] columns = {

                "ID Đơn Hàng",

                "Tên Người Mua",

                "Tên Dịch Vụ",

                "Số Lượng",

                "Tổng Tiền",

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
    // LOAD ALL ORDER
    // =====================================

}