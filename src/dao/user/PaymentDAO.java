package dao.user;

import config.DBConnection;
import model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public List<Payment> getPaymentsByUserId(int userId) {

        List<Payment> list = new ArrayList<>();

        String sql =
                "SELECT p.amount, p.method, p.status, " +
                "p.created_at, s.name AS service_name " +
                "FROM payments p " +
                "LEFT JOIN appointments a ON p.appointment_id = a.id " +
                "LEFT JOIN services s ON a.service_id = s.id " +
                "WHERE p.user_id = ? " +
                "ORDER BY p.created_at DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

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