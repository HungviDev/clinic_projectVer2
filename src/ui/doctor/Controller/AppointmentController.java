
package ui.doctor.Controller;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ui.doctor.Model.DentalAppointmentModel;

public class AppointmentController {

    // ==================================================
    // CÁC HÀM CŨ ĐÃ CÓ (GIỮ NGUYÊN)
    // ==================================================
    public List<DentalAppointmentModel> getAppointmentsByDoctor(int doctorUserId) {
        List<DentalAppointmentModel> list = new ArrayList<>();
        String sql = """
            SELECT a.id, u.fullname AS patient_name, CONVERT(VARCHAR(10), a.appointment_date, 120) AS appointment_date,
                   CONVERT(VARCHAR(5), a.appointment_date, 108) AS appointment_time, ISNULL(s.name, '') AS problem, ISNULL(a.status, N'Chờ khám') AS status
            FROM appointments a
            INNER JOIN users u ON a.user_id = u.id
            LEFT JOIN doctors d ON a.doctor_id = d.id
            LEFT JOIN services s ON a.service_id = s.id
            WHERE d.user_id = ? OR a.doctor_id IS NULL OR LOWER(a.status) = 'pending' OR a.status = N'Chờ khám'
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
                list.add(ap);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addAppointment(DentalAppointmentModel ap) {
        String sql = "INSERT INTO appointments (user_id, service_id, appointment_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, findUserIdByName(ap.getPatientName()));
            ps.setInt(2, findServiceIdByName(ap.getProblem()));
            ps.setString(3, ap.getAppointmentDate().trim() + " " + ap.getAppointmentTime().trim());
            ps.setString(4, ap.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateAppointment(DentalAppointmentModel ap) {
        String sql = "UPDATE appointments SET user_id = ?, service_id = ?, appointment_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, findUserIdByName(ap.getPatientName()));
            ps.setInt(2, findServiceIdByName(ap.getProblem()));
            ps.setString(3, ap.getAppointmentDate().trim() + " " + ap.getAppointmentTime().trim());
            ps.setString(4, ap.getStatus());
            ps.setInt(5, ap.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateStatus(int appointmentId, String newStatus, int doctorUserId) {
        int realDoctorId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psDoc = conn.prepareStatement("SELECT id FROM doctors WHERE user_id = ?")) {
            psDoc.setInt(1, doctorUserId);
            ResultSet rsDoc = psDoc.executeQuery();
            if (rsDoc.next()) realDoctorId = rsDoc.getInt("id");
        } catch (Exception e) { e.printStackTrace(); }

        String sqlUpdate = "UPDATE appointments SET status = ?, doctor_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
            ps.setString(1, newStatus);
            if (realDoctorId != -1) ps.setInt(2, realDoctorId); else ps.setNull(2, java.sql.Types.INTEGER);
            ps.setInt(3, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ==================================================
    // PHẦN BỔ SUNG LOGIC TỰ ĐỘNG TẠO BỆNH ÁN
    // ==================================================
    public void handleAppointmentCompletion(int appointmentId) {
        DentalAppointmentModel appt = getAppointmentById(appointmentId);
        if (appt != null) {
            int userId = findUserIdByName(appt.getPatientName());
            if (!checkIfRecordExists(userId)) {
                String sql = "INSERT INTO medical_records (user_id, doctor_id, diagnosis, service_id, created_at) VALUES (?, ?, ?, ?, GETDATE())";
                try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, 1); // Lưu ý: Bạn có thể cần lấy chính xác ID bác sĩ ở đây
                    ps.setString(3, "Chẩn đoán: " + appt.getProblem());
                    ps.setInt(4, findServiceIdByName(appt.getProblem()));
                    ps.executeUpdate();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private boolean checkIfRecordExists(int userId) {
        String sql = "SELECT id FROM medical_records WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ==================================================
    // CÁC HÀM TIỆN ÍCH (GIỮ NGUYÊN)
    // ==================================================
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
}