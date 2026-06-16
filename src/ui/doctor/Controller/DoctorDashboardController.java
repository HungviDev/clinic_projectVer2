package ui.doctor.Controller;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import ui.doctor.Model.AppointmentModel;
import ui.doctor.Model.DoctorDashboardModel;

public class DoctorDashboardController {

    // =====================================================
    // TẢI DỮ LIỆU TỔNG QUAN (KPI CARDS) - ĐÃ FIX THEO DOCTOR USER_ID
    // =====================================================
    public DoctorDashboardModel getDashboardData(int doctorUserId) {
        DoctorDashboardModel model = new DoctorDashboardModel();
        
        // 1. Chỉ đếm những lịch hẹn HÔM NAY và ĐÃ DUYỆT (approved) của bác sĩ này
     String sqlAppointmentsToday =
    "SELECT COUNT(*) " +
    "FROM appointments a " +
    "JOIN doctors d ON a.doctor_id = d.id " +
    "WHERE d.user_id = ? " +
    "AND CAST(a.appointment_date AS DATE) = CAST(GETDATE() AS DATE)";
        // 2. Số bệnh nhân bác sĩ này đang điều trị (Dựa trên bệnh án do bác sĩ phụ trách)
        String sqlPatientsTreating = 
                "SELECT COUNT(DISTINCT r.user_id) FROM medical_records r " +
                "JOIN doctors d ON r.doctor_id = d.id " +
                "WHERE d.user_id = ?"; 
                
        // 3. Ca hoàn thành trong tháng (Đã chuyển sang trạng thái chuẩn 'completed')
        String sqlCompletedMonth = 
                "SELECT COUNT(*) FROM appointments a " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "WHERE d.user_id = ? AND a.status = 'completed' " +
                "AND MONTH(a.appointment_date) = MONTH(GETDATE()) " +
                "AND YEAR(a.appointment_date) = YEAR(GETDATE())";
                
        // 4. Doanh thu thực tế phát sinh trong ngày hôm nay của riêng bác sĩ này
        // 4. Doanh thu thực tế phát sinh trong ngày hôm nay của riêng bác sĩ này
String sqlRevenueToday = 
    "SELECT ISNULL(SUM(p.amount), 0) " +
    "FROM payments p " +
    "JOIN treatment_stages ts ON p.treatment_stage_id = ts.id " +
    "JOIN medical_records mr ON ts.treatment_route_id = mr.treatment_route_id " +
    "JOIN doctors d ON mr.doctor_id = d.id " +
    "WHERE d.user_id = ? " +
    "AND MONTH(p.created_at) = MONTH(GETDATE()) " +
    "AND YEAR(p.created_at) = YEAR(GETDATE())";
        try (Connection conn = DBConnection.getConnection()) {
            
            // 1. Số lịch hẹn hôm nay
            try (PreparedStatement ps = conn.prepareStatement(sqlAppointmentsToday)) {
                ps.setInt(1, doctorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) model.setTotalAppointmentsToday(rs.getInt(1));
                }
            }
            
            // 2. Số bệnh nhân đang điều trị
            try (PreparedStatement ps = conn.prepareStatement(sqlPatientsTreating)) {
                ps.setInt(1, doctorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) model.setTotalPatientsTreating(rs.getInt(1));
}
            }
            
            // 3. Ca hoàn thành trong tháng
            try (PreparedStatement ps = conn.prepareStatement(sqlCompletedMonth)) {
                ps.setInt(1, doctorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) model.setCompletedCasesMonth(rs.getInt(1));
                }
            }
            
            // 4. Doanh thu trong ngày
            try (PreparedStatement ps = conn.prepareStatement(sqlRevenueToday)) {
                ps.setInt(1, doctorUserId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) model.setRevenueToday(rs.getDouble(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    // =====================================================
    // TẢI LỊCH HẸN HÔM NAY - CHỈ LẤY LỊCH ĐÃ DUYỆT (APPROVED)
    // =====================================================
    public List<AppointmentModel> getTodayAppointments(int doctorUserId) {
        List<AppointmentModel> list = new ArrayList<>();
       String sql =
    "SELECT a.id, " +
    "FORMAT(a.appointment_date, 'HH:mm') AS time, " +
    "u.fullname AS patient_name, " +
    "s.name AS service_name, " +
    "a.status " +
    "FROM appointments a " +
    "JOIN users u ON a.user_id = u.id " +
    "JOIN services s ON a.service_id = s.id " +
    "JOIN doctors d ON a.doctor_id = d.id " +
    "WHERE d.user_id = ? " +
    "AND CAST(a.appointment_date AS DATE) = CAST(GETDATE() AS DATE) " +
    "ORDER BY a.appointment_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AppointmentModel ap = new AppointmentModel(
                        rs.getInt("id"),
                        rs.getString("time"),
                        rs.getString("patient_name"),
                        rs.getString("service_name"),
                        rs.getString("status")
                    );
                    list.add(ap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // =====================================================
    // VÔ HIỆU HÓA HÀM UPDATE STATUS TẠI ĐÂY VÌ KHÔNG DÙNG NỮA
    // =====================================================
    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        return false; 
    }
}