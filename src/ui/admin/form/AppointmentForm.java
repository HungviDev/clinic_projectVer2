package ui.admin.form;

import controller.admin.DoctorController;
import controller.admin.ServiceController;
import controller.admin.UserController;
import ui.doctor.Controller.AppointmentController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import java.awt.*;

public class AppointmentForm extends JDialog {

    private JComboBox<String> cboPatient;
    private JComboBox<String> cboDoctor;
    private JComboBox<String> cboService;

    private JTextField txtEmail;
    
    // ĐÃ ĐỔI TỪ JTextField SANG JDateChooser
    private JDateChooser dateChooserAppointment; 

    private JTextArea txtDescription;
    
    private UserController userController = new UserController();
    private DoctorController doctorController = new DoctorController();
    private ServiceController serviceController = new ServiceController();
    private AppointmentController appointmentController = new AppointmentController();

    private int idPatient = 0;
    private int idDoctor = 0;
    private int idService = 0;

    public AppointmentForm(JFrame parent) {

        super(parent, "TẠO LỊCH HẸN", true);
        setSize(850, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 244, 250));

        // =====================================================
        // HEADER
        // =====================================================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel lblTitle = new JLabel("TẠO LỊCH HẸN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 34));

        header.add(lblTitle, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // =====================================================
        // CONTENT
        // =====================================================
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(240, 244, 250));
        content.setBorder(new EmptyBorder(40, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 18, 18, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        // =====================================================
        // PATIENT
        // =====================================================
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(createLabel("Tên bệnh nhân"), gbc);

        gbc.gridx = 1;
        cboPatient = new JComboBox<>();
        cboPatient.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cboPatient.setPreferredSize(new Dimension(450, 45));

        getAllPatient();
        eventPatient();
        content.add(cboPatient, gbc);

        // =====================================================
        // EMAIL
        // =====================================================
        gbc.gridx = 0;
        gbc.gridy++;
        content.add(createLabel("Email bệnh nhân"), gbc);

        gbc.gridx = 1;
        txtEmail = createTextField();
        txtEmail.setEditable(false);
        content.add(txtEmail, gbc);

        // =====================================================
        // DOCTOR
        // =====================================================
        gbc.gridx = 0;
        gbc.gridy++;
        content.add(createLabel("Tên bác sĩ"), gbc);

        gbc.gridx = 1;
        cboDoctor = new JComboBox<>();
        cboDoctor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cboDoctor.setPreferredSize(new Dimension(450, 45));

        getAllDoctor();
        eventDoctor();
        content.add(cboDoctor, gbc);

        // =====================================================
        // SERVICE
        // =====================================================
        gbc.gridx = 0;
        gbc.gridy++;
        content.add(createLabel("Dịch vụ"), gbc);

        gbc.gridx = 1;
        cboService = new JComboBox<>();
        cboService.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cboService.setPreferredSize(new Dimension(450, 45));

        getAllService();
        eventService();
        content.add(cboService, gbc);

        // =====================================================
        // DATE (ĐÃ NÂNG CẤP LÊN JCALENDAR)
        // =====================================================
        gbc.gridx = 0;
        gbc.gridy++;
        content.add(createLabel("Ngày hẹn"), gbc);

        gbc.gridx = 1;
        dateChooserAppointment = new JDateChooser();
        dateChooserAppointment.setPreferredSize(new Dimension(450, 45));
        dateChooserAppointment.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        dateChooserAppointment.setDateFormatString("yyyy-MM-dd"); // Định dạng ngày lưu DB
        
        // Chỉnh viền và font cho thanh nhập text bên trong JDateChooser để đồng bộ giao diện
        JTextField dateEditor = (JTextField) dateChooserAppointment.getDateEditor().getUiComponent();
        dateEditor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        dateEditor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));

        content.add(dateChooserAppointment, gbc);

        // =====================================================
        // DESCRIPTION
        // =====================================================
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        content.add(createLabel("Mô tả"), gbc);

        gbc.gridx = 1;
        txtDescription = new JTextArea(10, 40);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(txtDescription);
        // Tăng chiều cao lên 250 (hoặc 300 tùy ý bạn)
        scrollPane.setPreferredSize(new Dimension(500, 250)); 
        
        // Thêm 2 dòng này để ô text tự động lấp đầy khoảng trống cực đẹp
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weighty = 1.0;
        
        content.add(scrollPane, gbc);

        add(content, BorderLayout.CENTER);

        // =====================================================
        // FOOTER
        // =====================================================
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        footer.setBackground(new Color(240, 244, 250));

        JButton btnSave = createButton("Tạo lịch hẹn", new Color(46, 204, 113));
        JButton btnClose = createButton("Đóng", new Color(231, 76, 60));

        footer.add(btnSave);
        footer.add(btnClose);
        add(footer, BorderLayout.SOUTH);

        // =====================================================
        // EVENT BUTTON
        // =====================================================
        btnClose.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> {
            // Lấy ngày đã chọn từ JDateChooser (trả về kiểu java.util.Date)
            // java.util.Date selectedDate = dateChooserAppointment.getDate();
            
            // int id = 1;
            // appointmentController.insertAppointment();
            
            // JOptionPane.showMessageDialog(this, "Tạo lịch hẹn thành công");
        });
    }

    // =====================================================
    // LABEL
    // =====================================================
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return label;
    }

    // =====================================================
    // TEXTFIELD
    // =====================================================
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setPreferredSize(new Dimension(450, 45));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    // =====================================================
    // BUTTON
    // =====================================================
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(190, 48));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return button;
    }

    // =====================================================
    // LOAD PATIENT
    // =====================================================
    public void getAllPatient() {
        java.util.List<String> list = userController.getAllPatients();
        cboPatient.removeAllItems();
        for (String patient : list) {
            cboPatient.addItem(patient);
        }
    }

    // =====================================================
    // EVENT PATIENT
    // =====================================================
    public void eventPatient() {
        cboPatient.addActionListener(e -> {
            if (cboPatient.getSelectedItem() == null) return;
            String patient = (String) cboPatient.getSelectedItem();
            txtEmail.setText(userController.getPatientEmailByFullName(patient));
            idPatient = userController.getPatientIdByFullName(patient);
        });
    }

    // =====================================================
    // LOAD DOCTOR
    // =====================================================
    public void getAllDoctor() {
        java.util.List<String> list = doctorController.getAllDoctorNames();
        cboDoctor.removeAllItems();
        for (String doctor : list) {
            cboDoctor.addItem(doctor);
        }
    }

    // =====================================================
    // EVENT DOCTOR
    // =====================================================
    public void eventDoctor() {
        cboDoctor.addActionListener(e -> {
            if (cboDoctor.getSelectedItem() == null) return;
            String doctor = (String) cboDoctor.getSelectedItem();
            idDoctor = doctorController.getDoctorIdByFullName(doctor);
        });
    }

    // =====================================================
    // LOAD SERVICE
    // =====================================================
    public void getAllService() {
        java.util.List<String> list = serviceController.getAllServiceNames();
        cboService.removeAllItems();
        for (String service : list) {
            cboService.addItem(service);
        }
    }

    // =====================================================
    // EVENT SERVICE
    // =====================================================
    public void eventService() {
        cboService.addActionListener(e -> {
            if (cboService.getSelectedItem() == null) return;
            String service = (String) cboService.getSelectedItem();
            idService = serviceController.getServiceIdByName(service);
        });
    }
}