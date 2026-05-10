package ui.doctor.View;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import ui.doctor.Controller.DoctorDashboardController;
import ui.doctor.Model.AppointmentModel;
import ui.doctor.Model.DoctorDashboardModel;

public class DoctorDashboardView extends JPanel {

    private DoctorDashboardController controller;

    // =====================================================
    // DATA
    // =====================================================
    private List<AppointmentModel> appointmentList;
    private int currentDoctorId;

    // =====================================================
    // LABELS
    // =====================================================
    private JLabel lblAppointments;
    private JLabel lblPatients;
    private JLabel lblCompleted;
    private JLabel lblRevenue;

    // =====================================================
    // TABLE
    // =====================================================
    private JTable table;
    private DefaultTableModel tableModel;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public DoctorDashboardView(
            int doctorId,
            String doctorName
    ) {

        controller =
                new DoctorDashboardController();

        this.currentDoctorId = doctorId;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 248, 252));

        // =================================================
        // HEADER
        // =================================================
        add(createHeader(doctorName),
                BorderLayout.NORTH);

        // =================================================
        // MAIN PANEL
        // =================================================
        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setOpaque(false);

        mainPanel.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        25,
                        25
                )
        );

        // =================================================
        // STATS PANEL
        // =================================================
        JPanel statsPanel =
                createStatsPanel();

        mainPanel.add(
                statsPanel,
                BorderLayout.NORTH
        );

        // =================================================
        // TABLE PANEL
        // =================================================
        JPanel tablePanel =
                createAppointmentPanel();

        mainPanel.add(
                tablePanel,
                BorderLayout.CENTER
        );

        add(mainPanel,
                BorderLayout.CENTER);

        // =================================================
        // ACTION PANEL
        // =================================================
        add(createActionPanel(),
                BorderLayout.SOUTH);

        // =================================================
        // LOAD DATA
        // =================================================
        loadDashboardData(doctorId);
        loadAppointments(doctorId);
    }

    // =====================================================
   // HEADER
// =====================================================
private JPanel createHeader(String doctorName) {

    JPanel panel =
            new JPanel(new BorderLayout());

    panel.setBackground(Color.WHITE);

    panel.setBorder(
            new EmptyBorder(
                    20,
                    30,
                    20,
                    30
            )
    );

    JLabel lblTitle =
            new JLabel("RĂNG KHỎE SỐNG VUI");

    lblTitle.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    30
            )
    );

    lblTitle.setForeground(
            new Color(0, 51, 102)
    );

    panel.add(
            lblTitle,
            BorderLayout.WEST
    );

    return panel;
}
    // =====================================================
    // STATS PANEL
    // =====================================================
    private JPanel createStatsPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                20,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        20,
                        0
                )
        );

        lblAppointments =
                new JLabel("0");

        lblPatients =
                new JLabel("0");

        lblCompleted =
                new JLabel("0");

        lblRevenue =
                new JLabel("0 VNĐ");

        panel.add(new StatCard(
                "Lịch hẹn hôm nay",
                lblAppointments,
                new Color(52, 152, 219)
        ));

        panel.add(new StatCard(
                "",
                lblPatients,
                new Color(46, 204, 113)
        ));

        panel.add(new StatCard(
                "Ca hoàn thành tháng",
                lblCompleted,
                new Color(243, 156, 18)
        ));

        // panel.add(new StatCard(
        //         "Doanh thu hôm nay",
        //         lblRevenue,
        //         new Color(155, 89, 182)
        // ));

        return panel;
    }

    // =====================================================
    // APPOINTMENT TABLE PANEL
    // =====================================================
    private JPanel createAppointmentPanel() {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220),
                                1,
                                true
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        JLabel lblTitle =
                new JLabel("LỊCH HẸN HÔM NAY");

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblTitle.setForeground(
                new Color(0, 51, 102)
        );

        panel.add(lblTitle,
                BorderLayout.NORTH);

        // =================================================
        // TABLE
        // =================================================
        String[] columns = {
            "Giờ",
            "Bệnh nhân",
            "Dịch vụ",
            "Trạng thái"
        };

        tableModel =
                new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        table =
                new JTable(tableModel);

        table.setRowHeight(35);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        table.setSelectionBackground(
                new Color(220, 235, 250)
        );

        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        table.getTableHeader().setBackground(
                new Color(0, 76, 153)
        );

        table.getTableHeader().setForeground(
                Color.WHITE
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                new EmptyBorder(
                        15,
                        0,
                        0,
                        0
                )
        );

        panel.add(scrollPane,
                BorderLayout.CENTER);

        return panel;
    }

 // =====================================================
// ACTION PANEL
// =====================================================
private JPanel createActionPanel() {

    JPanel panel =
            new JPanel(
                    new FlowLayout(
                            FlowLayout.RIGHT
                    )
            );

    JLabel lblInfo =
            new JLabel(
                    "Chọn lịch hẹn để cập nhật trạng thái"
            );

    panel.add(lblInfo);

    // =================================================
    // CLICK ROW EVENT
    // =================================================
    table.addMouseListener(
            new java.awt.event.MouseAdapter() {

        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e
        ) {

            int row =
                    table.getSelectedRow();

            if (row == -1) {
                return;
            }

            AppointmentModel ap =
                    appointmentList.get(row);

            // chỉ cho sửa pending
            if (!ap.getStatus()
                    .equalsIgnoreCase("pending")) {

                JOptionPane.showMessageDialog(
                        null,
                        "Chỉ sửa được lịch pending"
                );

                return;
            }

            // =================================================
            // OPTIONS
            // =================================================
            Object[] options = {
                "approved",
                "reject",
                "done"
            };

            int choice =
                    JOptionPane.showOptionDialog(
                            null,
                            "Chọn trạng thái mới",
                            "Cập nhật lịch hẹn",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

            // đóng cửa sổ
            if (choice == -1) {
                return;
            }

            String selectedStatus =
                    options[choice].toString();

            boolean updated =
                    controller.updateAppointmentStatus(
                            ap.getId(),
                            selectedStatus
                    );

            if (updated) {

                JOptionPane.showMessageDialog(
                        null,
                        "Cập nhật thành công"
                );

                loadAppointments(currentDoctorId);

                loadDashboardData(currentDoctorId);

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Cập nhật thất bại"
                );
            }
        }
    });

    return panel;
}

    // =====================================================
    // LOAD DASHBOARD DATA
    // =====================================================
    private void loadDashboardData(int doctorId) {

        DoctorDashboardModel model =
                controller.getDashboardData(
                        doctorId
                );

        lblAppointments.setText(
                String.valueOf(
                        model.getTotalAppointmentsToday()
                )
        );

        lblPatients.setText(
                String.valueOf(
                        model.getTotalPatientsTreating()
                )
        );

        lblCompleted.setText(
                String.valueOf(
                        model.getCompletedCasesMonth()
                )
        );

        NumberFormat vn =
                NumberFormat.getCurrencyInstance(
                        new Locale("vi", "VN")
                );

        lblRevenue.setText(
                vn.format(
                        model.getRevenueToday()
                )
        );
    }

    // =====================================================
    // LOAD APPOINTMENTS
    // =====================================================
    private void loadAppointments(int doctorId) {

        tableModel.setRowCount(0);

        appointmentList =
                controller.getTodayAppointments(
                        doctorId
                );

        for (AppointmentModel ap :
                appointmentList) {

            tableModel.addRow(new Object[]{
                ap.getTime(),
                ap.getPatientName(),
                ap.getServiceName(),
                ap.getStatus()
            });
        }
    }
}