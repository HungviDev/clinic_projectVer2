
package ui.doctor.Controller;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ui.doctor.Model.MedicalRecordModel;

public class MedicalRecordController {

    private final Integer doctorUserId;

    // =====================================================
    // HÀM LẤY KẾT NỐI CHUNG TỪ LỚP DBConnection
    // =====================================================
    private Connection getDbConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public MedicalRecordController() {
        this(null);
    }

    public MedicalRecordController(Integer doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    // =====================================================
    // LẤY TỰ ĐỘNG ID TIẾP THEO
    // =====================================================
    public int getNextId() {
        String sql = "SELECT ISNULL(MAX(id), 0) AS max_id FROM medical_records";
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // =====================================================
    // LẤY TOÀN BỘ DANH SÁCH BỆNH ÁN (ĐÃ THEO ĐÚNG DATABASE)
    // =====================================================
// =====================================================
    // LẤY TOÀN BỘ DANH SÁCH BỆNH ÁN (CẬP NHẬT LEFT JOIN CHỐNG TRỐNG VIEW)
    // =====================================================
 public List<MedicalRecordModel> getAllRecords() {
    List<MedicalRecordModel> list = new ArrayList<>();
    
    // Đã xóa u.age khỏi danh sách select
    String sql = "SELECT m.id, u.fullname, m.diagnosis, " +
                 "ts_start.min_date AS start_date, " +
                 "ts_end.max_date AS end_date, " +
                 "DATEDIFF(day, ts_start.min_date, ts_end.max_date) AS duration, " +
                 "ts_current.stage_name AS current_stage, " +
                 "u.avatar " +
                 "FROM medical_records m " +
                 "LEFT JOIN users u ON m.user_id = u.id " +
                 "LEFT JOIN (SELECT treatment_route_id, MIN(appointment_date) as min_date FROM treatment_stages GROUP BY treatment_route_id) ts_start ON m.treatment_route_id = ts_start.treatment_route_id " +
                 "LEFT JOIN (SELECT treatment_route_id, MAX(appointment_date) as max_date FROM treatment_stages GROUP BY treatment_route_id) ts_end ON m.treatment_route_id = ts_end.treatment_route_id " +
                 "LEFT JOIN (SELECT treatment_route_id, stage_name FROM treatment_stages WHERE status = N'Đang thực hiện') ts_current ON m.treatment_route_id = ts_current.treatment_route_id " +
                 "ORDER BY m.created_at DESC";

    try (Connection conn = getDbConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        while (rs.next()) {
            String startDate = (rs.getDate("start_date") != null) ? sdf.format(rs.getDate("start_date")) : "N/A";
            String endDate = (rs.getDate("end_date") != null) ? sdf.format(rs.getDate("end_date")) : "N/A";
            int duration = rs.getInt("duration");
            String stage = (rs.getString("current_stage") != null) ? rs.getString("current_stage") : "Chưa xác định";
            String avatar = (rs.getString("avatar") != null) ? rs.getString("avatar") : "/images/default.png";

            // Thay vì rs.getInt("age"), ta truyền 0 hoặc giá trị mặc định vì bảng không có cột tuổi
            list.add(new MedicalRecordModel(
                rs.getInt("id"),
                rs.getString("fullname"),
                rs.getString("diagnosis"),
                startDate,
                endDate,
                duration,
                stage,
                avatar
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    // =====================================================
    // LẤY DANH SÁCH GIAI ĐOẠN THEO LỘ TRÌNH (TỪ BẢNG treatment_stages)
    // =====================================================
    public List<String> getTreatmentStages(String diseaseName) {
        List<String> stages = new ArrayList<>();
        if (diseaseName == null || diseaseName.trim().isEmpty()) {
            return stages; 
        }

        // Lấy danh sách tên giai đoạn dựa vào bảng liên kết trong Database của bạn
        String sql = "SELECT ts.stage_name " +
                     "FROM treatment_stages ts " +
                     "JOIN treatment_routes tr ON ts.treatment_route_id = tr.id " +
                     "WHERE LOWER(tr.title) LIKE LOWER(?) " +
                     "ORDER BY ts.sequence_order ASC";
        
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + diseaseName.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stages.add(rs.getString("stage_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stages;
    }
    //lay danh sach giai doan theo lo trinh
    public List<String> getTitleRoadMap() {
        List<String> stages = new ArrayList<>();
        String sql = "SELECT title FROM treatment_routes ORDER BY id ASC";
        
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                stages.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stages;  
    }

    // =====================================================
    // THÊM MỚI HỒ SƠ BỆNH ÁN
    // =====================================================
    public boolean addRecord(MedicalRecordModel record) {
        // SQL Server thực tế yêu cầu chèn vào các trường thực thể sẵn có
        String sql = "INSERT INTO medical_records (user_id, doctor_id, diagnosis, h ) " +
                     "VALUES ((SELECT TOP 1 id FROM users WHERE fullname = ?), " +
                     "        (SELECT TOP 1 id FROM doctors WHERE user_id = ?), ?, GETDATE())";
        
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, record.getPatientName());
            ps.setInt(2, this.doctorUserId != null ? this.doctorUserId : 1); // Fallback về ID bác sĩ mặc định nếu null
            ps.setString(3, record.getDisease());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================
    // CẬP NHẬT HỒ SƠ BỆNH ÁN
    // =====================================================
 public boolean updateRecord(MedicalRecordModel record) {
    // Lưu ý: Cần chắc chắn các bảng liên quan (treatment_stages) đã được cập nhật logic
    // Ở đây tôi cập nhật các trường cơ bản của bảng medical_records
    String sql = "UPDATE medical_records SET diagnosis = ? WHERE id = ?";
    
    try (Connection conn = getDbConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, record.getDisease()); // Cập nhật bệnh lý
        ps.setInt(2, record.getId());         // Điều kiện ID
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // =====================================================
    // XÓA HỒ SƠ BỆNH ÁN
    // =====================================================
    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM medical_records WHERE id = ?";
        
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}