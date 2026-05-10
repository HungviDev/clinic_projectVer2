package dao.user;

import config.DBConnection;
import model.user.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // ================= LẤY NGÀY ĐÃ CÓ LỊCH =================
    public List<LocalDate> getBookedDates(int userId) {

        List<LocalDate> dates = new ArrayList<>();

        String sql =
                "SELECT appointment_date " +
                "FROM appointments " +
                "WHERE user_id = ? " +
                "AND status != 'Cancelled'";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Timestamp ts =
                        rs.getTimestamp("appointment_date");

                if (ts != null) {
                    dates.add(
                            ts.toLocalDateTime().toLocalDate()
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dates;
    }

    // ================= LẤY LỊCH HẸN THEO NGÀY =================
    public List<Appointment> getAppointmentsByDate(
            int userId,
            LocalDate date
    ) {

        List<Appointment> list = new ArrayList<>();

        String sql =
                "SELECT a.id, " +
                "s.name AS service_name, " +
                "a.appointment_date, " +
                "a.status " +
                "FROM appointments a " +
                "LEFT JOIN services s ON a.service_id = s.id " +
                "WHERE a.user_id = ? " +
                "AND CAST(a.appointment_date AS DATE) = ? " +
                "AND a.status != 'Cancelled'";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);
            ps.setDate(2, java.sql.Date.valueOf(date));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Appointment app = new Appointment();

                app.setId(rs.getInt("id"));
                app.setServiceName(
                        rs.getString("service_name")
                );
                app.setAppointmentDate(
                        rs.getTimestamp("appointment_date")
                );
                app.setStatus(
                        rs.getString("status")
                );

                list.add(app);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Appointment> getAppointmentsByUserId(int userId) {

        List<Appointment> list = new ArrayList<>();

        String sql =
                "SELECT a.id, a.appointment_date, a.status, a.note, " +
                "s.name as service_name, " +
                "du.fullname as doctor_name " +
                "FROM appointments a " +
                "LEFT JOIN services s ON a.service_id = s.id " +
                "LEFT JOIN doctors d ON a.doctor_id = d.id " +
                "LEFT JOIN users du ON d.user_id = du.id " +
                "WHERE a.user_id = ? " +
                "ORDER BY a.appointment_date DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Appointment app = new Appointment(
                        rs.getInt("id"),
                        rs.getTimestamp("appointment_date"),
                        rs.getString("status"),
                        rs.getString("note"),
                        rs.getString("service_name"),
                        rs.getString("doctor_name")
                );

                list.add(app);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}