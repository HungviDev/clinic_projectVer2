package ui.patient;

import config.DBConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TreatmentProgressDialog extends JDialog {

    private final Color COLOR_COMPLETED = new Color(46, 204, 113); // Xanh lá
    private final Color COLOR_IN_PROGRESS = new Color(243, 156, 18); // Cam
    private final Color COLOR_PENDING = new Color(189, 195, 199); // Xám nhạt
    private final Color TEXT_DARK = new Color(44, 62, 80);

    public TreatmentProgressDialog(JFrame parent, int treatmentRouteId, String planName) {
        super(parent, "Tiến độ điều trị: " + (planName != null ? planName : ""), true);
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Tiêu đề
        JLabel lblTitle = new JLabel("LỘ TRÌNH ĐIỀU TRỊ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(93, 173, 226));
        lblTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Container chứa danh sách các giai đoạn
        JPanel timelinePanel = new JPanel();
        timelinePanel.setLayout(new BoxLayout(timelinePanel, BoxLayout.Y_AXIS));
        timelinePanel.setBackground(Color.WHITE);
        timelinePanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Lấy dữ liệu từ DB và vẽ lên giao diện
        loadStagesFromDB(timelinePanel, treatmentRouteId);

        JScrollPane scrollPane = new JScrollPane(timelinePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Nút Đóng
        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(new Color(236, 240, 241));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // GỌI SQL ĐỂ LẤY CÁC GIAI ĐOẠN CỦA LỘ TRÌNH
    private void loadStagesFromDB(JPanel container, int routeId) {
        
        // Đã thêm cột appointment_date vào câu SQL
        String sql = "SELECT stage_name, status, sequence_order, appointment_date FROM treatment_stages WHERE treatment_route_id = ? ORDER BY sequence_order ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, routeId);
            ResultSet rs = ps.executeQuery();
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String stageName = rs.getString("stage_name");
                String status = rs.getString("status");
                
                // --- LẤY VÀ ĐỊNH DẠNG NGÀY HẸN ---
                java.sql.Timestamp apptDate = rs.getTimestamp("appointment_date");
                String dateStr = "";
                if (apptDate != null) {
                    dateStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(apptDate);
                }
                
                // Truyền thêm dateStr vào hàm tạo UI
                container.add(createTimelineItem(stageName, status, dateStr));
            }

            if (!hasData) {
                JLabel empty = new JLabel("Lộ trình này chưa có giai đoạn nào được lên kế hoạch.");
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                empty.setForeground(Color.GRAY);
                container.add(empty);
            }

            container.revalidate();
            container.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // THIẾT KẾ GIAO DIỆN CHO TỪNG BƯỚC (TIMELINE ITEM)
// Đã thêm tham số dateStr
    private JPanel createTimelineItem(String stageName, String status, String dateStr) {
        JPanel itemPanel = new JPanel(new BorderLayout(15, 0));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setMaximumSize(new Dimension(400, 70));
        itemPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Xác định màu sắc và Icon
        Color statusColor;
        String iconChar;
        
        if (status.equalsIgnoreCase("Đã hoàn thành")) {
            statusColor = COLOR_COMPLETED;
            iconChar = "●"; 
        } else if (status.equalsIgnoreCase("Đang thực hiện")) {
            statusColor = COLOR_IN_PROGRESS;
            iconChar = "◐"; 
        } else {
            statusColor = COLOR_PENDING;
            iconChar = "○"; 
        }

        // Cột bên trái: Icon
        JLabel lblIcon = new JLabel(iconChar);
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblIcon.setForeground(statusColor);
        itemPanel.add(lblIcon, BorderLayout.WEST);

        // Cột giữa: Tên & Trạng thái
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel lblName = new JLabel(stageName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblName.setForeground(TEXT_DARK);

        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblStatus.setForeground(statusColor);

        textPanel.add(lblName);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(lblStatus);
        
        itemPanel.add(textPanel, BorderLayout.CENTER);

        // --- CỘT BÊN PHẢI: NGÀY HẸN ---
        if (!dateStr.isEmpty()) {
            JLabel lblDate = new JLabel(dateStr);
            lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDate.setForeground(Color.GRAY);
            
            // Căn giữa ngày theo chiều dọc
            JPanel rightPanel = new JPanel(new GridBagLayout());
            rightPanel.setBackground(Color.WHITE);
            rightPanel.add(lblDate);
            
            itemPanel.add(rightPanel, BorderLayout.EAST);
        }

        // Viền dưới mờ mờ tạo sự phân cách
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
            new EmptyBorder(10, 5, 10, 5)
        ));

        return itemPanel;
    }
}