package controller.admin;

import model.admin.RoadmapModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement; // Bổ sung thư viện này để lấy ID vừa tạo
import java.util.ArrayList;
import java.util.List;

import config.DBConnection;

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
}  