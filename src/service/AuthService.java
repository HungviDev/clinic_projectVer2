package service;

import dao.user.UserDAO;
import model.user.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String phone, String password) throws Exception {
        // validate tối thiểu ở service
        if (phone == null || phone.trim().isEmpty()) throw new IllegalArgumentException("Phone is required");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password is required");

        return userDAO.findByPhoneAndPassword(phone.trim(), password);
    }

    public void register(String fullName, String phone, String password) throws Exception {
        if (fullName == null || fullName.trim().isEmpty()) throw new IllegalArgumentException("Full name is required");
        if (phone == null || phone.trim().isEmpty()) throw new IllegalArgumentException("Phone is required");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password is required");

        // role_id = 3 (patient) theo code hiện tại
        int rows = userDAO.createUser(fullName.trim(), phone.trim(), password, 3);
        if (rows <= 0) {
            throw new IllegalStateException("Register failed");
        }
    }
}

