package ui.admin;

import controller.admin.PaymentController;
import model.admin.PaymentModel;
import model.admin.PaymentSummaryModel;
import ui.admin.form.PaymentAddForm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoiceView extends JPanel {

    private final Color BACKGROUND_COLOR =
            new Color(240, 245, 250);

    private final Color PRIMARY_COLOR =
            new Color(0, 102, 204);

    private final Color SUCCESS_COLOR =
            new Color(46, 204, 113);

    private final Color DANGER_COLOR =
            new Color(231, 76, 60);

    private JTable table;
    private DefaultTableModel model;
    private List<PaymentSummaryModel> summaryList;

    private PaymentController paymentController =
            new PaymentController();

    public InvoiceView() {

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(
                new EmptyBorder(20, 20, 10, 20)
        );

        JLabel lblTitle =
                new JLabel("QUẢN LÝ HÓA ĐƠN");

        lblTitle.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        30)
        );

        lblTitle.setForeground(PRIMARY_COLOR);

        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setBackground(
                BACKGROUND_COLOR
        );

        JButton btnAdd =
                createButton(
                        "Thêm",
                        SUCCESS_COLOR
                );

        JButton btnDetail =
                createButton(
                        "Chi tiết",
                        PRIMARY_COLOR
                );

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDetail);

        // =====================================
        // ADD EVENT
        // =====================================
        btnAdd.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            PaymentAddForm form = new PaymentAddForm(parentFrame);
            form.setVisible(true);
            if (form.isSaved()) {
                loadAllPayments();
            }
        });

        // =====================================
        // DETAIL EVENT
        // =====================================
        btnDetail.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân để xem chi tiết");
                return;
            }
            PaymentSummaryModel summary = summaryList.get(row);
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            InvoiceDetailDialog dialog = new InvoiceDetailDialog(parentFrame, summary);
            dialog.setVisible(true);
            // Reload after closing detail dialog because payments might have changed
            loadAllPayments();
        });

        topPanel.add(buttonPanel, BorderLayout.EAST);

        String[] columns = {
                "Mã Bệnh nhân",
                "Bệnh nhân",
                "Tổng tiền",
                "Đã thanh toán",
                "Còn nợ"
        };

        model =
                new DefaultTableModel(
                        columns,
                        0
                );

        table = new JTable(model);

        table.setRowHeight(38);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                new EmptyBorder(10, 20, 20, 20)
        );

        loadAllPayments();

        add(topPanel,
                BorderLayout.NORTH);

        add(scrollPane,
                BorderLayout.CENTER);
    }

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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        button.setPreferredSize(
                new Dimension(120, 42)
        );

        return button;
    }

    private void loadAllPayments() {

        model.setRowCount(0);

        summaryList = paymentController.getPaymentSummary();

        java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));

        summaryList.forEach(summary -> {
            model.addRow(new Object[]{
                    summary.getUserId(),
                    summary.getPatientName(),
                    currencyFormat.format(summary.getTotalAmount()),
                    currencyFormat.format(summary.getPaidAmount()),
                    currencyFormat.format(summary.getUnpaidAmount())
            });
        });
    }
}