package controller.admin;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;
import dao.user.UserDAO;
import model.User;
import model.User;

public class UserController {

    // =====================================
    // GET ALL USERS
    // =====================================
    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM users where role_id = 3";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                User user = new User();

                user.setId(
                        rs.getInt("id")
                );

                user.setFullName(
                        rs.getString("fullname")
                );

                user.setPhone(
                        rs.getString("phone")
                );

                user.setPassword(
                        rs.getString("password")
                );

                user.setBirthDate(
                        rs.getDate("birth_date")
                );

                user.setAddress(
                        rs.getString("address")
                );

                user.setAvatar(
                        rs.getString("avatar")
                );
                user.setEmail(
                        rs.getString("email")
                );
                userList.add(user);
            }

            rs.close();

            ps.close();

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return userList;
    }
    public boolean insertUser(User user) {

    try {

        Connection conn = DBConnection.getConnection();

        String sql = """
                INSERT INTO users(
                    fullname,
                    phone,
                    password,
                    birth_date,
                    address,
                    avatar,
                    role_id,
                    email
                )
                VALUES(?,?,?,?,?,?,?,?)
                """;

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setString(1, user.getFullName());

        ps.setString(2, user.getPhone());

        ps.setString(3, user.getPassword());

        ps.setDate(
                4,
                new java.sql.Date(
                        user.getBirthDate().getTime()
                )
        );

        ps.setString(5, user.getAddress());

        ps.setString(6, user.getAvatar());

        // role mặc định = 3
        ps.setInt(7, 3);

        ps.setString(8, user.getEmail());

        int result = ps.executeUpdate();

        ps.close();

        conn.close();

        return result > 0;

    } catch (Exception e) {

        e.printStackTrace();

        return false;
    }
}
public boolean deleteUser(int id) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql =
                    "DELETE FROM users WHERE id = ?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            ps.close();

            conn.close();

            return result > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
     public boolean updateUser(User user) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = """
                    UPDATE users
                    SET
                        fullname = ?,
                        phone = ?,
                        password = ?,
                        birth_date = ?,
                        address = ?,
                        avatar = ?,
                        role_id = ?,
                        email = ?
                    WHERE id = ?
                    """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, user.getFullName());

            ps.setString(2, user.getPhone());

            ps.setString(3, user.getPassword());

            ps.setDate(
                    4,
                    new java.sql.Date(
                            user.getBirthDate().getTime()
                    )
            );

            ps.setString(5, user.getAddress());

            ps.setString(6, user.getAvatar());

            ps.setInt(7, 3);

            ps.setString(8, user.getEmail());

            ps.setInt(9, user.getId());

            int result = ps.executeUpdate();

            ps.close();

            conn.close();

            return result > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateProfile(int userId, String fullName, String phone, String birthDateStr, String address, String email) throws IllegalArgumentException {
        java.sql.Date sqlDate = null;
        
        // Kiểm tra và ép kiểu chuỗi ngày tháng sang java.sql.Date
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            sqlDate = java.sql.Date.valueOf(birthDateStr.trim()); // Sẽ ném ra IllegalArgumentException nếu sai định dạng
        }
        
        // Gọi xuống DAO để thực thi DB
        UserDAO userdao = new UserDAO();
        return userdao.updateProfile(userId, fullName, phone, sqlDate, address, email);
    }

}