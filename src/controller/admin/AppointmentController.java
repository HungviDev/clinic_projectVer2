package controller.admin;

import config.DBConnection;
import model.admin.AppointmentModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class AppointmentController {

    public List<AppointmentModel> getAllAppointment() {
        List<AppointmentModel> appointmentList =
                new ArrayList<>();
        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql = """
                    SELECT
                        appointments.id AS appointment_id,
                        doctor_user.fullname AS doctor_name,
                        patient_user.fullname AS patient_name,
                        appointments.appointment_date,
                        appointments.status
                    FROM appointments

                    INNER JOIN doctors
                        ON appointments.doctor_id = doctors.id

                    INNER JOIN users AS doctor_user
                        ON doctors.user_id = doctor_user.id

                    INNER JOIN users AS patient_user
                        ON appointments.user_id = patient_user.id
                    """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                AppointmentModel appointment =
                        new AppointmentModel();

                appointment.setId(
                        rs.getInt("appointment_id")
                );

                appointment.setDoctorName(
                        rs.getString("doctor_name")
                );

                appointment.setPatientName(
                        rs.getString("patient_name")
                );

                appointment.setAppointmentDate(
                        rs.getDate("appointment_date")
                );

                appointment.setStatus(
                        rs.getString("status")
                );

                appointmentList.add(appointment);
            }

            rs.close();

            ps.close();

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return appointmentList;
    }
    public int countAppointments() {

    int total = 0;

    try {

        Connection conn =
                DBConnection.getConnection();

        String sql =
                "SELECT COUNT(*) AS total FROM appointments";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery();

        if (rs.next()) {

            total = rs.getInt("total");
        }

        rs.close();

        ps.close();

        conn.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return total;
}
    public boolean insertAppointment(
        int userId,
        int doctorId,
        int serviceId,
        java.util.Date appointmentDate,
        String note
) {

    boolean check = false;

    try {

        Connection conn =
                DBConnection.getConnection();

        String sql = """
                INSERT INTO appointments
                (
                    user_id,
                    doctor_id,
                    service_id,
                    appointment_date,
                    status,
                    note,
                    created_at
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    GETDATE()
                )
                """;

        PreparedStatement ps =
                conn.prepareStatement(sql);

        java.sql.Date sqlDate =
                new java.sql.Date(
                        appointmentDate.getTime()
                );

        ps.setInt(1, userId);

        ps.setInt(2, doctorId);

        ps.setInt(3, serviceId);

        ps.setDate(4, sqlDate);

        ps.setString(5, "Pending");

        ps.setString(6, note);

        int result =
                ps.executeUpdate();

        if (result > 0) {

            check = true;
        }

        ps.close();

        conn.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return check;
}
    public static void main(String[] args) {
        List<AppointmentModel> userList = new AppointmentController().getAllAppointment();
        System.out.println(userList.size()+"hiển thị danh sách bác sĩ");
    }
}