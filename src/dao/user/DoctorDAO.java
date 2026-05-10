package dao.user;

import config.DBConnection;
import model.user.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public List<Doctor> getAllDoctors() {

        List<Doctor> list = new ArrayList<>();

        String sql =
                "SELECT d.id, u.fullname " +
                "FROM doctors d " +
                "JOIN users u ON d.user_id = u.id " +
                "WHERE u.role_id = 2";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                list.add(
                        new Doctor(
                                rs.getInt("id"),
                                rs.getString("fullname")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public String getFullNameById(int id) {

    String fullName = null;

    try {

        Connection conn = DBConnection.getConnection();

        String sql = """
                SELECT u.fullname
                FROM users u
                JOIN doctors d ON d.id = u.id
                WHERE d.id = ?
                """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            fullName = rs.getString("fullname");
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return fullName;
}
}