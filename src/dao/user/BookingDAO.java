package dao.user;

import config.DBConnection;
import model.user.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingDAO {

    public boolean saveBooking(Booking booking) {

        String sql =
                "INSERT INTO appointments " +
                "(user_id, doctor_id, service_id, appointment_date, status, note, created_at) " +
                "VALUES (?, ?, ?, ?, 'Pending', ?, GETDATE())";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getDoctorId());
            ps.setInt(3, booking.getServiceId());
            ps.setTimestamp(4, booking.getAppointmentDate());
            ps.setString(5, booking.getNote());
            

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getNextTreatmentStageFromDB(int userId, int serviceId) {
        String sql = "SELECT TOP 1 ts.stage_name, tr.title " +
                    "FROM treatment_stages ts " +
                    "JOIN treatment_routes tr ON ts.treatment_route_id = tr.id " +
                    "JOIN medical_records mr ON mr.treatment_route_id = tr.id " +
                    "WHERE mr.user_id = ? " +
                    "  AND mr.service_id = ? " + 
                    "  AND ts.status IN ( N'Chưa thực hiện', N'Đang thực hiện' ) " +
                    "ORDER BY ts.sequence_order ASC";
                     
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