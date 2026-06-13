package ui.admin;

import controller.admin.PaymentController;
import model.admin.PaymentModel;
import ui.admin.form.PaymentAddForm;
import ui.admin.form.PaymentEditForm;

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
            PaymentAddForm form = new PaymentAddForm(parentFrame);
            form.setVisible(true);
            if (form.isSaved()) {
                loadAllPayments();
            }
        });

        // =====================================
        // UPDATE EVENT
        // =====================================
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần sửa");
                return;
            }
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            PaymentEditForm form = new PaymentEditForm(parentFrame, id);
            form.setVisible(true);
            if (form.isSaved()) {
                loadAllPayments();
            }
        });

        // =====================================
        // DELETE EVENT
        // =====================================
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa");
                return;
            }
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa hóa đơn này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                boolean result = paymentController.deletePayment(id);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadAllPayments();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        });

        topPanel.add(buttonPanel, BorderLayout.EAST);

        String[] columns = {
                "ID",
                "Bệnh nhân",
                "Số tiền (VND)",
                "Phương thức",
                "Trạng thái",
                "Ngày tạo",
                "Mã giai đoạn"
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

        List<PaymentModel> list =
                paymentController.getAllPayments();

        list.forEach(payment -> {

            model.addRow(new Object[]{
                    payment.getId(),
                    payment.getPatientName(),
                    payment.getAmount(),
                    payment.getMethod(),
                    payment.getStatus(),
                    payment.getCreatedAt(),
                    payment.getTreatmentStageId() == 0 ? "Không có" : payment.getTreatmentStageId()
            });

        });
    }
}