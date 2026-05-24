
package ui.doctor.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.doctor.Model.MedicalRecordModel;

public class MedicalRecordCard extends JPanel {

    public MedicalRecordCard(MedicalRecordModel record, Runnable onEdit, Runnable onDelete) {
        setLayout(new BorderLayout(20, 0));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));

        add(createAvatarPanel(), BorderLayout.WEST);
        add(createInfoPanel(record), BorderLayout.CENTER);
        add(createActionPanel(onEdit, onDelete), BorderLayout.EAST);
    }

    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(110, 110));

        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(235, 245, 255));
        lblAvatar.setForeground(new Color(52, 152, 219));
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 230), 1, true));

        panel.add(lblAvatar, BorderLayout.CENTER);
        return panel;
    }

  private JPanel createInfoPanel(MedicalRecordModel record) {
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    // --- THÊM DÒNG NÀY ĐỂ HIỂN THỊ TÊN ---
    JLabel lblName = new JLabel("Bệnh nhân: " + record.getPatientName());
    lblName.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Tên nên để chữ to hơn chút
    // ------------------------------------

    JLabel lblDisease = new JLabel("Bệnh lý: " + record.getDisease());
    lblDisease.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    JLabel lblDuration = new JLabel("Tổng thời gian điều trị: " + record.getTreatmentDurationDays() + " ngày");
    lblDuration.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    String dateRange = record.getStartDate() + "  ➔  " + record.getEndDate();
    JLabel lblDates = new JLabel("Thời hạn: " + dateRange);
    lblDates.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    JLabel lblStage = new JLabel("Giai đoạn điều trị: " + record.getCurrentStage());
    lblStage.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblStage.setForeground(new Color(41, 128, 185));

    // Thêm lblName vào panel (nên để ở trên cùng)
    panel.add(Box.createVerticalStrut(8));
    panel.add(lblName); 
    panel.add(Box.createVerticalStrut(5));
    panel.add(lblDisease);
    panel.add(Box.createVerticalStrut(5));
    panel.add(lblDuration);
    panel.add(Box.createVerticalStrut(5));
    panel.add(lblDates);
    panel.add(Box.createVerticalStrut(5));
    panel.add(lblStage);

    return panel;
}

    private JPanel createActionPanel(Runnable onEdit, Runnable onDelete) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton btnEdit = createButton("Cập nhật", new Color(52, 152, 219));
        JButton btnDelete = createButton("Xóa bỏ", new Color(231, 76, 60));

        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEdit.setMaximumSize(new Dimension(120, 38));
        btnDelete.setMaximumSize(new Dimension(120, 38));

        btnEdit.addActionListener(e -> { if (onEdit != null) onEdit.run(); });
        btnDelete.addActionListener(e -> { if (onDelete != null) onDelete.run(); });

        panel.add(Box.createVerticalGlue());
        panel.add(btnEdit);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnDelete);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        return btn;
    }
}
