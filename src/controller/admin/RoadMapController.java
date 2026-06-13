package controller.admin;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList; // Bổ sung thư viện này để lấy ID vừa tạo
import java.util.List;
import model.admin.RoadmapModel;
import model.admin.StepRoadMapModel;

public class RoadMapController {

    // Lấy tất cả roadmap
    public List<RoadmapModel> getAllRoadmap() {
        List<RoadmapModel> roadmapList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM treatment_routes";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RoadmapModel roadmap = new RoadmapModel(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("created_at")
                );
                roadmapList.add(roadmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roadmapList;
    }

    // =================================================================
    // ĐÃ SỬA HÀM NÀY: Thêm roadmap và móc ID vào medical_records
    // =================================================================
    public boolean insertRoadmap(RoadmapModel roadmap, int targetMedicalRecordId) {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;
        boolean isSuccess = false;

        try {
            conn = DBConnection.getConnection();
            
            // 1. Tắt Auto Commit để bắt đầu Transaction (Bảo toàn dữ liệu 2 bảng)
            conn.setAutoCommit(false); 

            // Bước 1: INSERT vào bảng treatment_routes
            String sqlInsert = "INSERT INTO treatment_routes(title, description, created_at) VALUES (?, ?, GETDATE())";
            
            // Cài cờ RETURN_GENERATED_KEYS để tý nữa moi cái ID ra
            psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            psInsert.setString(1, roadmap.getTitle());
            psInsert.setString(2, roadmap.getDescription());
            
            int affectedRows = psInsert.executeUpdate();

            if (affectedRows > 0) {
                // Lấy cái ID của lộ trình vừa tạo
                rs = psInsert.getGeneratedKeys();
                if (rs.next()) {
                    int newRouteId = rs.getInt(1); 

                    // Bước 2: UPDATE móc ID lộ trình vào bảng medical_records
                    String sqlUpdate = "UPDATE medical_records SET treatment_route_id = ? WHERE id = ?";
                    psUpdate = conn.prepareStatement(sqlUpdate);
                    psUpdate.setInt(1, newRouteId);
                    psUpdate.setInt(2, targetMedicalRecordId); // Mỏ neo của chúng ta đây
                    
                    psUpdate.executeUpdate();

                    // Thành công cả 2 bước -> Lưu vĩnh viễn vào DB
                    conn.commit();
                    isSuccess = true;
                }
            }
        } catch (Exception e) {
            // Nếu có bất kỳ lỗi gì ở 1 trong 2 bước -> Hủy toàn bộ, trả DB về như cũ
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Đóng các luồng kết nối
            try {
                if (rs != null) rs.close();
                if (psInsert != null) psInsert.close();
                if (psUpdate != null) psUpdate.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định cho conn
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    // Cập nhật roadmap
    public boolean updateRoadmap(RoadmapModel roadmap) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE treatment_routes SET title = ?, description = ?, created_at = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roadmap.getTitle());
            ps.setString(2, roadmap.getDescription());
            ps.setDate(3, roadmap.getDatecreate());
            ps.setInt(4, roadmap.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa roadmap
    public boolean deleteRoadmap(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM treatment_routes WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm roadmap theo id
    public RoadmapModel getRoadmapById(String id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM treatment_routes WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new RoadmapModel(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("created_at")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        RoadMapController roadmapController = new RoadMapController();
        List<RoadmapModel> roadmapList = roadmapController.getAllRoadmap();
        System.out.println(roadmapList.size());
    }
    //đây
public boolean insertStageAndCreatePayment(StepRoadMapModel step) {
    Connection conn = null;
    PreparedStatement psCheck = null;
    PreparedStatement psStage = null;
    PreparedStatement psPayment = null;
    ResultSet rs = null;

    boolean isSuccess = false;

    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);

        // ==========================================
        // BƯỚC 1: TÌM BỆNH NHÂN THEO LỘ TRÌNH
        // ==========================================
        int patientUserId = -1;

        String sqlCheck =
                "SELECT TOP 1 user_id " +
                "FROM medical_records " +
                "WHERE treatment_route_id = ?";

        psCheck = conn.prepareStatement(sqlCheck);
        psCheck.setInt(1, step.getTreatmentRouteId());

        rs = psCheck.executeQuery();

        if (rs.next()) {
            patientUserId = rs.getInt("user_id");
        }

        rs.close();

        if (patientUserId == -1) {
            System.err.println(
                    "Không tìm thấy bệnh nhân của lộ trình ID = "
                    + step.getTreatmentRouteId());

            conn.rollback();
            return false;
        }

       // ==========================================
// BƯỚC 2: THÊM GIAI ĐOẠN ĐIỀU TRỊ
// TỰ TÍNH APPOINTMENT_DATE THEO DELAY
// ==========================================
String sqlStage =
        "DECLARE @LastDate DATETIME; " +

        "SELECT TOP 1 @LastDate = appointment_date " +
        "FROM treatment_stages " +
        "WHERE treatment_route_id = ? " +
        "ORDER BY sequence_order DESC; " +

        "IF @LastDate IS NULL " +
        "SET @LastDate = GETDATE(); " +

        "INSERT INTO treatment_stages (" +
        "treatment_route_id, stage_name, sequence_order, delay, " +
        "appointment_date, cost, note, status" +
        ") VALUES (?, ?, ?, ?, DATEADD(day, ?, @LastDate), ?, ?, N'Chưa thực hiện');";

psStage = conn.prepareStatement(
        sqlStage,
        Statement.RETURN_GENERATED_KEYS
);

// Tham số cho SELECT tìm ngày gần nhất
psStage.setInt(1, step.getTreatmentRouteId());

// Tham số cho INSERT
psStage.setInt(2, step.getTreatmentRouteId());
psStage.setString(3, step.getStageName());
psStage.setInt(4, step.getSequenceOrder());
psStage.setInt(5, step.getDelay());

// DATEADD(day, ?, @LastDate)
psStage.setInt(6, step.getDelay());

psStage.setDouble(7, step.getCost());
psStage.setString(8, step.getNote());

int stageRows = psStage.executeUpdate();

        // ==========================================
        // BƯỚC 3: LẤY ID STAGE VỪA TẠO
        // ==========================================
        int newStageId = -1;

        rs = psStage.getGeneratedKeys();

        if (rs.next()) {
            newStageId = rs.getInt(1);
        }

        rs.close();

        if (newStageId == -1) {
            conn.rollback();
            return false;
        }

        // ==========================================
        // BƯỚC 4: TẠO PAYMENT
        // ==========================================
        String sqlPayment =
                "INSERT INTO payments " +
                "(user_id, treatment_stage_id, amount, method, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, GETDATE())";

        psPayment = conn.prepareStatement(sqlPayment);

        psPayment.setInt(1, patientUserId);
        psPayment.setInt(2, newStageId);
        psPayment.setDouble(3, step.getCost());
        psPayment.setString(4, "Tiền mặt");
        psPayment.setString(5, "Chưa thanh toán");

        int paymentRows = psPayment.executeUpdate();

        if (paymentRows <= 0) {
            conn.rollback();
            return false;
        }

        // ==========================================
        // BƯỚC 5: COMMIT
        // ==========================================
        conn.commit();
        isSuccess = true;

    } catch (Exception e) {

        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        System.err.println("🔥 Lỗi SQL/Java: " + e.getMessage());
        e.printStackTrace();

    } finally {

        try {
            if (rs != null) rs.close();
            if (psCheck != null) psCheck.close();
            if (psStage != null) psStage.close();
            if (psPayment != null) psPayment.close();

            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return isSuccess;
}
}  