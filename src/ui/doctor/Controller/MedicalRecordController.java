
package ui.doctor.Controller;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ui.doctor.Model.MedicalRecordModel;
import ui.doctor.View.MedicalRecordDialog.RouteItem;

public class MedicalRecordController {

    private final Integer doctorUserId;

    private Connection getDbConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public MedicalRecordController(Integer doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    // 1. LẤY ID LỘ TRÌNH VÀ TÊN ĐỂ NẠP VÀO COMBOBOX
    public List<RouteItem> getRouteList() {
        List<RouteItem> list = new ArrayList<>();
        String sql = "SELECT id, title FROM treatment_routes";
        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new RouteItem(rs.getInt("id"), rs.getString("title")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. LẤY TẤT CẢ HỒ SƠ (ĐÃ XỬ LÝ LOGIC TRẠNG THÁI)
    public List<MedicalRecordModel> getAllRecords() {
        List<MedicalRecordModel> list = new ArrayList<>();
        
        String sql = """
            SELECT m.id, u.fullname, m.diagnosis, m.created_at, 
                   m.treatment_route_id, tr.title AS route_title, u.avatar,
                   (SELECT TOP 1 stage_name 
                    FROM treatment_stages ts 
                    WHERE ts.treatment_route_id = m.treatment_route_id 
                      AND ts.status <> N'Đã hoàn thành' 
                    ORDER BY sequence_order ASC) AS current_stage
            FROM medical_records m 
            INNER JOIN doctors d ON m.doctor_id = d.id
            LEFT JOIN users u ON m.user_id = u.id 
            LEFT JOIN treatment_routes tr ON m.treatment_route_id = tr.id 
            WHERE d.user_id = ? 
            ORDER BY m.created_at DESC
        """;

        int idToFilter = (this.doctorUserId != null) ? this.doctorUserId : 1;

        try (Connection conn = getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idToFilter);
            
            try (ResultSet rs = ps.executeQuery()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

                while (rs.next()) {
                    int routeId = rs.getInt("treatment_route_id");
                    String stageFromDb = rs.getString("current_stage");
                    String routeTitle = rs.getString("route_title") != null ? rs.getString("route_title") : "Chưa chọn lộ trình";
                    
                    // --- XỬ LÝ LOGIC HIỂN THỊ TRẠNG THÁI ---
                    String currentStageDisplay;
                    if (routeId == 0) {
                        currentStageDisplay = "Chưa có lộ trình";
                    } else if (stageFromDb == null || stageFromDb.isEmpty()) {
                        currentStageDisplay = "Đã hoàn thành lộ trình";
                    } else {
                        currentStageDisplay = stageFromDb;
                    }
                    // --------------------------------------

                    MedicalRecordModel model = new MedicalRecordModel(
                        rs.getInt("id"),
                        rs.getString("fullname") != null ? rs.getString("fullname") : "Khách vãng lai",
                        rs.getString("diagnosis") != null ? rs.getString("diagnosis") : "Chưa có chẩn đoán",
                        (rs.getDate("created_at") != null) ? sdf.format(rs.getDate("created_at")) : "N/A",
                        routeId,
                        routeTitle,
                        (rs.getString("avatar") == null || rs.getString("avatar").isEmpty()) ? "/images/default.png" : rs.getString("avatar")
                    );
                    
                    model.setCurrentStage(currentStageDisplay);
                    list.add(model);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. THÊM MỚI
    public boolean addRecord(MedicalRecordModel record, int routeId) {
        String sql = "INSERT INTO medical_records (user_id, doctor_id, diagnosis, treatment_route_id, created_at) " +
                     "VALUES ((SELECT TOP 1 id FROM users WHERE fullname = ?), (SELECT TOP 1 id FROM doctors WHERE user_id = ?), ?, ?, GETDATE())";
        try (Connection conn = getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getPatientName());
            ps.setInt(2, this.doctorUserId != null ? this.doctorUserId : 1);
            ps.setString(3, record.getDisease());
            ps.setInt(4, routeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 4. CẬP NHẬT
    public boolean updateRecord(MedicalRecordModel record, int routeId) {
        String sql = "UPDATE medical_records SET diagnosis = ?, treatment_route_id = ? WHERE id = ?";
        try (Connection conn = getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getDisease());
            ps.setInt(2, routeId);
            ps.setInt(3, record.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 5. LẤY ID TỰ ĐỘNG
    public int getNextId() {
        String sql = "SELECT ISNULL(MAX(id), 0) AS max_id FROM medical_records";
        try (Connection conn = getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("max_id") + 1;
        } catch (SQLException e) { e.printStackTrace(); }
        return 1;
    }

    // 6. XÓA BẢN GHI
    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM medical_records WHERE id = ?";
        try (Connection conn = getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 7. LẤY CÁC BƯỚC CHƯA HOÀN THÀNH
    public List<ui.doctor.Model.TreatmentStageModel> getActiveStagesByRoute(int routeId) {
        List<ui.doctor.Model.TreatmentStageModel> list = new ArrayList<>();
        String sql = "SELECT id, treatment_route_id, stage_name, sequence_order, status, note FROM treatment_stages WHERE treatment_route_id = ? AND status <> N'Đã hoàn thành' ORDER BY sequence_order ASC";
        try (Connection conn = getDbConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ui.doctor.Model.TreatmentStageModel(rs.getInt("id"), rs.getInt("treatment_route_id"), rs.getString("stage_name"), rs.getInt("sequence_order"), rs.getString("status"), rs.getString("note")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 8. LOGIC CHUYỂN GIAI ĐOẠN TỰ ĐỘNG
    public boolean advanceStage(int currentStageId, int routeId) {
        Connection conn = null;
        try {
            conn = getDbConnection();
            conn.setAutoCommit(false);
            
            // 1. Cập nhật giai đoạn hiện tại thành Đã hoàn thành
            try (PreparedStatement ps1 = conn.prepareStatement("UPDATE treatment_stages SET status = N'Đã hoàn thành', updated_at = GETDATE() WHERE id = ?")) {
                ps1.setInt(1, currentStageId);
                ps1.executeUpdate();
            }
            
            // 2. Tìm giai đoạn tiếp theo
            int nextStageId = -1;
            try (PreparedStatement ps2 = conn.prepareStatement("SELECT TOP 1 id FROM treatment_stages WHERE treatment_route_id = ? AND status <> N'Đã hoàn thành' ORDER BY sequence_order ASC")) {
                ps2.setInt(1, routeId);
                try (ResultSet rs = ps2.executeQuery()) { 
                    if (rs.next()) nextStageId = rs.getInt("id"); 
                }
            }
            
            if (nextStageId != -1) {
                // 3. Có giai đoạn tiếp theo -> cập nhật nó thành Đang thực hiện
                try (PreparedStatement ps3 = conn.prepareStatement("UPDATE treatment_stages SET status = N'Đang thực hiện', updated_at = GETDATE() WHERE id = ?")) {
                    ps3.setInt(1, nextStageId);
                    ps3.executeUpdate();
                }
            } else {
                // KHÔNG CÒN GIAI ĐOẠN NÀO NỮA -> LỘ TRÌNH ĐÃ HOÀN THÀNH TOÀN BỘ
                // Cập nhật trạng thái lịch hẹn tương ứng thành completed
                String updateApptSql = """
                    UPDATE a
                    SET a.status = 'completed'
                    FROM appointments a
                    INNER JOIN medical_records mr ON a.user_id = mr.user_id 
                                                  AND a.doctor_id = mr.doctor_id
                                                  AND CAST(a.appointment_date AS DATE) = CAST(mr.created_at AS DATE)
                    WHERE mr.treatment_route_id = ? 
                      AND a.status = 'approved'
                """;
                try (PreparedStatement ps4 = conn.prepareStatement(updateApptSql)) {
                    ps4.setInt(1, routeId);
                    int rows = ps4.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Đã cập nhật trạng thái lịch hẹn thành 'completed' do lộ trình đã hoàn thành.");
                    }
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}