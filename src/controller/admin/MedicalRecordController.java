package controller.admin;

import config.DBConnection;
import ui.admin.ItinereryView.PatientComboItem;
import ui.admin.ItinereryView.ServiceComboItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordController {

    // 1. Lấy danh sách Khách hàng đang có Bệnh án dở dang (Chưa có Lộ trình)
    public List<PatientComboItem> getPatientsNeedingRoute() {
        List<PatientComboItem> list = new ArrayList<>();
        String sql = """
            SELECT DISTINCT u.id, u.fullname 
            FROM users u
            INNER JOIN medical_records mr ON u.id = mr.user_id
            WHERE mr.treatment_route_id IS NULL
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PatientComboItem(rs.getInt("id"), rs.getString("fullname")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy danh sách Dịch vụ (Bệnh án) của một Khách hàng cụ thể
    public List<ServiceComboItem> getUnroutedServicesByPatient(int userId) {
        List<ServiceComboItem> list = new ArrayList<>();
        String sql = """
            SELECT mr.id AS medical_record_id, s.name AS service_name
            FROM medical_records mr
            INNER JOIN services s ON mr.service_id = s.id
            WHERE mr.user_id = ? AND mr.treatment_route_id IS NULL
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ServiceComboItem(rs.getInt("medical_record_id"), rs.getString("service_name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}