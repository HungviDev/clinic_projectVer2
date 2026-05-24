
package dao.user;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.TreatmentHistory;

public class TreatmentHistoryDAO {
    public List<TreatmentHistory> getByPatientId(int patientId) {
        List<TreatmentHistory> list = new ArrayList<>();
        String sql = """
            SELECT m.created_at, u.fullname AS doctor_name, m.diagnosis, tr.title AS treatment_title
            FROM medical_records m 
            LEFT JOIN doctors d ON m.doctor_id = d.id 
            LEFT JOIN users u ON d.user_id = u.id 
            LEFT JOIN treatment_routes tr ON m.treatment_route_id = tr.id
            WHERE m.user_id = ? 
            ORDER BY m.created_at DESC
        """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TreatmentHistory t = new TreatmentHistory();
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setDoctorName(rs.getString("doctor_name"));
                t.setDiagnosis(rs.getString("diagnosis"));
                t.setTreatmentPlan(rs.getString("treatment_title"));
                list.add(t);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}