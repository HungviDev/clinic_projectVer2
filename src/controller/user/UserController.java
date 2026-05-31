package controller.user;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;
import dao.user.UserDAO;
import model.admin.UserModel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserController {

    // =====================================
    // GET ALL USERS
    // =====================================
    public List<UserModel> getAllUsers() {

        List<UserModel> userList = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM users where role_id = 3";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                UserModel user = new UserModel();

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
    public boolean insertUser(UserModel user) {

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
     public boolean updateUser(UserModel user) {
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
    public int countPatients() {

        int total = 0;

        try {

                Connection conn =
                        DBConnection.getConnection();

                String sql =
                        "SELECT COUNT(*) AS total FROM users WHERE role_id = 3";

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery();

                if (rs.next()) {

                total = rs.getInt("total");
                }

                rs.close();

                ps.close();

                conn.close();

        } catch (Exception e) {

                e.printStackTrace();
        }

        return total;
        }
public boolean updateProfile(int userId, String fullName, String phone, String birthDateStr, String address, String email, File newAvatarFile) throws Exception {
        java.sql.Date sqlDate = null;
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            sqlDate = java.sql.Date.valueOf(birthDateStr.trim()); 
        }
        
        // 1. Xử lý lưu file ảnh nếu người dùng có chọn ảnh mới
        String newAvatarFileName = null;
        if (newAvatarFile != null) {
            String originalName = newAvatarFile.getName();
            String ext = originalName.substring(originalName.lastIndexOf("."));
            
            // Tạo tên file mới chống trùng lặp
            newAvatarFileName = "avatar_" + userId + "_" + System.currentTimeMillis() + ext;
            
            // Copy file vào thư mục resources
            String projectPath = System.getProperty("user.dir");
            File destFolder = new File(projectPath + "/src/resources");
            if (!destFolder.exists()) destFolder.mkdirs(); 
            
            Path destPath = Paths.get(destFolder.getAbsolutePath(), newAvatarFileName);
            Files.copy(newAvatarFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 2. Gọi xuống DAO để cập nhật thông tin chữ 
        UserDAO userdao = new UserDAO();
        boolean isSuccess = userdao.updateProfile(userId, fullName, phone, sqlDate, address, email);
        
        // 3. Cập nhật tên ảnh vào Database nếu đổi ảnh thành công
        if (isSuccess && newAvatarFileName != null) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE users SET avatar = ? WHERE id = ?")) {
                ps.setString(1, newAvatarFileName);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
        }
        return isSuccess;
    }

        // =====================================
        // COUNT DOCTORS
        // =====================================
        public int countDoctors() {

        int total = 0;

        try {

                Connection conn =
                        DBConnection.getConnection();

                String sql =
                        "SELECT COUNT(*) AS total FROM users WHERE role_id = 2";

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery();

                if (rs.next()) {

                total = rs.getInt("total");
                }

                rs.close();

                ps.close();

                conn.close();

        } catch (Exception e) {

                e.printStackTrace();
        }

        return total;
        }
     public UserModel getUserByPhone(String phone) {

    UserModel user = null;

    try {

        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM users WHERE phone = ? AND role_id = 3";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, phone);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            user = new UserModel();

            user.setId(rs.getInt("id"));
            user.setFullName(rs.getString("fullname"));
            user.setPhone(rs.getString("phone"));
            user.setPassword(rs.getString("password"));
            user.setBirthDate(rs.getDate("birth_date"));
            user.setAddress(rs.getString("address"));
            user.setAvatar(rs.getString("avatar"));
            user.setEmail(rs.getString("email"));
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return user;
}
        public static void main(String[] args) {
            UserController controller = new UserController();

        UserModel user = controller.getUserByPhone("03377928757");

        if (user != null) {

                System.out.println("ID: " + user.getId());
                System.out.println("Name: " + user.getFullName());
                System.out.println("Phone: " + user.getPhone());

        } else {

                System.out.println("Không tìm thấy user");
    }
        }
}