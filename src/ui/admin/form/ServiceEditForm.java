package ui.admin.form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.admin.ServiceController;
import model.admin.ServiceModel;
// import ui.admin.ServiceView;

import java.awt.*;

public class ServiceEditForm extends JDialog {

    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtPrice;
    private JTextField txtImage;

    private JButton btnSave;
    private JButton btnCancel;

    private ServiceController serviceController;
    private ServiceModel serviceView;
    private int serviceId;

    public ServiceEditForm(JFrame parent, ServiceModel serviceView, int serviceId) {
        super(parent, "SỬA DỊCH VỤ", true);

        this.serviceView = serviceView;
        this.serviceId = serviceId;
        this.serviceController = new ServiceController();

        setSize(550, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(220, 235, 250));

        JLabel lblTitle = new JLabel("SỬA DỊCH VỤ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setBorder(new EmptyBorder(20, 10, 20, 10));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(220, 235, 250));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = new JTextField();
        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        txtPrice = new JTextField();
        txtImage = new JTextField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Tên dịch vụ"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Mô tả"), gbc);

        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtDescription), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Giá"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("Ảnh"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtImage, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(220, 235, 250));

        btnSave = createButton("Lưu");
        btnCancel = createButton("Hủy");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> updateService());
        btnCancel.addActionListener(e -> dispose());

        loadServiceData();
    }

    private void loadServiceData() {
        ServiceModel service = serviceController.getServiceById(serviceId);

        if (service != null) {
            txtName.setText(service.getName());
            txtDescription.setText(service.getDescription());
            txtPrice.setText(String.valueOf(service.getPrice()));
            txtImage.setText(service.getImage());
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dịch vụ");
            dispose();
        }
    }

    private void updateService() {
        try {
            String name = txtName.getText().trim();
            String description = txtDescription.getText().trim();
            String priceText = txtPrice.getText().trim();
            String image = txtImage.getText().trim();

            if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            double price;

            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số");
                return;
            }

            ServiceModel service = new ServiceModel();
            service.setId(serviceId);
            service.setName(name);
            service.setDescription(description);
            service.setPrice(price);
            service.setImage(image);

            boolean result = serviceController.updateService(service);

            if (result) {
                JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công");

                // if (serviceView != null) {
                //     serviceView.loadAllService();
                // }

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thất bại");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(0, 51, 102));
        return label;
    }

    private JLabel createLabel(String text, GridBagConstraints gbc) {
        return createLabel(text);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}