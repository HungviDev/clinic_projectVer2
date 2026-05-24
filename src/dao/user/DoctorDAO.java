package dao.user;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Doctor;

public class DoctorDAO {

    public List<Doctor> getAllDoctors() {

        List<Doctor> list = new ArrayList<>();

        String sql =
                "SELECT DISTINCT u.id, u.fullname " +
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
}