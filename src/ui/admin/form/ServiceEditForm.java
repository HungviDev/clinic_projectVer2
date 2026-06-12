package ui.admin.form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.admin.ServiceController;
import model.admin.ServiceModel;
import ui.admin.ServicesView;

import java.awt.*;

public class ServiceEditForm extends JDialog {

    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtPrice;
    private JTextField txtImage;

    private JButton btnSave;
    private JButton btnCancel;

    private ServiceController serviceController;
    private int serviceId;

    // Màu sắc chủ đạo
    private final Color MAIN_BG = new Color(240, 248, 255); // Xanh dương nhạt
    private final Color PANEL_BG = Color.WHITE;
    private final Color PRIMARY = new Color(0, 153, 255); // Xanh lam chủ đạo
    private final Color PRIMARY_DARK = new Color(0, 102, 204);
    private final Color BORDER_COLOR = new Color(153, 204, 255); // Viền xanh nhạt
    private final Color DANGER = new Color(255, 77, 77);
    private final Color DANGER_DARK = new Color(204, 0, 0);
    private final Color TEXT_COLOR = new Color(30, 40, 50);
    private ServicesView serviceView;
    public ServiceEditForm(JFrame parent, ServicesView serviceView, int serviceId) {
        super(parent,
                serviceId == 0 ? "THÊM DỊCH VỤ" : "SỬA DỊCH VỤ",
                true);
        this.serviceView = serviceView;
        this.serviceId = serviceId;
        this.serviceController = new ServiceController();

        initUI();

        if (serviceId > 0) {
            loadServiceData();
        }
    }

    private void initUI() {

        setSize(650, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAIN_BG);

        // ===== TITLE =====
        JLabel lblTitle = new JLabel(
                serviceId == 0 ? "THÊM DỊCH VỤ" : "SỬA DỊCH VỤ",
                SwingConstants.CENTER);

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(PRIMARY_DARK);
        lblTitle.setBorder(new EmptyBorder(25, 10, 25, 10));

        add(lblTitle, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(MAIN_BG);
        wrapperPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                new EmptyBorder(25, 25, 25, 25)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = createTextField();
        txtPrice = createTextField();
        txtImage = createTextField();

        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDescription = new JScrollPane(txtDescription);
        scrollDescription.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        // Tên dịch vụ
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Tên dịch vụ"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtName, gbc);

        // Mô tả
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        formPanel.add(createLabel("Mô tả"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(scrollDescription, gbc);

        // Giá
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Giá dịch vụ"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtPrice, gbc);

        // Ảnh
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Đường dẫn ảnh"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtImage, gbc);

        wrapperPanel.add(formPanel, BorderLayout.CENTER);
        add(wrapperPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                20,
                15));

        buttonPanel.setBackground(MAIN_BG);

        btnSave = createButton("Lưu", PRIMARY);
        btnCancel = createButton("Hủy", DANGER);

        addHoverEffect(btnSave, PRIMARY, PRIMARY_DARK);
        addHoverEffect(btnCancel, DANGER, DANGER_DARK);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveService());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadServiceData() {

        ServiceModel service =
                serviceController.getServiceById(serviceId);

        if (service != null) {

            txtName.setText(service.getName());
            txtDescription.setText(service.getDescription());
            txtPrice.setText(String.valueOf(service.getPrice()));
            txtImage.setText(service.getImage());

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy dịch vụ");

            dispose();
        }
    }

    private void saveService() {

        try {

            String name = txtName.getText().trim();
            String description = txtDescription.getText().trim();
            String image = txtImage.getText().trim();

            if (name.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Tên dịch vụ không được để trống");

                return;
            }

            double price;

            try {

                price = Double.parseDouble(
                        txtPrice.getText().trim());

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Giá phải là số");

                return;
            }

            ServiceModel service = new ServiceModel();

            service.setName(name);
            service.setDescription(description);
            service.setPrice(price);
            service.setImage(image);

            boolean result;

            if (serviceId == 0) {

                result =serviceController.insertService(service);

            } else {
                service.setId(serviceId);
                result =
                        serviceController.updateService(service);
            }

            if (result) {

                JOptionPane.showMessageDialog(
                        this,
                        serviceId == 0
                                ? "Thêm dịch vụ thành công!"
                                : "Cập nhật dịch vụ thành công!");
                serviceView.loadAllService();
                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Thao tác thất bại!");
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi: " + e.getMessage());
        }
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 15));

        label.setForeground(TEXT_COLOR);

        return label;
    }

    private JTextField createTextField() {

        JTextField txt = new JTextField();

        txt.setFont(
                new Font("Segoe UI", Font.PLAIN, 14));

        txt.setPreferredSize(
                new Dimension(250, 38));

        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        return txt;
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 15));

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(
                new Dimension(140, 42));

        button.setBorder(BorderFactory.createEmptyBorder());

        return button;
    }

    private void addHoverEffect(
            JButton button,
            Color normal,
            Color hover) {

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent evt) {

                        button.setBackground(hover);
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent evt) {

                        button.setBackground(normal);
                    }
                });
    }
}