package ui.admin;

import controller.admin.ServiceController;
import model.admin.ServiceModel;
import ui.admin.form.ServiceEditForm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class ServicesView extends JPanel {

    // =====================================
    // COLOR
    // =====================================
    private final Color BACKGROUND_COLOR =
            new Color(240, 245, 250);

    private final Color PRIMARY_COLOR =
            new Color(0, 102, 204);

    private final Color SUCCESS_COLOR =
            new Color(46, 204, 113);

    private final Color WARNING_COLOR =
            new Color(241, 196, 15);

    private final Color DANGER_COLOR =
            new Color(231, 76, 60);

    // =====================================
    // TABLE
    // =====================================
    private JTable table;

    private DefaultTableModel model;

    // =====================================
    // CONTROLLER
    // =====================================
    private ServiceController serviceController = new ServiceController();
    private int idService;    
    // =====================================
    // CONSTRUCTOR
    // =====================================
    public ServicesView() {

        setLayout(new BorderLayout());

        setBackground(BACKGROUND_COLOR);

        // =====================================
        // TOP PANEL
        // =====================================
        JPanel topPanel =
                new JPanel(new BorderLayout());

        topPanel.setBackground(BACKGROUND_COLOR);

        topPanel.setBorder(
                new EmptyBorder(20, 20, 10, 20)
        );

        // =====================================
        // TITLE
        // =====================================
        JLabel lblTitle =
                new JLabel("QUẢN LÝ DỊCH VỤ");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        lblTitle.setForeground(PRIMARY_COLOR);

        topPanel.add(lblTitle, BorderLayout.WEST);

        // =====================================
        // BUTTON PANEL
        // =====================================
        JPanel buttonPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT,
                        10,
                        0
                ));

        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnAdd =
                createButton(
                        "Thêm",
                        SUCCESS_COLOR
                );

        JButton btnUpdate =
                createButton(
                        "Sửa",
                        PRIMARY_COLOR
                );

        JButton btnDelete =
                createButton(
                        "Xóa",
                        DANGER_COLOR
                );

 
        buttonPanel.add(btnAdd);

        buttonPanel.add(btnUpdate);

        buttonPanel.add(btnDelete);

        btnAdd.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ServiceEditForm form = new ServiceEditForm(parentFrame, this, 0); // 0 = Thêm mới
            form.setVisible(true);
        });
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để sửa");
                return;
            }
            idService = Integer.parseInt(table.getValueAt(row, 0).toString());
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ServiceEditForm form = new ServiceEditForm(parentFrame, this, idService);
            form.setVisible(true);
        });
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để xóa");
                return;
            }
            
            idService = Integer.parseInt(table.getValueAt(row, 0).toString());
            String name = table.getValueAt(row, 1).toString();
            
            int confirm = JOptionPane.showConfirmDialog(
                    this, 
                    "Bạn có chắc chắn muốn xóa dịch vụ: " + name + "?", 
                    "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean result = serviceController.deleteService(idService);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
                    loadAllService();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa dịch vụ thất bại!");
                }
            }
        });
  

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // =====================================
        // TABLE
        // =====================================
        String[] columns = {

                "ID",

                "Tên dịch vụ",

                "Mô tả",

                "Giá (VNĐ)",

                "Hình ảnh"
        };

        model =
                new DefaultTableModel(columns, 0);

        table = new JTable(model);

        // =====================================
        // TABLE STYLE
        // =====================================
        table.setRowHeight(38);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        table.setSelectionBackground(
                new Color(184, 207, 229)
        );

        table.setGridColor(
                new Color(220, 220, 220)
        );

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        table.getTableHeader().setBackground(
                PRIMARY_COLOR
        );

        table.getTableHeader().setForeground(
                Color.WHITE
        );

        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                new EmptyBorder(10, 20, 20, 20)
        );

        // =====================================
        // LOAD DATABASE
        // =====================================
        loadAllService();

        // =====================================
        // ADD COMPONENT
        // =====================================
        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
    }

    // =====================================
    // BUTTON UI
    // =====================================
    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(text);

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setPreferredSize(
                new Dimension(120, 42)
        );

        return button;
    }

    public void loadAllService() {

        try {

            model.setRowCount(0);

            List<ServiceModel> serviceList =
                    serviceController.getAllService();

            serviceList.forEach(service -> {

                model.addRow(new Object[]{

                        service.getId(),

                        service.getName(),

                        service.getDescription(),

                        service.getPrice(),

                        service.getImage()
                });
            });

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi load dữ liệu"
            );
        }
    }
}