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
    // TẢI DỮ LIỆU TỔNG QUAN (KPI CARDS)
    // =====================================================
    public DoctorDashboardModel getDashboardData(int doctorId) {
        DoctorDashboardModel model = new DoctorDashboardModel();
        
        String sqlAppointmentsToday = 
                "SELECT COUNT(*) FROM appointments " +
                "WHERE doctor_id = ? AND CAST(appointment_date AS DATE) = CAST(GETDATE() AS DATE)";
                
        String sqlPatientsTreating = 
                "SELECT COUNT(DISTINCT user_id) FROM medical_records " +
                "WHERE doctor_id = ?"; 
                
        String sqlCompletedMonth = 
                "SELECT COUNT(*) FROM appointments " +
                "WHERE doctor_id = ? AND status = 'done' " +
                "AND MONTH(appointment_date) = MONTH(GETDATE()) " +
                "AND YEAR(appointment_date) = YEAR(GETDATE())";
                
        String sqlRevenueToday = 
                "SELECT ISNULL(SUM(p.amount), 0) " +
                "FROM payments p " +
                "JOIN appointments a ON p.appointment_id = a.id " +
                "WHERE a.doctor_id = ? AND CAST(p.created_at AS DATE) = CAST(GETDATE() AS DATE)";

        try (Connection conn = DBConnection.getConnection()) {
            
            // 1. Số lịch hẹn hôm nay
            try (PreparedStatement ps = conn.prepareStatement(sqlAppointmentsToday)) {
                ps.setInt(1, doctorId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) model.setTotalAppointmentsToday(rs.getInt(1));
                }
            }
            
            // 2. Số bệnh nhân đang điều trị (Dựa trên bệnh án do bác sĩ phụ trách)
            try (PreparedStatement ps = conn.prepareStatement(sqlPatientsTreating)) {
                ps.setInt(1, doctorId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) model.setTotalPatientsTreating(rs.getInt(1));
                }
            }
            
            // 3. Ca hoàn thành trong tháng
            try (PreparedStatement ps = conn.prepareStatement(sqlCompletedMonth)) {
                ps.setInt(1, doctorId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) model.setCompletedCasesMonth(rs.getInt(1));
                }
            }
            
            // 4. Doanh thu thực tế phát sinh trong ngày hôm nay
            try (PreparedStatement ps = conn.prepareStatement(sqlRevenueToday)) {
                ps.setInt(1, doctorId);
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
    // TẢI LỊCH HẸN CHỈ TRONG NGÀY HÔM NAY
    // =====================================================
    public List<AppointmentModel> getTodayAppointments(int doctorId) {
        List<AppointmentModel> list = new ArrayList<>();
        String sql = "SELECT a.id, " +
                     "FORMAT(a.appointment_date, 'HH:mm') AS time, " +
                     "u.fullname AS patient_name, " +
                     "s.name AS service_name, " +
                     "a.status " +
                     "FROM appointments a " +
                     "JOIN users u ON a.user_id = u.id " +
                     "JOIN services s ON a.service_id = s.id " +
                     "WHERE a.doctor_id = ? " +
                     "AND CAST(a.appointment_date AS DATE) = CAST(GETDATE() AS DATE) " +
                     "ORDER BY a.appointment_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorId);
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
    // CẬP NHẬT TRẠNG THÁI KHÔNG GIỚI HẠN BỞI 'PENDING'
    // =====================================================
    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        // Cho phép cập nhật linh hoạt từ 'approved' sang 'done' hoặc 'reject'
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newStatus);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}