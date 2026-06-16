package ui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.admin.AppointmentController;
import controller.admin.PaymentController;
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
    PaymentController paymentController = new PaymentController();
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
                "Tổng Doanh Thu",
                String.valueOf(
                        paymentController.getTotal() +" VND"
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

        StringBuilder apptBuilder = new StringBuilder();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (model.admin.AppointmentModel appt : appointmentController.getRecentAppointments(4)) {
            String dateStr = appt.getAppointmentDate() != null ? sdf.format(appt.getAppointmentDate()) : "N/A";
            apptBuilder.append("• ").append(appt.getPatientName()).append(" - ").append(dateStr).append("\n");
        }
        if (apptBuilder.length() == 0) {
            apptBuilder.append("Chưa có lịch hẹn nào.");
        }
        txtAppointment.setText(apptBuilder.toString());

        txtAppointment.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        txtAppointment.setEditable(false);

        txtAppointment.setBackground(Color.WHITE);

        appointmentPanel.add(txtAppointment);

        // =====================================
        // INVOICE PANEL
        // =====================================
        JPanel orderPanel =
                createInfoPanel(
                        "Hóa đơn gần đây"
                );

        JTextArea txtOrder =
                new JTextArea();

        StringBuilder orderBuilder = new StringBuilder();
        java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));
        for (model.admin.PaymentModel payment : paymentController.getRecentPayments(4)) {
            String stageName = payment.getTreatmentStageName() != null ? payment.getTreatmentStageName() : "Khác";
            orderBuilder.append("• ").append(payment.getPatientName()).append(" - ").append(stageName).append(" - ").append(currencyFormat.format(payment.getAmount())).append("\n");
        }
        if (orderBuilder.length() == 0) {
            orderBuilder.append("Chưa có hóa đơn nào.");
        }
        txtOrder.setText(orderBuilder.toString());

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