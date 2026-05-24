package ui.admin.form;

import model.admin.TreatmentDetail;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DetailForm extends JDialog {

    private JLabel lblPatientIdValue;
    private JLabel lblPatientNameValue;
    private JLabel lblDiagnosisValue;
    private JLabel lblRouteNameValue;
    private JLabel lblDoctorNameValue;

    public DetailForm(JFrame parent, TreatmentDetail detail) {

        super(parent, "CHI TIẾT ĐIỀU TRỊ", true);

        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(245, 248, 255));

        // ================= HEADER =================
        JPanel header = new JPanel();
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(new EmptyBorder(15, 15, 15, 15));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("THÔNG TIN CHI TIẾT ĐIỀU TRỊ");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // ================= CONTENT =================
        JPanel content = new JPanel(new GridLayout(5, 2, 10, 15));
        content.setBorder(new EmptyBorder(20, 25, 20, 25));
        content.setBackground(new Color(245, 248, 255));

        content.add(createLabel("Mã bệnh nhân:"));
        lblPatientIdValue = createValue(detail.getPatientId());
        content.add(lblPatientIdValue);

        content.add(createLabel("Tên bệnh nhân:"));
        lblPatientNameValue = createValue(detail.getPatientName());
        content.add(lblPatientNameValue);

        content.add(createLabel("Chuẩn đoán:"));
        lblDiagnosisValue = createValue(detail.getDiagnosis());
        content.add(lblDiagnosisValue);

        content.add(createLabel("Tên lộ trình:"));
        lblRouteNameValue = createValue(detail.getRouteName());
        content.add(lblRouteNameValue);

        content.add(createLabel("Bác sĩ phụ trách:"));
        lblDoctorNameValue = createValue(detail.getDoctorName());
        content.add(lblDoctorNameValue);

        add(content, BorderLayout.CENTER);

        // ================= FOOTER =================
        JPanel footer = new JPanel();
        footer.setBackground(new Color(245, 248, 255));

        JButton btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(120, 38));
        btnClose.setBackground(new Color(41, 128, 185));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnClose.addActionListener(e -> dispose());

        footer.add(btnClose);

        add(footer, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JLabel createValue(String text) {
        JLabel label = new JLabel(text != null ? text : "N/A");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(20, 20, 20));
        return label;
    }
}