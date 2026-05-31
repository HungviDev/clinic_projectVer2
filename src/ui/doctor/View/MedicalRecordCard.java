package ui.doctor.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.doctor.Model.MedicalRecordModel;

public class MedicalRecordCard extends JPanel {

    public MedicalRecordCard(MedicalRecordModel record, Runnable onEdit, Runnable onDelete) {
        setLayout(new BorderLayout(20, 0));
        setBackground(Color.WHITE);
        // Giảm chiều cao xuống vì đã bớt thông tin
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 180)); 

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        add(createAvatarPanel(), BorderLayout.WEST);
        add(createInfoPanel(record), BorderLayout.CENTER);
        add(createActionPanel(onEdit, onDelete), BorderLayout.EAST);
    }

    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(90, 90));

        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
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

        JLabel lblName = new JLabel("Bệnh nhân: " + record.getPatientName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(new Color(44, 62, 80));

        JLabel lblDisease = new JLabel("Bệnh lý: " + record.getDisease());
        lblDisease.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblStartDate = new JLabel("Ngày bắt đầu: " + record.getStartDate());
        lblStartDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStartDate.setForeground(new Color(100, 100, 100));

        JLabel lblStage = new JLabel("Lộ trình/Giai đoạn: " + record.getCurrentStage());
        lblStage.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStage.setForeground(new Color(39, 174, 96)); // Màu xanh lá cho lộ trình

        panel.add(Box.createVerticalGlue());
        panel.add(lblName);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblDisease);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblStartDate);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblStage);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createActionPanel(Runnable onEdit, Runnable onDelete) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton btnEdit = createButton("Cập nhật", new Color(52, 152, 219));
        JButton btnDelete = createButton("Xóa bỏ", new Color(231, 76, 60));

        // Căn giữa nút
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setMaximumSize(new Dimension(100, 35));
        return btn;
    }
}