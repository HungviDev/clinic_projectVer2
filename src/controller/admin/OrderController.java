package controller.admin;

import config.DBConnection;
import model.admin.OrderModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public List<OrderModel> getAllOrder() {

        List<OrderModel> orderList =
                new ArrayList<>();

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql = """
                    SELECT
                        o.id AS order_id,
                        u.fullname AS customer_name,
                        s.name AS service_name,
                        oi.quantity,
                        o.total,
                        o.status
                    FROM orders o

                    JOIN users u
                        ON o.user_id = u.id

                    JOIN order_items oi
                        ON o.id = oi.order_id

                    JOIN services s
                        ON oi.product_id = s.id
                    """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                OrderModel order =
                        new OrderModel();

                order.setId(
                        rs.getInt("order_id")
                );

                order.setCustomerName(
                        rs.getString("customer_name")
                );

                order.setServiceName(
                        rs.getString("service_name")
                );

                order.setQuantity(
                        rs.getInt("quantity")
                );

                order.setTotal(
                        rs.getDouble("total")
                );

                order.setStatus(
                        rs.getString("status")
                );

                orderList.add(order);
            }

            rs.close();

            ps.close();

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return orderList;
    }
    
    public List<OrderModel> getRecentOrders(int limit) {
        List<OrderModel> orderList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = """
                    SELECT TOP (?)
                        o.id AS order_id,
                        u.fullname AS customer_name,
                        s.name AS service_name,
                        oi.quantity,
                        o.total,
                        o.status
                    FROM orders o
                    JOIN users u ON o.user_id = u.id
                    JOIN order_items oi ON o.id = oi.order_id
                    JOIN services s ON oi.product_id = s.id
                    ORDER BY o.id DESC
                    """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setId(rs.getInt("order_id"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setServiceName(rs.getString("service_name"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotal(rs.getDouble("total"));
                order.setStatus(rs.getString("status"));
                orderList.add(order);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }
    public int countOrders() {

    int total = 0;

    try {

        Connection conn =
                DBConnection.getConnection();

        String sql =
                "SELECT COUNT(*) AS total FROM orders";

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
    public double getTotalRevenue() {

    double totalRevenue = 0;

    try {

        Connection conn =
                DBConnection.getConnection();

        String sql =
                "SELECT SUM(total) AS revenue FROM orders";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery();

        if (rs.next()) {

            totalRevenue =
                    rs.getDouble("revenue");
        }

        rs.close();

        ps.close();

        conn.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return totalRevenue;
}
  
}