package ui.doctor.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StatCard extends JPanel {

    public StatCard(
            String title,
            JLabel valueLabel,
            Color accentColor
    ) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Bo góc nhẹ bằng viền trắng + padding
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 235),
                        1,
                        true
                ),
                new EmptyBorder(18, 20, 18, 20)
        ));

        // Kích thước đẹp cho dashboard
        setPreferredSize(new Dimension(260, 120));

        // Thanh màu phía trên
        JPanel topBar = new JPanel();
        topBar.setBackground(accentColor);
        topBar.setPreferredSize(new Dimension(0, 6));

        // Nội dung card
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(
                contentPanel,
                BoxLayout.Y_AXIS
        ));

        // Tiêu đề
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );
        lblTitle.setForeground(new Color(120, 120, 120));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Giá trị
        valueLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );
        valueLabel.setForeground(new Color(33, 37, 41));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Khoảng cách
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(valueLabel);

        // Thêm vào panel chính
        add(topBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}