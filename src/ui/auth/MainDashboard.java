package ui.auth;

import controller.user.DashboardController;
import java.awt.*;
import java.sql.Date;
import ui.admin.AppointmentView;
import ui.admin.DashboardView;
import ui.admin.DoctorView;
import ui.admin.ItinereryView;
import ui.admin.OrderView;
import ui.admin.ServicesView;
import ui.admin.UserView;
import ui.auth.LoginForm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.user.DashboardController;
import model.user.User;

import java.awt.*;
import ui.doctor.View.DoctorDashboardView;
import ui.patient.BookingPanel;
import ui.patient.ContactPanel;
import ui.patient.EditProfilePanel;
import ui.patient.HomePatientPanel;
import ui.patient.MyAppointmentsPanel;
import ui.patient.PaymentHistoryPanel;
import ui.patient.ProfilePanel;
import ui.patient.ServicePatientPanel;
import ui.patient.TreatmentHistoryPanel;

public class MainDashboard extends JFrame {

    // ================= BẢNG MÀU PASTEL BLUE =================
    private final Color HEADER_COLOR = new Color(133, 193, 233);  // Xanh biển pastel (vừa)
    private final Color SIDEBAR_COLOR = new Color(214, 234, 248); // Xanh biển pastel (sáng, nhạt)
    private final Color BG_COLOR = new Color(248, 250, 252);      // Trắng pha chút xanh xám nhạt
    private final Color TEXT_DARK = new Color(44, 62, 80);        // Xanh than/xám đậm cho chữ
    private final Color HOVER_COLOR = new Color(93, 173, 226);    // Xanh pastel đậm hơn khi di chuột

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private DashboardController controller;
    private MyAppointmentsPanel appointmentsPanel;
    
    // Đưa lblUser ra làm biến toàn cục để có thể đổi tên sau khi cập nhật
    private JLabel lblUser;

    public MainDashboard(DashboardController controller) {
        this.controller = controller;

        controller.setView(this);

        User user = controller.getUser();

        int userId = user.getId();
        String userName = user.getFullName();
        String userPhone = user.getPhone();
        String address =  user.getAddress();
        Date birth_date = user.getBirthDate();
        String email = user.getEmail();
        int role = user.getRoleId();
        String avatarPath = user.getAvatar();

        setTitle("Hệ Thống Nha Khoa Việt Anh");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(1200, 70));
        header.setBorder(new EmptyBorder(0, 20, 0, 20)); // Căn lề 2 bên rộng hơn chút

