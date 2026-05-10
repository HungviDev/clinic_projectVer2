package controller.user;

import model.User;
import service.AuthService;

public class AuthController {

    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public User login(String phone, String password) throws Exception {
        return authService.login(phone, password);
    }

    public void register(String fullName, String phone, String password) throws Exception {
        authService.register(fullName, phone, password);
    }
}

