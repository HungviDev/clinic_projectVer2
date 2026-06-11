package controller.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;
import model.admin.DoctorModel;

public class DoctorController {
      public List<DoctorModel> getAllDoctor() {

        List<DoctorModel> userList = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

       String sql = """
                SELECT *
                FROM doctors, users
                WHERE users.id = user_id
                AND users.role_id = 2
                """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            DoctorModel doctor = new DoctorModel();

            doctor.setId(
                    rs.getInt("user_id")
            );

            doctor.setFullName(
                    rs.getString("fullname")
            );

            doctor.setPhone(
                    rs.getString("phone")
            );

            doctor.setPassword(
                    rs.getString("password")
            );

            doctor.setBirthDate(
                    rs.getDate("birth_date")
            );

            doctor.setAddress(
                    rs.getString("address")
            );



            doctor.setEmail(
                    rs.getString("email")
            );

            doctor.setSpecialization(
                    rs.getString("specialization")
            );

            doctor.setExperience(
                    rs.getInt("experience_years")
            );

            userList.add(doctor);

        }

            rs.close();

            ps.close();

            conn.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return userList;
    }
    public List<String> getAllDoctorNames() {

    List<String> doctorList = new ArrayList<>();

    try {

        Connection conn = DBConnection.getConnection();

        String sql = """
                SELECT fullname
                FROM doctors, users
                WHERE users.id = user_id
                AND users.role_id = 2
                """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            doctorList.add(
                    rs.getString("fullname")
            );
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return doctorList;
}
        public int getDoctorIdByFullName(String fullName) {

        int id = -1;

        try {

                Connection conn = DBConnection.getConnection();

                String sql = """
                        SELECT doctors.id
                        FROM doctors, users
                        WHERE users.id = user_id
                        AND users.role_id = 2
                        AND users.fullname = ?
                        """;

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ps.setString(1, fullName);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                id = rs.getInt("id");
                }

                rs.close();
                ps.close();
                conn.close();

        } catch (Exception e) {

                e.printStackTrace();
        }

        return id;
        }

    public DoctorModel getDoctorById(int id) {
        DoctorModel doctor = null;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = """
                    SELECT users.*, doctors.specialization, doctors.experience_years
                    FROM users
                    JOIN doctors ON users.id = doctors.user_id
                    WHERE users.id = ? AND users.role_id = 2
                    """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                doctor = new DoctorModel();
                doctor.setId(rs.getInt("id"));
                doctor.setFullName(rs.getString("fullname"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setPassword(rs.getString("password"));
                doctor.setBirthDate(rs.getDate("birth_date"));
                doctor.setAddress(rs.getString("address"));
                doctor.setAvatar(rs.getString("avatar"));
                doctor.setEmail(rs.getString("email"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setExperience(rs.getInt("experience_years"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctor;
    }

    public boolean insertDoctor(DoctorModel doctor) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlUser = "INSERT INTO users(fullname, phone, password, birth_date, address, role_id, email) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement psUser = conn.prepareStatement(sqlUser, java.sql.Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, doctor.getFullName());
            psUser.setString(2, doctor.getPhone());
            psUser.setString(3, doctor.getPassword());
            psUser.setDate(4, new java.sql.Date(doctor.getBirthDate().getTime()));
            psUser.setString(5, doctor.getAddress());
            psUser.setInt(6, 2); // role_id for doctor
            psUser.setString(7, doctor.getEmail());
            psUser.executeUpdate();

            ResultSet rs = psUser.getGeneratedKeys();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt(1);
            }
            rs.close();
            psUser.close();

            if (userId != -1) {
                String sqlDoctor = "INSERT INTO doctors(user_id, specialization, experience_years) VALUES(?,?,?)";
                PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
                psDoctor.setInt(1, userId);
                psDoctor.setString(2, doctor.getSpecialization());
                psDoctor.setInt(3, doctor.getExperience());
                psDoctor.executeUpdate();
                psDoctor.close();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {}
        }
    }

    public boolean updateDoctor(DoctorModel doctor) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlUser = "UPDATE users SET fullname=?, phone=?, password=?, birth_date=?, address=?, email=? WHERE id=?";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, doctor.getFullName());
            psUser.setString(2, doctor.getPhone());
            psUser.setString(3, doctor.getPassword());
            psUser.setDate(4, new java.sql.Date(doctor.getBirthDate().getTime()));
            psUser.setString(5, doctor.getAddress());
            psUser.setString(6, doctor.getEmail());
            psUser.setInt(7, doctor.getId());
            psUser.executeUpdate();
            psUser.close();

            String sqlDoctor = "UPDATE doctors SET specialization=?, experience_years=? WHERE user_id=?";
            PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
            psDoctor.setString(1, doctor.getSpecialization());
            psDoctor.setInt(2, doctor.getExperience());
            psDoctor.setInt(3, doctor.getId());
            psDoctor.executeUpdate();
            psDoctor.close();

            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {}
        }
    }

    public boolean deleteDoctor(int id) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlDoctor = "DELETE FROM doctors WHERE user_id=?";
            PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
            psDoctor.setInt(1, id);
            psDoctor.executeUpdate();
            psDoctor.close();

            String sqlUser = "DELETE FROM users WHERE id=?";
            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setInt(1, id);
            psUser.executeUpdate();
            psUser.close();

            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ex) {}
        }
    }
    public int getIdUserByDoctorId(int doctorId) {
        String sql = "SELECT user_id FROM doctors WHERE id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
    
    public static void main(String[] args) {
        List<DoctorModel> userList = new DoctorController().getAllDoctor();
        System.out.println(userList.size()+"hiển thị danh sách bác sĩ");
    }
}
