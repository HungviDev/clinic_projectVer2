package dao.user;

import config.DBConnection;
import model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingDAO {

    public boolean saveBooking(Booking booking) {

        String sql =
                "INSERT INTO appointments " +
                "(user_id, doctor_id, service_id, appointment_date, status, note, created_at, status_stage) " +
                "VALUES (?, ?, ?, ?, 'Pending', ?, GETDATE(), ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getDoctorId());
            ps.setInt(3, booking.getServiceId());
            ps.setTimestamp(4, booking.getAppointmentDate());
            ps.setString(5, booking.getNote());
            ps.setString(6, booking.getstatusStage());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getNextTreatmentStageFromDB(int userId, int serviceId) {
        String sql = "SELECT TOP 1 ts.stage_name, tp.title " +
                     "FROM treatment_stages ts " +
                     "JOIN treatment_plans tp ON ts.treatment_plan_id = tp.id " +
                     "JOIN medical_records mr ON tp.medical_record_id = mr.id " +
                     "WHERE mr.user_id = ? " +
                     "  AND mr.service_id = ? " + 
                     "  AND ts.status IN (N'In Progress', N'Pending') " +
                     "  AND tp.status != N'Completed' " +
                     "ORDER BY ts.sequence_order ASC";;
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, userId);
            ps.setInt(2, serviceId); // Thiết lập kiểu dữ liệu INT cho service_id
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String stageName = rs.getString("stage_name");
                String planTitle = rs.getString("title");
                return planTitle + " ➔ " + stageName; 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
}