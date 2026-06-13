package ui.admin.form;

import controller.admin.PaymentController;
import model.admin.PaymentModel;
import model.admin.UserModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class PaymentAddForm extends JDialog {

    private JComboBox<String> cboPatient;
    private JTextField txtAmount;
    private JComboBox<String> cboMethod;
    private JComboBox<String> cboStatus;
    private JComboBox<String> cboStage;

    private JButton btnSave;
    private JButton btnCancel;

    private PaymentController paymentController;
    private List<UserModel> patientList;
    private List<PaymentController.TreatmentStageOption> stageList;
    private boolean isSaved = false;

    public PaymentAddForm(JFrame parent) {
        super(parent, "THÊM HÓA ĐƠN MỚI", true);
        paymentController = new PaymentController();

        setSize(500, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(220, 235, 250));

        JLabel lblTitle = new JLabel("THÊM HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        // =====================================
        // FORM PANEL
        // =====================================
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBackground(new Color(220, 235, 250));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. Patient ComboBox
        cboPatient = new JComboBox<>();
        cboPatient.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        patientList = paymentController.getAllPatients();
        for (UserModel u : patientList) {
            cboPatient.addItem(u.getFullName() + " (SĐT: " + u.getPhone() + ")");
        }

        // 2. Amount Text Field
        txtAmount = new JTextField();
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 3. Method ComboBox
        cboMethod = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Thẻ tín dụng"});
        cboMethod.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 4. Status ComboBox
        cboStatus = new JComboBox<>(new String[]{"Chưa thanh toán", "Đã thanh toán"});
        cboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 5. Stage ComboBox
        cboStage = new JComboBox<>();
        cboStage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboStage.addItem("Không chọn (Giai đoạn tự do)");
        stageList = paymentController.getAllTreatmentStages();
        for (PaymentController.TreatmentStageOption s : stageList) {
            cboStage.addItem(s.name);
        }

        formPanel.add(createLabel("Bệnh nhân"));
        formPanel.add(cboPatient);

        formPanel.add(createLabel("Số tiền (VND)"));
        formPanel.add(txtAmount);

        formPanel.add(createLabel("Phương thức"));
        formPanel.add(cboMethod);

        formPanel.add(createLabel("Trạng thái"));
        formPanel.add(cboStatus);

        formPanel.add(createLabel("Giai đoạn"));
        formPanel.add(cboStage);

        add(formPanel, BorderLayout.CENTER);

        // =====================================
        // BUTTON PANEL
        // =====================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(220, 235, 250));

        btnSave = createButton("Lưu");
        btnCancel = createButton("Hủy");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> savePayment());
        btnCancel.addActionListener(e -> dispose());
    }

    private void savePayment() {
        try {
            if (patientList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có bệnh nhân để chọn");
                return;
            }
            int patientIdx = cboPatient.getSelectedIndex();
            if (patientIdx < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bệnh nhân");
                return;
            }
            int userId = patientList.get(patientIdx).getId();

            String amountStr = txtAmount.getText().trim();
            if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount < 0) {
                    JOptionPane.showMessageDialog(this, "Số tiền không được âm");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ");
                return;
            }

            String method = (String) cboMethod.getSelectedItem();
            String status = (String) cboStatus.getSelectedItem();

            int stageIdx = cboStage.getSelectedIndex();
            int stageId = 0;
            if (stageIdx > 0 && stageList != null) {
                stageId = stageList.get(stageIdx - 1).id;
            }

            PaymentModel payment = new PaymentModel();
            payment.setUserId(userId);
            payment.setAmount(amount);
            payment.setMethod(method);
            payment.setStatus(status);
            payment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            payment.setTreatmentStageId(stageId);

            boolean result = paymentController.insertPayment(payment);
            if (result) {
                JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công");
                isSaved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm hóa đơn thất bại");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    public boolean isSaved() {
        return isSaved;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(0, 51, 102));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
