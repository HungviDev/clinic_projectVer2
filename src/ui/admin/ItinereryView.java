package ui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.admin.RoadMapController;
import controller.admin.StepRoadMapController;
import model.admin.RoadmapModel;
import model.admin.StepRoadMapModel;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ItinereryView extends JPanel {

    /*
     * =========================================
     * COLOR
     * =========================================
     */

    private final Color BACKGROUND_COLOR =
            new Color(245, 247, 250);

    private final Color PRIMARY_COLOR =
            new Color(25, 118, 210);

    private final Color SUCCESS_COLOR =
            new Color(46, 204, 113);

    private final Color DANGER_COLOR =
            new Color(231, 76, 60);

    private final Color WARNING_COLOR =
            new Color(255, 167, 38);

    /*
     * =========================================
     * TABLE
     * =========================================
     */

    private JTable tableItinerary;

    private JTable tableStep;

    private DefaultTableModel itineraryModel;

    private DefaultTableModel stepModel;

    /*
     * =========================================
     * DATA
     * =========================================
     */

    private final HashMap<Integer, ArrayList<Object[]>> itinerarySteps =
            new HashMap<>();
     RoadMapController roadmapController = new RoadMapController();

    /*
     * =========================================
     * CONSTRUCTOR
     * =========================================
     */

    public ItinereryView() {

        setLayout(new BorderLayout());

        setBackground(BACKGROUND_COLOR);

        initUI();

        loadData();

        initAction();
    }

    /*
     * =========================================
     * UI
     * =========================================
     */

    private void initUI() {

        /*
         * =========================================
         * HEADER
         * =========================================
         */

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(BACKGROUND_COLOR);

        header.setBorder(
                new EmptyBorder(20, 25, 20, 25)
        );

        JLabel title =
                new JLabel("Quản lý lộ trình điều trị");

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnAddItinerary =
                createButton(
                        "Thêm lộ trình",
                        SUCCESS_COLOR
                );

        JButton btnAddStep =
                createButton(
                        "Thêm bước",
                        PRIMARY_COLOR
                );

        JButton btnDelete =
                createButton(
                        "Xóa",
                        DANGER_COLOR
                );

        buttonPanel.add(btnAddItinerary);

        buttonPanel.add(btnAddStep);

        buttonPanel.add(btnDelete);

        header.add(title, BorderLayout.WEST);

        header.add(buttonPanel, BorderLayout.EAST);

        /*
         * =========================================
         * MAIN
         * =========================================
         */

        JPanel mainPanel =
                new JPanel(new GridLayout(1, 2, 20, 0));

        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.setBorder(
                new EmptyBorder(0, 25, 25, 25)
        );

        /*
         * =========================================
         * LEFT PANEL
         * =========================================
         */

        JPanel leftPanel =
                createCardPanel(
                        "Danh sách lộ trình"
                );

        String[] itineraryColumns = {
                "ID",
                "Tên lộ trình",
                "Mô tả",
                "Ngày tạo"

        };

        itineraryModel =
        new DefaultTableModel(
                itineraryColumns,
                0
        );

        tableItinerary =
                new JTable(itineraryModel);

        // Style table
        styleTable(tableItinerary);

        // Cho phép scroll ngang
        tableItinerary.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        // Chiều cao row
        tableItinerary.setRowHeight(35);

        // Set width từng cột
        tableItinerary.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(80);

        tableItinerary.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(250);

        tableItinerary.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(450);

        tableItinerary.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(180);

        // ScrollPane
        JScrollPane scrollLeft =
                new JScrollPane(tableItinerary);

        // Scroll dọc luôn hiện
        scrollLeft.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );

        // Scroll ngang luôn hiện
        scrollLeft.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        // Background đẹp hơn
        scrollLeft.getViewport()
                .setBackground(Color.WHITE);

        // Border đẹp hơn
        scrollLeft.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220,220,220),
                        1
                )
        );

        leftPanel.add(scrollLeft, BorderLayout.CENTER);

        /*
         * =========================================
         * RIGHT PANEL
         * =========================================
         */

        JPanel rightPanel =
                createCardPanel(
                        "Các bước điều trị"
                );

        String[] stepColumns = {

                "STT",

                "Tên giai đoạn",

                "Thứ tự",

                "Khoảng cách",

                "Ngày hẹn",

                "Trạng thái",

                "Chi phí",

                "Ghi chú"
                };

        stepModel =
        new DefaultTableModel(
                stepColumns,
                0
        );

        tableStep =
                new JTable(stepModel);

        // Style table
        styleTable(tableStep);

        // Cho phép scroll ngang
        tableStep.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        // Chiều cao row
        tableStep.setRowHeight(35);

        // Set width từng cột
        tableStep.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        tableStep.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(250);

        tableStep.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(100);

        tableStep.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(120);

        tableStep.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(150);

        tableStep.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(130);

        tableStep.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);

        tableStep.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(300);

        // ScrollPane
        JScrollPane scrollRight =
                new JScrollPane(tableStep);

        // Scroll dọc luôn hiện
        scrollRight.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );

        // Scroll ngang luôn hiện
        scrollRight.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        // Background đẹp hơn
        scrollRight.getViewport()
                .setBackground(Color.WHITE);

        // Border đẹp hơn
        scrollRight.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220,220,220),
                        1
                )
        );

        rightPanel.add(
                scrollRight,
                BorderLayout.CENTER
        );

        /*
         * =========================================
         * ADD
         * =========================================
         */

        mainPanel.add(leftPanel);

        mainPanel.add(rightPanel);

        add(header, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);

        /*
         * =========================================
         * ACTION BUTTON
         * =========================================
         */

        btnAddItinerary.addActionListener(e -> {

    JTextField txtName = new JTextField(20);
    JTextField txtService = new JTextField(20);

    Font font = new Font("Segoe UI", Font.PLAIN, 14);

    txtName.setFont(font);
    txtService.setFont(font);

    Dimension fieldSize = new Dimension(250, 35);

    txtName.setPreferredSize(fieldSize);
    txtService.setPreferredSize(fieldSize);

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(Color.WHITE);
    panel.setBorder(
            BorderFactory.createEmptyBorder(
                    20,
                    25,
                    20,
                    25
            )
    );

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lblTitle =
            new JLabel("THÊM LỘ TRÌNH ĐIỀU TRỊ");

    lblTitle.setFont(
            new Font("Segoe UI", Font.BOLD, 18)
    );

    lblTitle.setForeground(
            new Color(33, 150, 243)
    );

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;

    panel.add(lblTitle, gbc);

    gbc.gridwidth = 1;

    // Tên lộ trình
    gbc.gridx = 0;
    gbc.gridy = 1;

    JLabel lblName =
            new JLabel("Tên lộ trình:");

    lblName.setFont(font);

    panel.add(lblName, gbc);

    gbc.gridx = 1;

    panel.add(txtName, gbc);

    // Dịch vụ
    gbc.gridx = 0;
    gbc.gridy = 2;

    JLabel lblService =
            new JLabel("Mô tả");

    lblService.setFont(font);

    panel.add(lblService, gbc);

    gbc.gridx = 1;

    panel.add(txtService, gbc);

    UIManager.put(
            "OptionPane.background",
            Color.WHITE
    );

    UIManager.put(
            "Panel.background",
            Color.WHITE
    );

    int result =
            JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Thêm lộ trình",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

    if (result == JOptionPane.OK_OPTION) {

        RoadmapModel roadmap =
                new RoadmapModel();

        roadmap.setTitle(
                txtName.getText()
        );

        roadmap.setDescription(
                txtService.getText()
        );

        // Gọi controller để insert database
        boolean isSuccess =
                roadmapController.insertRoadmap(roadmap);

        if (isSuccess) {
                loadData();
                JOptionPane.showMessageDialog(
                        null,
                        "Thêm lộ trình thành công!"
                );
        } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Thêm lộ trình thất bại!"
                );
        }
        }
});

        /*
         * =========================================
         * ADD STEP
         * =========================================
         */

        btnAddStep.addActionListener(e -> {

            int row =
                    tableItinerary.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        null,
                        "Hãy chọn lộ trình"
                );

                return;
            }

            int itineraryId =
                    Integer.parseInt(
                            itineraryModel
                                    .getValueAt(row, 0)
                                    .toString()
                    );

            JTextField txtStepName =
                    new JTextField();

            JTextField txtDescription =
                    new JTextField();

            JTextField txtTime =
                    new JTextField();

            JComboBox<String> cbStatus =
                    new JComboBox<>(
                            new String[]{

                                    "Chưa thực hiện",

                                    "Đang thực hiện",

                                    "Hoàn thành"
                            }
                    );

            JPanel panel =
                    new JPanel(
                            new GridLayout(
                                    0,
                                    1,
                                    10,
                                    10
                            )
                    );

            panel.add(new JLabel("Tên bước"));
            panel.add(txtStepName);

            panel.add(new JLabel("Mô tả"));
            panel.add(txtDescription);

            panel.add(new JLabel("Thời gian"));
            panel.add(txtTime);

            panel.add(new JLabel("Trạng thái"));
            panel.add(cbStatus);

            int result =
                    JOptionPane.showConfirmDialog(
                            null,
                            panel,
                            "Thêm bước điều trị",
                            JOptionPane.OK_CANCEL_OPTION
                    );

            if (result == JOptionPane.OK_OPTION) {

                Object[] step = {

                        stepModel.getRowCount() + 1,

                        txtStepName.getText(),

                        txtDescription.getText(),

                        txtTime.getText(),

                        cbStatus.getSelectedItem()
                };

                itinerarySteps
                        .get(itineraryId)
                        .add(step);

                loadSteps(itineraryId);
            }
        });

        /*
         * =========================================
         * DELETE
         * =========================================
         */

      btnDelete.addActionListener(e -> {

    int row =
            tableItinerary.getSelectedRow();

    if (row == -1) {

        JOptionPane.showMessageDialog(
                null,
                "Chọn lộ trình cần xóa"
        );

        return;
    }

    int confirm =
            JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc muốn xóa ?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

    if (confirm == JOptionPane.YES_OPTION) {

        int id =
                Integer.parseInt(
                        itineraryModel
                                .getValueAt(row, 0)
                                .toString()
                );

        boolean check =
                roadmapController.deleteRoadmap(id);

        if (check) {
            itineraryModel.removeRow(row);
            itinerarySteps.remove(id);
            stepModel.setRowCount(0);
            JOptionPane.showMessageDialog(
                    null,
                    "Xóa thành công"
            );
            loadData();
        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Xóa thất bại"
            );
        }
    }
});
    }

    /*
     * =========================================
     * ACTION
     * =========================================
     */

    private void initAction() {

        tableItinerary
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    int row =
                            tableItinerary.getSelectedRow();

                    if (row != -1) {

                        int itineraryId =
                                Integer.parseInt(
                                        itineraryModel
                                                .getValueAt(row, 0)
                                                .toString()
                                );

                        loadSteps(itineraryId);
                    }
                });
    }

    /*
     * =========================================
     * LOAD STEP
     * =========================================
     */

    private void loadSteps(int itineraryId) {

        stepModel.setRowCount(0);

        ArrayList<Object[]> steps =
                itinerarySteps.get(itineraryId);

        if (steps != null) {

            for (Object[] step : steps) {

                stepModel.addRow(step);
            }
        }
    }

    /*
     * =========================================
     * FAKE DATA
     * =========================================
     */

    private void fakeData() {

        itineraryModel.addRow(new Object[]{

                1,

                "Niềng mắc cài",

                "Niềng răng",

                "Dr. Minh"
        });

        ArrayList<Object[]> steps1 =
                new ArrayList<>();

        steps1.add(new Object[]{

                1,

                "Khám tổng quát",

                "Kiểm tra răng",

                "30 phút",

                "Hoàn thành"
        });

        steps1.add(new Object[]{

                2,

                "Chụp X-Ray",

                "Chụp toàn hàm",

                "20 phút",

                "Hoàn thành"
        });

        steps1.add(new Object[]{

                3,

                "Gắn mắc cài",

                "Tiến hành niềng",

                "2 giờ",

                "Đang thực hiện"
        });

        itinerarySteps.put(1, steps1);

        itineraryModel.addRow(new Object[]{

                2,

                "Implant Straumann",

                "Trồng Implant",

                "Dr. Hùng"
        });

        ArrayList<Object[]> steps2 =
                new ArrayList<>();

        steps2.add(new Object[]{

                1,

                "Khám",

                "Kiểm tra mất răng",

                "20 phút",

                "Hoàn thành"
        });

        steps2.add(new Object[]{

                2,

                "Cấy trụ Implant",

                "Cấy trụ titanium",

                "1 giờ",

                "Chưa thực hiện"
        });

        itinerarySteps.put(2, steps2);
    }

    /*
     * =========================================
     * STYLE TABLE
     * =========================================
     */

    private void styleTable(JTable table) {

        table.setRowHeight(40);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(PRIMARY_COLOR);

        table.getTableHeader().setForeground(Color.WHITE);

        table.setSelectionBackground(
                new Color(220, 235, 255)
        );

        table.setGridColor(
                new Color(240, 240, 240)
        );

        table.setShowVerticalLines(false);
    }

    /*
     * =========================================
     * CARD
     * =========================================
     */

    private JPanel createCardPanel(String title) {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(230, 230, 230)
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblTitle.setBorder(
                new EmptyBorder(0, 0, 15, 0)
        );

        panel.add(lblTitle, BorderLayout.NORTH);

        return panel;
    }

    /*
     * =========================================
     * BUTTON
     * =========================================
     */

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

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setPreferredSize(
                new Dimension(150, 42)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }
    ///load data
    private void loadData() {

    // Xóa dữ liệu cũ
    itineraryModel.setRowCount(0);

    // clear map step
    itinerarySteps.clear();

    StepRoadMapController stepController =
            new StepRoadMapController();

    List<RoadmapModel> roadmapList =
            roadmapController.getAllRoadmap();

    int stt = 1;

    for (RoadmapModel roadmap : roadmapList) {


        itineraryModel.addRow(new Object[]{

                roadmap.getId(),

                roadmap.getTitle(),

                roadmap.getDescription(),

                roadmap.getDatecreate()
        });

        // =========================
        // LOAD STEP THEO ROADMAP ID
        // =========================

        List<StepRoadMapModel> stepList =
                stepController.getStepsByRouteId(
                        roadmap.getId()
                );

        ArrayList<Object[]> stepData =
                new ArrayList<>();

        for (StepRoadMapModel step : stepList) {

            stepData.add(new Object[]{

                    step.getId(),

                    step.getStageName(),

                    step.getSequenceOrder(),

                    step.getDelay() + " ngày",

                    step.getAppointmentDate(),

                    step.getStatus(),

                    step.getCost(),

                    step.getNote()
            });
        }

        // =========================
        // LƯU THEO ROADMAP ID
        // =========================

        itinerarySteps.put(
                roadmap.getId(),
                stepData
        );

        stt++;
    }
}

}