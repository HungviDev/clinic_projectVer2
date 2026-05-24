
package ui.doctor.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.doctor.Controller.MedicalRecordController;
import ui.doctor.Model.MedicalRecordModel;

public class MedicalRecordView extends JPanel {

    // =====================================================
    // CONTROLLER
    // =====================================================
    private MedicalRecordController controller;

    // =====================================================
    // UI COMPONENTS
    // =====================================================
    private JPanel cardsContainer;
    private JScrollPane scrollPane;

    private JButton btnAdd;
    private JButton btnRefresh;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public MedicalRecordView() {
        this(null);
    }

    public MedicalRecordView(Integer doctorUserId) {
        controller = new MedicalRecordController(doctorUserId);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 248, 252));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Content
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);
        cardsContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(245, 248, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Bottom actions
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Load data đầu vào từ CSDL thật
        loadRecords();
    }

    // =====================================================
    // HEADER
    // =====================================================
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("HỒ SƠ BỆNH ÁN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(new Color(0, 51, 102));

        JLabel lblSub = new JLabel("Quản lý tiến trình điều trị bệnh nhân từ cơ sở dữ liệu");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(120, 120, 120));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(lblTitle);
        left.add(Box.createVerticalStrut(5));
        left.add(lblSub);

        panel.add(left, BorderLayout.WEST);

        return panel;
    }

    // =====================================================
    // BOTTOM PANEL
    // =====================================================
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnAdd = createActionButton("Thêm hồ sơ mới", new Color(46, 204, 113));
        btnRefresh = createActionButton("Làm mới danh sách", new Color(52, 152, 219));

        panel.add(btnAdd);
        panel.add(btnRefresh);

        // Hành động: Thêm mới hồ sơ bệnh án
        btnAdd.addActionListener(e -> {
            // Chuẩn bị một Model trống và tự động lấy ID tiếp theo từ CSDL
            MedicalRecordModel newRecord = new MedicalRecordModel();
            newRecord.setId(controller.getNextId()); 

            MedicalRecordDialog dialog = new MedicalRecordDialog(
                    SwingUtilities.getWindowAncestor(this),
                    controller,
                    newRecord
            );

            dialog.setVisible(true);

            // Nếu Dialog báo đã lưu thành công vào CSDL -> Cập nhật lại UI
            if (dialog.isSaved()) {
                loadRecords();
            }
        });

        // Hành động: Làm mới thủ công bằng nút bấm
        btnRefresh.addActionListener(e -> loadRecords());

        return panel;
    }

    // =====================================================
    // ACTION BUTTON (Định dạng nút bấm đồng bộ hệ thống)
    // =====================================================
    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // =====================================================
    // LOAD RECORDS (Đọc dữ liệu thật đổ lên giao diện)
    // =====================================================
    private void loadRecords() {
        cardsContainer.removeAll();

        // Lấy danh sách hồ sơ từ tầng điều khiển kết nối CSDL
        List<MedicalRecordModel> records = controller.getAllRecords();

        if (records == null || records.isEmpty()) {
            // Hiển thị thông báo trực quan nếu cơ sở dữ liệu trống
            JLabel lblEmpty = new JLabel("Không có hồ sơ bệnh án nào trong hệ thống.", SwingConstants.CENTER);
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardsContainer.add(Box.createVerticalStrut(50));
            cardsContainer.add(lblEmpty);
        } else {
            for (MedicalRecordModel record : records) {
                cardsContainer.add(createRecordCard(record));
                cardsContainer.add(Box.createVerticalStrut(15));
            }
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    // =====================================================
    // CREATE CARD (Thẻ thông tin chi tiết từng bệnh nhân)
    // =====================================================
    private JPanel createRecordCard(MedicalRecordModel record) {

        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // ==============================================
        // AVATAR
        // ==============================================
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(120, 120));

        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(235, 245, 255));
        lblAvatar.setForeground(new Color(52, 152, 219));
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 230), 1, true));

        avatarPanel.add(lblAvatar, BorderLayout.CENTER);

        // ==============================================
        // THÔNG TIN BỆNH ÁN
        // ==============================================
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(record.getPatientName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblName.setForeground(new Color(33, 37, 41));

        JLabel lblDisease = new JLabel("Bệnh lý: " + record.getDisease());
        lblDisease.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblDuration = new JLabel("Thời gian điều trị: " + record.getTreatmentDurationDays() + " ngày");
        lblDuration.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblDates = new JLabel("Thời hạn: " + record.getStartDate() + "  →  " + record.getEndDate());
        lblDates.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblStage = new JLabel("Giai đoạn hiện tại: " + record.getCurrentStage());
        lblStage.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblStage.setForeground(new Color(41, 128, 185));

        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblDisease);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblDuration);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblDates);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblStage);

        // ==============================================
        // THAO TÁC (CẬP NHẬT / XÓA)
        // ==============================================
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        JButton btnEdit = createActionButton("Cập nhật", new Color(52, 152, 219));
        JButton btnDelete = createActionButton("Xóa hồ sơ", new Color(231, 76, 60));

        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEdit.setMaximumSize(new Dimension(130, 40));
        btnDelete.setMaximumSize(new Dimension(130, 40));

        actionPanel.add(Box.createVerticalGlue());
        actionPanel.add(btnEdit);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalGlue());

        // Hành động: Cập nhật thông tin bản ghi dữ liệu hiện tại
        btnEdit.addActionListener(e -> {
            MedicalRecordDialog dialog = new MedicalRecordDialog(
                    SwingUtilities.getWindowAncestor(this),
                    controller,
                    record
            );

            dialog.setVisible(true);

            if (dialog.isSaved()) {
                loadRecords();
            }
        });

        // Hành động: Xóa bản ghi có kiểm soát dữ liệu
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa hồ sơ của bệnh nhân [" + record.getPatientName() + "] không?\nDữ liệu sẽ bị xóa vĩnh viễn khỏi hệ thống.",
                    "Xác nhận xóa hồ sơ bệnh án",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean isDeleted = controller.deleteRecord(record.getId());
                if (isDeleted) {
                    JOptionPane.showMessageDialog(this, "Đã xóa hồ sơ thành công!");
                    loadRecords(); // Chỉ tải lại danh sách khi xóa thành công trong CSDL
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa hồ sơ thất bại! Vui lòng kiểm tra lại kết nối CSDL.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Tích hợp các thành phần vào thẻ Card
        card.add(avatarPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }
}