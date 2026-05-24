package controller.admin;

import model.admin.RoadmapModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    // Thêm roadmap
    public boolean insertRoadmap(RoadmapModel roadmap) {

    try {
        Connection conn = DBConnection.getConnection();

        String sql =
                "INSERT INTO treatment_routes(title, description) VALUES (?, ?)";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setString(1, roadmap.getTitle());
        ps.setString(2, roadmap.getDescription());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
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