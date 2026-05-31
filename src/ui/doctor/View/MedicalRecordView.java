
package ui.doctor.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.doctor.Controller.MedicalRecordController;
import ui.doctor.Model.MedicalRecordModel;


public class MedicalRecordView extends JPanel {

    private MedicalRecordController controller;
    private JPanel cardsContainer;
    private JScrollPane scrollPane;
    private JButton btnAdd;
    private JButton btnRefresh;
    private static MedicalRecordView instance; //day
    public MedicalRecordView() {
        this(null);
    }

    public MedicalRecordView(Integer doctorUserId) {
        // Khởi tạo Controller với ID cụ thể của bác sĩ vừa đăng nhập

        instance = this; //day
        controller = new MedicalRecordController(doctorUserId);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 248, 252));

        add(createHeader(), BorderLayout.NORTH);

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);
        cardsContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(245, 248, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Nạp dữ liệu lần đầu khi mở giao diện
        loadRecords();

        // ======================================================================
        // 🌟 TỰ ĐỘNG LÀM MỚI TẠI ĐÂY: Nhận tín hiệu là tự gọi hàm loadRecords() luôn!
        // ======================================================================
        this.addPropertyChangeListener("REFRESH_MEDICAL_RECORDS", new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                // Gọi trực tiếp hàm loadRecords để nạp lại dữ liệu, không cần click qua nút nữa
                loadRecords(); 
            }
        });
    }

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

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnAdd = createActionButton("Thêm hồ sơ mới", new Color(46, 204, 113));
        btnRefresh = createActionButton("Làm mới danh sách", new Color(52, 152, 219));

        panel.add(btnAdd);
        panel.add(btnRefresh);

        btnAdd.addActionListener(e -> {
            MedicalRecordDialog dialog = new MedicalRecordDialog(
                    SwingUtilities.getWindowAncestor(this),
                    controller,
                    null
            );
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadRecords();
            }
        });

        btnRefresh.addActionListener(e -> loadRecords());
        return panel;
    }

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

    private void loadRecords() {
        cardsContainer.removeAll();
        List<MedicalRecordModel> records = controller.getAllRecords();

        if (records == null || records.isEmpty()) {
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

    private JPanel createRecordCard(MedicalRecordModel record) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // AVATAR
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(100, 100));

        JLabel lblAvatar = new JLabel("👤", SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(235, 245, 255));
        lblAvatar.setForeground(new Color(52, 152, 219));
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 230), 1, true));
        avatarPanel.add(lblAvatar, BorderLayout.CENTER);

        // THÔNG TIN BỆNH ÁN
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel("Bệnh nhân: " + record.getPatientName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(new Color(44, 62, 80));

        JLabel lblDisease = new JLabel("Bệnh lý: " + record.getDisease());
        lblDisease.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblStartDate = new JLabel("Ngày bắt đầu: " + record.getStartDate());
        lblStartDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStartDate.setForeground(new Color(120, 120, 120));

        // 🌟 THÊM DÒNG NÀY: Hiển thị Tên Lộ trình
        JLabel lblRoute = new JLabel("Lộ trình điều trị: " + record.getRouteName());
        lblRoute.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRoute.setForeground(new Color(41, 128, 185)); // Màu xanh dương

        // 🌟 THÊM DÒNG NÀY: Hiển thị Giai đoạn hiện tại
        JLabel lblStage = new JLabel("Giai đoạn hiện tại: " + record.getCurrentStage());
        lblStage.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStage.setForeground(new Color(39, 174, 96)); // Màu xanh lá

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblDisease);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblStartDate);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblRoute); // Đưa Lộ trình vào
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblStage); // Đưa Giai đoạn vào ngay bên dưới
        infoPanel.add(Box.createVerticalGlue());

        // THAO TÁC (CẬP NHẬT / XÓA)
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        JButton btnEdit = createActionButton("Cập nhật", new Color(52, 152, 219));
        JButton btnDelete = createActionButton("Xóa hồ sơ", new Color(231, 76, 60));

        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEdit.setMaximumSize(new Dimension(120, 36));
        btnDelete.setMaximumSize(new Dimension(120, 36));

        actionPanel.add(Box.createVerticalGlue());
        actionPanel.add(btnEdit);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalGlue());

        btnEdit.addActionListener(e -> {
            MedicalRecordDialog dialog = new MedicalRecordDialog(
                    SwingUtilities.getWindowAncestor(this), controller, record);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadRecords();
            }
        });

        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa hồ sơ của bệnh nhân [" + record.getPatientName() + "] không?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.deleteRecord(record.getId())) {
                    JOptionPane.showMessageDialog(this, "Đã xóa hồ sơ thành công!");
                    loadRecords();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa hồ sơ thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        card.add(avatarPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }
    public static void refreshData() {
        if (instance != null) {
            instance.loadRecords();
        }
    }
}