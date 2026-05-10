package controller.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;
import model.DoctorModel;

public class DoctorController {
      public List<DoctorModel> getAllDoctor() {

        List<DoctorModel> userList = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "Select * from doctors, users where users.id = user_id and users.role_id =2";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            DoctorModel doctor = new DoctorModel();
            doctor.setId(rs.getInt("id"));
            doctor.setFullName(rs.getString("fullname"));
            doctor.setPhone(rs.getString("phone"));
            doctor.setPassword(rs.getString("password"));
            doctor.setBirthDate(rs.getDate("birth_date"));
            doctor.setAddress(rs.getString("address"));
            doctor.setAvatar(rs.getString("avatar"));
            doctor.setEmail(rs.getString("email"));
            doctor.setSpecialization(rs.getString("specialization"));
            doctor.setExperience(rs.getInt("experience"));
            userList.add(doctor);
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return userList;
    }

}
