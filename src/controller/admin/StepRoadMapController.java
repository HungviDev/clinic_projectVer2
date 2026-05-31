package controller.admin;

import model.admin.StepRoadMapModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class StepRoadMapController {

    // Lấy danh sách giai đoạn theo id lộ trình
    public List<StepRoadMapModel>
    getStepsByRouteId(int routeId) {
        List<StepRoadMapModel> stepList =
                new ArrayList<>();

        try {

            Connection conn =
                    config.DBConnection.getConnection();

            String sql =
                    "SELECT * FROM treatment_stages " +
                    "WHERE treatment_route_id = ? " +
                    "ORDER BY sequence_order ASC";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, routeId);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                StepRoadMapModel step =
                        new StepRoadMapModel();

                step.setId(
                        rs.getInt("id")
                );

                step.setTreatmentRouteId(
                        rs.getInt("treatment_route_id")
                );

                step.setStageName(
                        rs.getString("stage_name")
                );

                step.setSequenceOrder(
                        rs.getInt("sequence_order")
                );

                step.setStatus(
                        rs.getString("status")
                );

                step.setAppointmentDate(
                        rs.getDate("appointment_date")
                );

                step.setNote(
                        rs.getString("note")
                );

                step.setCreatedAt(
                        rs.getDate("created_at")
                );

                step.setUpdatedAt(
                        rs.getDate("updated_at")
                );

                step.setCost(
                        rs.getDouble("cost")
                );

                step.setDelay(
                        rs.getInt("delay")
                );

                stepList.add(step);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return stepList;
    }
    public boolean insertStepByRouteId(StepRoadMapModel step) {

    try {

        Connection conn =
                config.DBConnection.getConnection();

        String sql =
        "INSERT INTO treatment_stages (" +
                "treatment_route_id, " +
                "stage_name, " +
                "sequence_order, " +
                "status, " +
                "note, " +
                "created_at, " +
                "updated_at, " +
                "cost, " +
                "delay" +
                ") VALUES (?, ?, ?, ?, ?, GETDATE(), GETDATE(), ?, ?)";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setInt(
                1,
                step.getTreatmentRouteId()
        );

        ps.setString(
                2,
                step.getStageName()
        );

        ps.setInt(
                3,
                step.getSequenceOrder()
        );

        ps.setString(
                4,
                step.getStatus()
        );

        ps.setString(5, step.getNote());

        ps.setDouble(6, step.getCost());

        ps.setInt(7, step.getDelay());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {

        e.printStackTrace();
    }

    return false;
}
}