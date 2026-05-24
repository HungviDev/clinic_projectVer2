package ui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.admin.AppointmentController;
import controller.admin.OrderController;
import controller.admin.ServiceController;
import controller.admin.UserController;

import java.awt.*;

public class DashboardView extends JPanel {

    // =====================================
    // COLOR
    // =====================================
    private final Color BACKGROUND_COLOR =
            new Color(245, 247, 250);

    private final Color CARD_COLOR =
            Color.WHITE;

    private final Color PRIMARY_COLOR =
            new Color(52, 152, 219);

    private final Color SUCCESS_COLOR =
            new Color(46, 204, 113);

    private final Color WARNING_COLOR =
            new Color(241, 196, 15);

    private final Color DANGER_COLOR =
            new Color(231, 76, 60);
    UserController userController = new UserController();
    AppointmentController appointmentController = new AppointmentController();
    OrderController orderController = new OrderController();
    ServiceController serviceController = new ServiceController();
    public DashboardView() {

        setLayout(new BorderLayout());

        setBackground(BACKGROUND_COLOR);

        // =====================================
        // MAIN PANEL
        // =====================================
        JPanel mainPanel =
                new JPanel();

        mainPanel.setLayout(
                new BorderLayout(20, 20)
        );

        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        // =====================================
        // TITLE
        // =====================================
        JLabel lblTitle =
                new JLabel("DASHBOARD");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 32)
        );

        lblTitle.setForeground(
                new Color(44, 62, 80)
        );

        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // =====================================
        // CONTENT PANEL
        // =====================================
        JPanel contentPanel =
                new JPanel();

        contentPanel.setLayout(
                new BorderLayout(20, 20)
        );

        contentPanel.setBackground(BACKGROUND_COLOR);

        // =====================================
        // CARD PANEL
        // =====================================
        JPanel cardPanel =
                new JPanel(
                        new GridLayout(2, 3, 20, 20)
                );

        cardPanel.setBackground(BACKGROUND_COLOR);

        cardPanel.add(
        createCard(
                "Bệnh nhân",
                String.valueOf(
                        userController.countPatients()
                ),
                PRIMARY_COLOR
                )
        );

        cardPanel.add(
                createCard(
                        "Tổng Bác Sĩ",
                        String.valueOf(
                                userController.countDoctors()
                        ),
                        SUCCESS_COLOR
                )
        );

        cardPanel.add(
        createCard(
                "Tổng Lịch Hẹn",
                String.valueOf(
                        appointmentController.countAppointments()
                ),
                WARNING_COLOR
        )
);

        cardPanel.add(
        createCard(
                "Tổng Đơn Hàng",
                String.valueOf(
                        orderController.countOrders()
                ),
                DANGER_COLOR
        )
);

        cardPanel.add(
        createCard(
                "Tổng Doanh Thu",
                String.valueOf(
                        orderController.getTotalRevenue()
                ),
                SUCCESS_COLOR
        )
);

        cardPanel.add(
        createCard(
                "Tổng Dịch Vụ",
                String.valueOf(
                        serviceController.countServices()
                ),
                PRIMARY_COLOR
        )
);

        // =====================================
        // BOTTOM PANEL
        // =====================================
        JPanel bottomPanel =
                new JPanel(
                        new GridLayout(1, 2, 20, 20)
                );

        bottomPanel.setBackground(BACKGROUND_COLOR);

        // =====================================
        // APPOINTMENT PANEL
        // =====================================
        JPanel appointmentPanel =
                createInfoPanel(
                        "Lịch hẹn gần đây"
                );

        JTextArea txtAppointment =
                new JTextArea();

        txtAppointment.setText(
                """
                • Nguyễn Văn A - 09:00 AM
                • Trần Văn B - 10:30 AM
                • Lê Văn C - 01:00 PM
                • Phạm Thị D - 03:00 PM
                """
        );

        txtAppointment.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        txtAppointment.setEditable(false);

        txtAppointment.setBackground(Color.WHITE);

        appointmentPanel.add(txtAppointment);

        // =====================================
        // ORDER PANEL
        // =====================================
        JPanel orderPanel =
                createInfoPanel(
                        "Đơn hàng gần đây"
                );

        JTextArea txtOrder =
                new JTextArea();

        txtOrder.setText(
                """
                • Implant - 15,000,000đ
                • Niềng răng - 30,000,000đ
                • Bọc sứ - 5,000,000đ
                • Tẩy trắng - 1,500,000đ
                """
        );

        txtOrder.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        txtOrder.setEditable(false);

        txtOrder.setBackground(Color.WHITE);

        orderPanel.add(txtOrder);

        bottomPanel.add(appointmentPanel);

        bottomPanel.add(orderPanel);

        // =====================================
        // ADD COMPONENT
        // =====================================
        contentPanel.add(cardPanel, BorderLayout.NORTH);

        contentPanel.add(bottomPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // =====================================
    // CREATE CARD
    // =====================================
    private JPanel createCard(
            String title,
            String value,
            Color color
    ) {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBackground(CARD_COLOR);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(230, 230, 230)
                        ),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        lblTitle.setForeground(
                Color.GRAY
        );

        JLabel lblValue =
                new JLabel(value);

        lblValue.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        lblValue.setForeground(color);

        panel.add(lblTitle, BorderLayout.NORTH);

        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    // =====================================
    // CREATE INFO PANEL
    // =====================================
    private JPanel createInfoPanel(
            String title
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(230, 230, 230)
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblTitle.setBorder(
                new EmptyBorder(0, 0, 10, 0)
        );

        panel.add(lblTitle, BorderLayout.NORTH);

        return panel;
    }
}