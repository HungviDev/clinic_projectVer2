package dao.user;

import config.DBConnection;
import model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
}