package ui.doctor.Controller;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ui.doctor.Model.DentalAppointmentModel;
import ui.doctor.View.MedicalRecordView;

public class AppointmentController {

    // 1. LẤY DANH SÁCH LỊCH KHÁM
    public List<DentalAppointmentModel> getAppointmentsByDoctor(int doctorUserId) {
        List<DentalAppointmentModel> list = new ArrayList<>();
        String sql = """
            SELECT a.id, u.fullname AS patient_name, 
                   CONVERT(VARCHAR(10), a.appointment_date, 120) AS appointment_date,
                   CONVERT(VARCHAR(5), a.appointment_date, 108) AS appointment_time, 
                   ISNULL(s.name, '') AS problem, 
                   ISNULL(a.status, N'Chờ khám') AS status,
                   
                 
                   ISNULL(a.stage_name, N'Chưa có') AS stage_name 
                   
            FROM appointments a
            INNER JOIN users u ON a.user_id = u.id
            INNER JOIN doctors d ON a.doctor_id = d.id
            LEFT JOIN services s ON a.service_id = s.id
            WHERE d.user_id = ?
            ORDER BY a.appointment_date DESC, a.id DESC
        """;
        
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DentalAppointmentModel ap = new DentalAppointmentModel();
                ap.setId(rs.getInt("id"));
                ap.setPatientName(rs.getString("patient_name"));
                ap.setAppointmentDate(rs.getString("appointment_date"));
                ap.setAppointmentTime(rs.getString("appointment_time"));
                ap.setProblem(rs.getString("problem"));
                ap.setStatus(rs.getString("status"));
                
                // THIẾU DÒNG NÀY: Lấy dữ liệu từ db gán vào object
                ap.setStageName(rs.getString("stage_name")); 
                
                list.add(ap);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // 2. THÊM LỊCH KHÁM (ĐÃ CHẶN LỖI)
    public boolean addAppointment(DentalAppointmentModel ap) {
        if (ap.getPatientName() == null || ap.getPatientName().trim().isEmpty()) return false;

        int userId = findUserIdByName(ap.getPatientName());
        if (userId == 0) userId = createNewUser(ap.getPatientName());
        if (userId <= 0) return false;

        int serviceId = findServiceIdByName(ap.getProblem());
        // Nếu dịch vụ không tồn tại, để là NULL (hoặc 1 nếu DB bắt buộc)
        String sql = "INSERT INTO appointments (user_id, service_id, appointment_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            if (serviceId > 0) ps.setInt(2, serviceId); else ps.setNull(2, java.sql.Types.INTEGER);
            ps.setString(3, ap.getAppointmentDate().trim() + " " + ap.getAppointmentTime().trim());
            ps.setString(4, ap.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 3. TẠO MỚI BỆNH NHÂN
    private int createNewUser(String name) {
        String sql = "INSERT INTO users (fullname, role_id) VALUES (?, 3)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public boolean updateStatus(int appointmentId, String newStatus, int doctorUserId) {
        int realDoctorId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM doctors WHERE user_id = ?")) {
            ps.setInt(1, doctorUserId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) realDoctorId = rs.getInt("id");
        } catch (Exception e) { e.printStackTrace(); }

       String sqlUpdate = "UPDATE appointments SET status = ?, doctor_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
            ps.setString(1, newStatus);
            if (realDoctorId != -1) ps.setInt(2, realDoctorId); else ps.setNull(2, Types.INTEGER);
            ps.setInt(3, appointmentId);
            boolean success = ps.executeUpdate() > 0;
            
            // Chỉ đổ dữ liệu xuống bệnh án khi trạng thái là "completed"
            if (success && newStatus.equalsIgnoreCase("completed")) {
                handleAppointmentCompletion(appointmentId, realDoctorId);
            }
            return success;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // Hàm xử lý khi hoàn thành ca khám
    private void handleFinalizeTreatment(int appointmentId) {
    
        System.out.println("Đang hoàn tất hồ sơ cho lịch hẹn ID: " + appointmentId);
    }
    // 5. TỰ ĐỘNG TẠO HỒ SƠ BỆNH ÁN
    public void handleAppointmentCompletion(int appointmentId, int realDoctorId) {
        DentalAppointmentModel appt = getAppointmentById(appointmentId);
        if (appt != null) {
            int userId = findUserIdByName(appt.getPatientName());
            String fullDate = appt.getAppointmentDate() + " " + appt.getAppointmentTime() + ":00";
            String sql = "INSERT INTO medical_records (user_id, doctor_id, diagnosis, created_at) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, (realDoctorId != -1) ? realDoctorId : 1);
                ps.setString(3, "Chẩn đoán: " + appt.getProblem());
                ps.setString(4, fullDate);
                ps.executeUpdate();
                MedicalRecordView.refreshData();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // --- TIỆN ÍCH HỖ TRỢ (GIỮ NGUYÊN) ---
    public DentalAppointmentModel getAppointmentById(int id) {
        String sql = "SELECT a.id, u.fullname AS patient_name, CONVERT(VARCHAR(10), a.appointment_date, 120) AS appointment_date, CONVERT(VARCHAR(5), a.appointment_date, 108) AS appointment_time, ISNULL(s.name, '') AS problem, a.status FROM appointments a JOIN users u ON a.user_id = u.id LEFT JOIN services s ON a.service_id = s.id WHERE a.id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DentalAppointmentModel ap = new DentalAppointmentModel();
                ap.setId(rs.getInt("id"));
                ap.setPatientName(rs.getString("patient_name"));
                ap.setAppointmentDate(rs.getString("appointment_date"));
                ap.setAppointmentTime(rs.getString("appointment_time"));
                ap.setProblem(rs.getString("problem"));
                ap.setStatus(rs.getString("status"));
                return ap;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public int findUserIdByName(String name) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE fullname = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int findServiceIdByName(String name) {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT id FROM services WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
    // HÀM MỚI: Lấy danh sách tên bệnh nhân theo bác sĩ (dùng cho JComboBox)
    public List<String> getPatientNamesByDoctor(int doctorUserId) {
        List<String> names = new ArrayList<>();
        // Lấy doctor_id từ user_id của bác sĩ
        String sql = """
            SELECT DISTINCT u.fullname 
            FROM users u
            JOIN appointments a ON u.id = a.user_id
            JOIN doctors d ON a.doctor_id = d.id
            WHERE d.user_id = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("fullname"));
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return names;
    }
    // Hàm lấy danh sách tên dịch vụ để chọn
    public List<String> getAllServiceNames() {
        List<String> services = new ArrayList<>();
        String sql = "SELECT name FROM services";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                services.add(rs.getString("name"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return services;
    }
}