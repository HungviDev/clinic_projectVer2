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
    // DASHBOARD DATA
    // =====================================================
    public DoctorDashboardModel getDashboardData(int doctorId) {

        DoctorDashboardModel model =
                new DoctorDashboardModel();

        try {

            Connection conn =
                    DBConnection.getConnection();

            // =================================================
            // TOTAL APPOINTMENTS TODAY
            // =================================================
            String sql1 =
                    "SELECT COUNT(*) " +
                    "FROM appointments " +
                    "WHERE doctor_id = ? " +
                    "AND CAST(appointment_date AS DATE) = CAST(GETDATE() AS DATE)";

            PreparedStatement ps1 =
                    conn.prepareStatement(sql1);

            ps1.setInt(1, doctorId);

            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {

                model.setTotalAppointmentsToday(
                        rs1.getInt(1)
                );
            }

            // =================================================
            // PATIENTS TREATING
            // =================================================
            String sql2 =
                    "SELECT COUNT(*) " +
                    "FROM appointments " +
                    "WHERE doctor_id = ? " +
                    "AND status = 'approved'";

            PreparedStatement ps2 =
                    conn.prepareStatement(sql2);

            ps2.setInt(1, doctorId);

            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {

                model.setTotalPatientsTreating(
                        rs2.getInt(1)
                );
            }

            // =================================================
            // COMPLETED THIS MONTH
            // =================================================
            String sql3 =
                    "SELECT COUNT(*) " +
                    "FROM appointments " +
                    "WHERE doctor_id = ? " +
                    "AND status = 'done' " +
                    "AND MONTH(appointment_date)=MONTH(GETDATE()) " +
                    "AND YEAR(appointment_date)=YEAR(GETDATE())";

            PreparedStatement ps3 =
                    conn.prepareStatement(sql3);

            ps3.setInt(1, doctorId);

            ResultSet rs3 = ps3.executeQuery();

            if (rs3.next()) {

                model.setCompletedCasesMonth(
                        rs3.getInt(1)
                );
            }

            // =================================================
            // REVENUE TODAY
            // =================================================
            String sql4 =
                    "SELECT ISNULL(SUM(amount),0) " +
                    "FROM payments p " +
                    "JOIN appointments a " +
                    "ON p.appointment_id = a.id " +
                    "WHERE a.doctor_id = ? ";
            PreparedStatement ps4 =
                    conn.prepareStatement(sql4);

            ps4.setInt(1, doctorId);

            ResultSet rs4 = ps4.executeQuery();

            if (rs4.next()) {

                model.setRevenueToday(
                        rs4.getDouble(1)
                );
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    // =====================================================
    // GET APPOINTMENTS
    // =====================================================
    public List<AppointmentModel> getTodayAppointments(
            int doctorId
    ) {

        List<AppointmentModel> list =
                new ArrayList<>();

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT " +
                    "a.id, " +
                    "FORMAT(a.appointment_date, 'HH:mm') AS time, " +
                    "u.fullname AS patient_name, " +
                    "s.name AS service_name, " +
                    "a.status " +
                    "FROM appointments a " +
                    "JOIN users u ON a.user_id = u.id " +
                    "JOIN services s ON a.service_id = s.id " +
                    "WHERE a.doctor_id = ? " +
                    "ORDER BY a.appointment_date ASC";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, doctorId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                AppointmentModel ap =
                        new AppointmentModel(
                                rs.getInt("id"),
                                rs.getString("time"),
                                rs.getString("patient_name"),
                                rs.getString("service_name"),
                                rs.getString("status")
                        );

                list.add(ap);
            }

            conn.close();
            System.out.println("Fetched " + list.size() + " appointments for doctor ID: " + doctorId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================
    public boolean updateAppointmentStatus(
            int appointmentId,
            String newStatus
    ) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE appointments " +
                    "SET status = ? " +
                    "WHERE id = ? " +
                    "AND status = 'pending'";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, newStatus);
            ps.setInt(2, appointmentId);

            int rows = ps.executeUpdate();

            conn.close();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}