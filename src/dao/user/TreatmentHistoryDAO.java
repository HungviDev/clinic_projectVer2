package dao.user;

import config.DBConnection;
import model.user.TreatmentHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TreatmentHistoryDAO {

    public List<TreatmentHistory> getByPatientId(int patientId) {

        List<TreatmentHistory> list = new ArrayList<>();

        String sql = "SELECT m.created_at, " +
                    "u.fullname AS doctor_name, " +
                    "m.diagnosis, " +
                    "tr.title AS treatment_plan, " + // Lấy tên lộ trình từ bảng treatment_routes
                                                
                    // DÙNG CASE WHEN ĐỂ XỬ LÝ LOGIC TRẠNG THÁI
                    "CASE " +
                    "   WHEN tr.id IS NULL THEN N'Chưa có lộ trình' " + // Bệnh án chưa được gán lộ trình
                    "   WHEN ts.stage_name IS NULL THEN N'Đã hoàn thành' " + // Không còn bước nào Chưa/Đang thực hiện -> Đã hoàn thành
                    "   ELSE ts.stage_name " + // Đang làm hoặc chuẩn bị làm bước này
                    "END AS stage_name " +
                                                
                    "FROM medical_records m " +
                    "LEFT JOIN doctors d ON m.doctor_id = d.id " +
                    "LEFT JOIN users u ON d.user_id = u.id " +
                    "LEFT JOIN treatment_routes tr ON m.treatment_route_id = tr.id " + 
                                                
                    // OUTER APPLY TÌM BƯỚC ĐANG LÀM/CHỜ
                    "OUTER APPLY ( " +
                    "    SELECT TOP 1 stage_name " +
                    "    FROM treatment_stages " +
                    "    WHERE treatment_route_id = tr.id " +
                    "      AND status IN (N'Chưa thực hiện', N'Đang thực hiện') " + // Cập nhật trạng thái tiếng Việt
                    "    ORDER BY sequence_order ASC " +
                    ") ts " +
                                                
                    "WHERE m.user_id = ? " +
                    "ORDER BY m.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                TreatmentHistory t = new TreatmentHistory();

                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setDoctorName(rs.getString("doctor_name"));
                t.setDiagnosis(rs.getString("diagnosis"));
                t.setTreatmentPlan(rs.getString("treatment_plan"));
                t.setstatusStage(rs.getString("stage_name"));

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}