package dao.user;

import config.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
public class UserDAO {

    public User getUserById(int userId) {

        String sql =
            "SELECT * FROM users WHERE id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

return new User(
                        rs.getInt("id"),
                        rs.getString("fullname"),
                        rs.getString("phone"),
                        rs.getString("password"), // 1. Chuyển password lên đây
                        rs.getInt("role_id"),     // 2. Chuyển role_id xuống đây
                        rs.getDate("birth_date"), 
                        rs.getString("address"),
                        rs.getString("email"),    // 3. Chuyển email lên trước
                        rs.getString("avatar")    // 4. Chuyển avatar xuống cuối
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public User findByPhoneAndPassword(String phone, String password) throws Exception {
        String sql = "SELECT id, fullname, phone, role_id FROM users WHERE phone=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("fullname"));
                u.setPhone(rs.getString("phone"));
                u.setRoleId(rs.getInt("role_id"));
                return u;
            }
        }
    }

    public int createUser(String fullName, String phone, String password, int roleId) throws Exception {
        String sql = "INSERT INTO users(fullname, phone, password, role_id) VALUES (?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, password);
            ps.setInt(4, roleId);
            return ps.executeUpdate();
        }
    }

    // ================= LƯU THÔNG TIN PROFILE =================
    public boolean updateProfile(int userId, String fullName, String phone, java.sql.Date birthDate, String address, String email) {
        String sql = "UPDATE users SET fullname = ?, phone = ?, birth_date = ?, address = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, fullName);
            ps.setString(2, phone);
            
            if (birthDate == null) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, birthDate);
            }
            
            ps.setString(4, address);
            ps.setString(5, email);
            ps.setInt(6, userId);
            
            // Trả về true nếu có ít nhất 1 dòng được cập nhật thành công
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

