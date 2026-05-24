package controller.user;

import model.User;
import model.User;
import service.UserService;
import ui.auth.LoginForm;
import ui.auth.MainDashboard;

import javax.swing.*;

public class DashboardController {

    private User user;

    private MainDashboard view;

    public DashboardController(int userId) {

        UserService service = new UserService();

        // Lấy user từ DB/service
        this.user = service.getUserById(userId);
    }

    public void setView(MainDashboard view) {
        this.view = view;
    }

    public User getUser() {
        return user;
    }

    // ================= ROLE =================

    public boolean isAdmin() {
        return user.getRoleId() == 1;
    }

    public boolean isDoctor() {
        return user.getRoleId() == 2;
    }

    public boolean isPatient() {
        return user.getRoleId() == 3;
    }

    // ================= ROLE NAME =================

    public String getRoleName() {

        switch (user.getRoleId()) {

            case 1:
                return "Admin";

            case 2:
                return "Bác sĩ";

            case 3:
                return "Bệnh nhân";

            default:
                return "User";
        }
    }

    // ================= ĐỔI TRANG =================

    public void showPage(String pageName) {

        if (view != null) {
            view.showPage(pageName);
        }
    }

    // ================= LOGOUT =================

    public void logout() {

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            view.dispose();

            new LoginForm();
        }
    }
}