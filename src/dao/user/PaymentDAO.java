package dao.user;

import config.DBConnection;
import model.user.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public List<Payment> getPaymentsByUserId(int userId) {

        List<Payment> list = new ArrayList<>();

        // Đã cập nhật lại luồng JOIN: payments -> treatment_stages
        String sql = 
                "SELECT p.amount, p.method, p.status, " +
                "p.created_at, ts.stage_name AS service_name " +
                "FROM payments p " +
                "LEFT JOIN treatment_stages ts ON p.treatment_stage_id = ts.id " +
                "WHERE p.user_id = ? " +
                "ORDER BY p.created_at DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Java mapping vẫn giữ nguyên vì SQL đã dùng alias 'AS service_name'
                Payment payment = new Payment(
                        rs.getDouble("amount"),
                        rs.getString("method"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getString("service_name") 
                );

                list.add(payment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}