package ui.doctor.View;

import ui.doctor.Model.PatientModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PatientCard extends JPanel {
    public PatientCard(PatientModel p) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Bo góc nhẹ bằng viền + padding giống StatCard
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Thanh màu nhấn phía trên (Accent Color)
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(52, 152, 219)); // Màu xanh dương chuyên nghiệp
        topBar.setPreferredSize(new Dimension(0, 5));
        add(topBar, BorderLayout.NORTH);

        // Panel nội dung chính
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Font chữ
        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color textColor = new Color(70, 70, 70);

        // Row 1: Tên bệnh nhân
        JLabel lblName = new JLabel("Bệnh nhân: " + (p.getFullName() != null ? p.getFullName() : "N/A"));
        lblName.setFont(titleFont);
        lblName.setForeground(new Color(33, 37, 41));
        
        // Row 2: Thông tin chi tiết
        JLabel lblDetails = new JLabel("<html>SĐT: " + (p.getPhone() != null ? p.getPhone() : "---") + 
                                       " | Email: " + (p.getEmail() != null ? p.getEmail() : "---") + 
                                       "<br>Ngày sinh: " + (p.getBirthDate() != null ? p.getBirthDate() : "---") + 
                                       " | Địa chỉ: " + (p.getAddress() != null ? p.getAddress() : "---") + "</html>");
        lblDetails.setFont(textFont);
        lblDetails.setForeground(textColor);

        // Row 3: Vấn đề điều trị
        JLabel lblProblem = new JLabel("Vấn đề: " + (p.getTreatmentProblem() != null ? p.getTreatmentProblem() : "Chưa xác định"));
        lblProblem.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblProblem.setForeground(new Color(231, 76, 60)); // Màu nhấn đỏ nhẹ cho vấn đề

        contentPanel.add(lblName);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblDetails);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblProblem);

        add(contentPanel, BorderLayout.CENTER);
    }
}