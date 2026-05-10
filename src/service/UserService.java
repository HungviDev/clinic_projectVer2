package service;

import dao.user.UserDAO;
import model.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {

        userDAO = new UserDAO();
    }

    public User getUserById(int userId) {

        return userDAO.getUserById(userId);
    }
}