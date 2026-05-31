package ui.doctor.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.doctor.Controller.PatientController;
import ui.doctor.Model.PatientModel;

public class PatientView extends JPanel {
    private JPanel containerPanel;
    private PatientController patientController;
    private int doctorUserId;

    public PatientView(int doctorUserId) {
        this.doctorUserId = doctorUserId;
        this.patientController = new PatientController();
        
        setLayout(new BorderLayout());
        
        // Container chính xếp dọc (BoxLayout)
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(new Color(245, 246, 247)); // Màu nền xám nhạt
        
        // ScrollPane để cuộn danh sách
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn
        add(scrollPane, BorderLayout.CENTER);
        
        loadPatientData();
    }

    public void loadPatientData() {
        containerPanel.removeAll();
        List<PatientModel> list = patientController.getPatientsByDoctor(doctorUserId);
        
        for (PatientModel p : list) {
            containerPanel.add(createPatientCard(p));
            containerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Khoảng cách giữa các thẻ
        }
        
        containerPanel.revalidate();
        containerPanel.repaint();
    }

    // Tạo Card chuẩn phong cách StatCard
    private JPanel createPatientCard(PatientModel p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 135));
        
        // Viền và bo góc chuẩn
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // 1. Thanh màu nhấn phía trên
        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(52, 152, 219)); // Màu xanh chủ đạo
        topBar.setPreferredSize(new Dimension(0, 6));
        card.add(topBar, BorderLayout.NORTH);

        // 2. Panel nội dung chính
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(12, 20, 12, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Fonts
        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 13);

        // Tên
        JLabel lblName = new JLabel("Bệnh nhân: " + (p.getFullName() != null ? p.getFullName() : "N/A"));
        lblName.setFont(titleFont);
        lblName.setForeground(new Color(33, 37, 41));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Chi tiết
        JLabel lblDetails = new JLabel("<html>SĐT: " + (p.getPhone() != null ? p.getPhone() : "---") + 
                                       " &nbsp;&nbsp;|&nbsp;&nbsp; Email: " + (p.getEmail() != null ? p.getEmail() : "---") + 
                                       "<br>Ngày sinh: " + (p.getBirthDate() != null ? p.getBirthDate() : "---") + 
                                       " &nbsp;&nbsp;|&nbsp;&nbsp; Địa chỉ: " + (p.getAddress() != null ? p.getAddress() : "---") + "</html>");
        lblDetails.setFont(textFont);
        lblDetails.setForeground(new Color(120, 120, 120));
        lblDetails.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Vấn đề
        JLabel lblProblem = new JLabel("Vấn đề: " + (p.getTreatmentProblem() != null ? p.getTreatmentProblem() : "Chưa xác định"));
        lblProblem.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblProblem.setForeground(new Color(231, 76, 60)); // Đỏ nhẹ
        lblProblem.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblName);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(lblDetails);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(lblProblem);

        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
}