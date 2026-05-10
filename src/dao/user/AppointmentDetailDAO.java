package dao.user;

import config.DBConnection;
import model.AppointmentDetail;

import java.sql.*;

public class AppointmentDetailDAO {

    public AppointmentDetail getDetailById(
            int appointmentId
    ) {

        String sql =
            "SELECT u.fullname AS p_name, " +
            "u.phone AS p_phone, " +
            "u.birth_date AS p_dob, " +
            "u.address AS p_address, " +
            "s.name AS service_name, " +
            "du.fullname AS doctor_name, " +
            "a.appointment_date, " +
            "a.status " +
            "FROM appointments a " +
            "LEFT JOIN users u ON a.user_id = u.id " +
            "LEFT JOIN services s ON a.service_id = s.id " +
            "LEFT JOIN doctors d ON a.doctor_id = d.id " +
            "LEFT JOIN users du ON d.user_id = du.id " +
            "WHERE a.id = ?";

        try (
            Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {

            ps.setInt(1, appointmentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String dob = "Chưa cập nhật";

                if (rs.getDate("p_dob") != null) {

                    dob =
                        new java.text.SimpleDateFormat(
                                "dd/MM/yyyy"
                        ).format(
                                rs.getDate("p_dob")
                        );
                }

                String date = "";

                String time = "";

                Timestamp ts =
                        rs.getTimestamp(
                                "appointment_date"
                        );

                if (ts != null) {

                    java.time.LocalDateTime dt =
                            ts.toLocalDateTime();

                    date =
                        dt.format(
                            java.time.format
                            .DateTimeFormatter
                            .ofPattern("dd/MM/yyyy")
                        );

                    time =
                        dt.format(
                            java.time.format
                            .DateTimeFormatter
                            .ofPattern("HH:mm")
                        );
                }

                return new AppointmentDetail(
                        appointmentId,
                        rs.getString("p_name"),
                        rs.getString("p_phone"),
                        dob,
                        rs.getString("p_address"),
                        rs.getString("service_name"),
                        rs.getString("doctor_name"),
                        date,
                        time,
                        rs.getString("status")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public void cancelAppointment(
            int appointmentId
    ) {

        String sql =
            "UPDATE appointments " +
            "SET status = 'Cancelled' " +
            "WHERE id = ?";

        try (
            Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)
        ) {

            ps.setInt(1, appointmentId);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}