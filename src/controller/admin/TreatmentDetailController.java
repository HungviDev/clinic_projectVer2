package controller.admin;

import model.admin.TreatmentDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import config.DBConnection;

public class TreatmentDetailController {

    public TreatmentDetail getDetail(int usserId) {

        TreatmentDetail detail = null;

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT " +
                    "u.id, " +
                    "u.fullname AS patient_name, " +
                    "mr.diagnosis, " +
                    "tr.title AS route_name, " +
                    "du.fullname AS doctor_name " +
                    "FROM medical_records mr " +
                    "JOIN users u " +
                    "ON mr.user_id = u.id " +
                    "JOIN treatment_routes tr " +
                    "ON mr.treatment_route_id = tr.id " +
                    "JOIN doctors d " +
                    "ON mr.doctor_id = d.id " +
                    "JOIN users du " +
                    "ON d.user_id = du.id " +
                    "WHERE u.id = ?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, usserId);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                detail =
                        new TreatmentDetail();

                detail.setPatientId(
                        rs.getString("id")
                );

                detail.setPatientName(
                        rs.getString("patient_name")
                );

                detail.setDiagnosis(
                        rs.getString("diagnosis")
                );

                detail.setRouteName(
                        rs.getString("route_name")
                );

                detail.setDoctorName(
                        rs.getString("doctor_name")
                );
            }

            rs.close();

            ps.close();

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return detail;
    }
    public static void main(String[] args) {
        TreatmentDetailController detailController = new TreatmentDetailController();
        TreatmentDetail detail = detailController.getDetail(1);
        System.out.println(detail.getDoctorName());
    }
}