        JLabel lblLogo = new JLabel("NHA KHOA VIỆT ANH");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // Khởi tạo lblUser (Không dùng từ khóa JLabel ở đầu nữa)
        lblUser = new JLabel(
            "Xin chào "
            + controller.getRoleName()
            + " "
            + userName
        );
        lblUser.setForeground(TEXT_DARK);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 16));

        header.add(lblLogo, BorderLayout.WEST);
        header.add(lblUser, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(240, 700));
        
        // Dùng BoxLayout để dễ kiểm soát chiều cao từng nút và khoảng cách
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); 
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // ===== MENU CHUNG =====
        JButton btnHome = createMenuButton("Trang chủ");
        sidebar.add(btnHome);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        if (controller.isPatient()) {
            JButton btnService = createMenuButton("Dịch vụ");
            JButton btnBooking = createMenuButton("Đặt lịch");
            // JButton btnCart = createMenuButton("Giỏ hàng");
            JButton btnContact = createMenuButton("Liên hệ");
            JButton btnProfile = createMenuButton("Cá nhân");

            sidebar.add(btnService);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnBooking);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            // sidebar.add(btnCart);
            // sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnContact);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnProfile);

            btnService.addActionListener(e -> controller.showPage("SERVICE"));
            btnBooking.addActionListener(e -> controller.showPage("BOOKING"));
            // btnCart.addActionListener(e -> controller.showPage("CART"));            
            btnContact.addActionListener(e -> controller.showPage("CONTACT"));
            btnProfile.addActionListener(e -> controller.showPage("PROFILE"));
        }

        // ================= DOCTOR (Role = 2) =================
        if(controller.isDoctor()) {
            JButton btnSchedule = createMenuButton("Lịch khám");
            JButton btnMedical = createMenuButton("Hồ sơ bệnh án");
            JButton btnPatients = createMenuButton("Bệnh nhân");

            sidebar.add(btnSchedule);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnMedical);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnPatients);

            btnSchedule.addActionListener(e -> cardLayout.show(contentPanel, "SCHEDULE"));
            btnMedical.addActionListener(e -> cardLayout.show(contentPanel, "MEDICAL"));
            btnPatients.addActionListener(e -> cardLayout.show(contentPanel, "PATIENTS"));
        }

        // ================= ADMIN (Role = 1) =================
        if(controller.isAdmin()) {
            JButton btnUsers = createMenuButton("Quản lý người dùng");
            JButton btnDoctors = createMenuButton("Quản lý bác sĩ");
            JButton btnServices = createMenuButton("Quản lý dịch vụ");
            JButton btnAppoinment = createMenuButton("Quản lý lịch hẹn");
            JButton btnOrders = createMenuButton("Quản lý đơn hàng");
            JButton btnStatistics = createMenuButton("Thống kê");
            JButton btnItinary = createMenuButton("Quản lý lộ trình điều trị");
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnUsers);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnDoctors);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnServices);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnOrders);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnStatistics);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnAppoinment);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnItinary);
            btnUsers.addActionListener(e -> cardLayout.show(contentPanel, "USERS"));
            btnDoctors.addActionListener(e -> cardLayout.show(contentPanel, "DOCTORS"));
            btnServices.addActionListener(e -> cardLayout.show(contentPanel, "SERVICES_ADMIN"));
            btnOrders.addActionListener(e -> cardLayout.show(contentPanel, "ORDERS"));
            btnStatistics.addActionListener(e -> cardLayout.show(contentPanel, "STATISTICS"));
            btnAppoinment.addActionListener(e -> cardLayout.show(contentPanel, "APPOINTMENTS"));
            btnItinary.addActionListener(e -> cardLayout.show(contentPanel, "ITINERARY"));
        }

        // ================= LOGOUT =================
        sidebar.add(Box.createVerticalGlue()); // Đẩy nút đăng xuất xuống cuối cùng

        JButton btnLogout = createMenuButton("Đăng xuất");
        sidebar.add(btnLogout);
        btnLogout.addActionListener(e -> controller.logout());

        add(sidebar, BorderLayout.WEST);

        // ================= CONTENT PANEL =================
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Nạp trước các màn hình giả lập
        if(role == 3) {
            contentPanel.add(new HomePatientPanel(userId,userName), "HOME");
            // (Bệnh nhân)
            contentPanel.add(new ServicePatientPanel(), "SERVICE");
            contentPanel.add(new BookingPanel(userId), "BOOKING");
            // contentPanel.add(createPagePanel("GIỎ HÀNG", ""), "CART");
            contentPanel.add(new ContactPanel(), "CONTACT");
            
            contentPanel.add(new ProfilePanel(userName, userPhone, avatarPath), "PROFILE");
            contentPanel.add(new TreatmentHistoryPanel(userId), "MEDICAL_RECORD");
            contentPanel.add(new PaymentHistoryPanel(userId), "PAYMENT_HISTORY");   
            contentPanel.add(new EditProfilePanel(userId, userName, userPhone,  birth_date, address, email, avatarPath), "EDIT_PROFILE"); 
            appointmentsPanel = new MyAppointmentsPanel(userId);

            contentPanel.add(appointmentsPanel, "SCHEDULE");
        }
        
        // (Bác sĩ)
        if(role == 2)
        {contentPanel.add(new DoctorDashboardView(userId,userName), "HOME");
        contentPanel.add(createPagePanel("LỊCH KHÁM", "Danh sách lịch hẹn"), "SCHEDULE");
        contentPanel.add(createPagePanel("HỒ SƠ BỆNH ÁN", "Quản lý bệnh án"), "MEDICAL");
        contentPanel.add(createPagePanel("BỆNH NHÂN", "Danh sách bệnh nhân"), "PATIENTS");
        }
        
        // (Admin)
        if(role == 1)
        {
        contentPanel.add(new DashboardView (), "HOME");
        contentPanel.add(new UserView(), "USERS");
        contentPanel.add(new DoctorView(), "DOCTORS");
        contentPanel.add(new ServicesView(), "SERVICES_ADMIN");
        contentPanel.add(new AppointmentView(), "APPOINTMENTS");
        contentPanel.add(new OrderView(), "ORDERS");
        contentPanel.add(new ItinereryView(),"ITINERARY");
        contentPanel.add(createPagePanel("THỐNG KÊ", "Biểu đồ doanh thu"), "STATISTICS");
        }

        add(contentPanel, BorderLayout.CENTER);

        // Hiển thị Trang chủ mặc định
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));

        setVisible(true);
    }

    // Hàm này cho phép các Panel con gọi ngược ra để đổi trang
    public void showPage(String pageName) {
        if (cardLayout != null && contentPanel != null) {
            cardLayout.show(contentPanel, pageName);
        }
    }

    // ================= TẠO NÚT MENU BO GÓC =================
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setForeground(TEXT_DARK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Tăng độ dày nút bằng padding (Trên 18, Trái 20, Dưới 18, Phải 20)
        btn.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20)); 
        
        // Cố định chiều cao nút (VD: 55px) và mở rộng tối đa chiều ngang
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Thêm hiệu ứng Hover đổi màu
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(HOVER_COLOR);
                btn.setForeground(Color.WHITE); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR_COLOR);
                btn.setForeground(TEXT_DARK);   
            }
        });

        return btn;
    }

    // ================= TẠO TRANG NỘI DUNG (GIẢ LẬP) =================
    private JPanel createPagePanel(String title, String content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setBorder(new EmptyBorder(30, 0, 20, 0));

        JTextArea txtContent = new JTextArea(content);
        txtContent.setEditable(false);
        txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtContent.setBackground(BG_COLOR);
        txtContent.setForeground(Color.DARK_GRAY);
        txtContent.setBorder(new EmptyBorder(20, 40, 20, 40));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(txtContent, BorderLayout.CENTER);

        return panel;
    }
    
    public void refreshAppointments() {
        if (appointmentsPanel != null) {
            appointmentsPanel.loadData();
        }
    }
    
    // ================= HÀM LÀM MỚI DỮ LIỆU SAU KHI LƯU PROFILE =================
    public void reloadUserData() {
        // Lấy lại dữ liệu mới nhất từ DB
        dao.user.UserDAO userDAO = new dao.user.UserDAO();
        User updatedUser = userDAO.getUserById(controller.getUser().getId());

        if (updatedUser != null) {
            // Cập nhật text trên Header
            lblUser.setText("Xin chào " + controller.getRoleName() + " " + updatedUser.getFullName());

            // Ghi đè lại 2 trang Profile bằng Panel mới chứa dữ liệu vừa cập nhật
            contentPanel.add(new ProfilePanel(updatedUser.getFullName(), updatedUser.getPhone(), updatedUser.getAvatar()), "PROFILE");
            contentPanel.add(new EditProfilePanel(
                updatedUser.getId(), 
                updatedUser.getFullName(), 
                updatedUser.getPhone(), 
                updatedUser.getBirthDate(), 
                updatedUser.getAddress(), 
                updatedUser.getEmail(), 
                updatedUser.getAvatar()
            ), "EDIT_PROFILE");

            // Làm mới giao diện
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    // Hàm main để test giao diện độc lập mà không cần phải qua trang đăng nhập
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            DashboardController controller = new DashboardController(1);
            new MainDashboard(controller);
        });
    }
}