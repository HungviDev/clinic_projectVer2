package controller.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                    rs.getInt("id")
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

            doctor.setAvatar(
                    rs.getString("avatar")
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

            System.out.println(doctor.getFullName());
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
    public static void main(String[] args) {
        List<DoctorModel> userList = new DoctorController().getAllDoctor();
        System.out.println(userList.size()+"hiển thị danh sách bác sĩ");
    }
}
