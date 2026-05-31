package ui.doctor.Controller;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ui.doctor.Model.PatientModel;

public class PatientController {

    public List<PatientModel> getPatientsByDoctor(int doctorUserId) {
        List<PatientModel> list = new ArrayList<>();
        
        // Dùng GROUP BY để gộp các lịch hẹn của cùng một bệnh nhân thành 1 dòng duy nhất
       String sql = """
    SELECT u.fullname, 
           MAX(u.birth_date) as birth_date, 
           MAX(u.phone) as phone, 
           MAX(u.email) as email, 
           MAX(u.address) as address, 
           MAX(s.name) as treatment_problem
    FROM users u
    JOIN appointments a ON u.id = a.user_id
    JOIN doctors d ON a.doctor_id = d.id
    LEFT JOIN services s ON a.service_id = s.id
    WHERE d.user_id = ? 
    GROUP BY u.id, u.fullname

        """;

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorUserId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new PatientModel(
                    rs.getString("fullname"),
                    rs.getString("birth_date"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("treatment_problem")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}