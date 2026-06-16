package controller.admin;

import config.DBConnection;
import model.admin.PaymentModel;
import model.admin.PaymentSummaryModel;
import model.admin.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentController {

    public static class TreatmentStageOption {
        public int id;
        public String name;

        public TreatmentStageOption(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " (ID: " + id + ")";
        }
    }

    public List<PaymentSummaryModel> getPaymentSummary() {
        List<PaymentSummaryModel> list = new ArrayList<>();
        String sql = """
                SELECT u.id AS user_id, 
                       u.fullname AS patient_name,
                       SUM(p.amount) AS total_amount,
                       SUM(CASE WHEN p.status = N'Đã thanh toán' THEN p.amount ELSE 0 END) AS paid_amount,
                       SUM(CASE WHEN p.status = N'Chưa thanh toán' THEN p.amount ELSE 0 END) AS unpaid_amount
                FROM payments p
                JOIN users u ON p.user_id = u.id
                GROUP BY u.id, u.fullname
                ORDER BY u.fullname ASC
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                PaymentSummaryModel summary = new PaymentSummaryModel();
                summary.setUserId(rs.getInt("user_id"));
                summary.setPatientName(rs.getString("patient_name") != null ? rs.getString("patient_name") : "Chưa xác định");
                summary.setTotalAmount(rs.getDouble("total_amount"));
                summary.setPaidAmount(rs.getDouble("paid_amount"));
                summary.setUnpaidAmount(rs.getDouble("unpaid_amount"));
                list.add(summary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getTotal() {
        String sql = "SELECT SUM(amount) AS total FROM payments WHERE status = N'Đã thanh toán'";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<PaymentModel> getPaymentsByUserId(int userId) {
        List<PaymentModel> list = new ArrayList<>();
        String sql = """
                SELECT p.id, p.user_id, u.fullname AS patient_name, p.amount, p.method, p.status, p.created_at, p.treatment_stage_id, ts.stage_name AS treatment_stage_name
                FROM payments p
                LEFT JOIN users u ON p.user_id = u.id
                LEFT JOIN treatment_stages ts ON p.treatment_stage_id = ts.id
                WHERE p.user_id = ?
                ORDER BY p.id DESC
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentModel payment = new PaymentModel();
                    payment.setId(rs.getInt("id"));
                    payment.setUserId(rs.getInt("user_id"));
                    payment.setPatientName(rs.getString("patient_name") != null ? rs.getString("patient_name") : "Chưa xác định");
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setMethod(rs.getString("method"));
                    payment.setStatus(rs.getString("status"));
                    payment.setCreatedAt(rs.getTimestamp("created_at"));
                    payment.setTreatmentStageId(rs.getInt("treatment_stage_id"));
                    payment.setTreatmentStageName(rs.getString("treatment_stage_name"));
                    list.add(payment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PaymentModel> getAllPayments() {
        List<PaymentModel> list = new ArrayList<>();
        String sql = """
                SELECT p.id, p.user_id, u.fullname AS patient_name, p.amount, p.method, p.status, p.created_at, p.treatment_stage_id, ts.stage_name AS treatment_stage_name
                FROM payments p
                LEFT JOIN users u ON p.user_id = u.id
                LEFT JOIN treatment_stages ts ON p.treatment_stage_id = ts.id
                ORDER BY p.id DESC
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                PaymentModel payment = new PaymentModel();
                payment.setId(rs.getInt("id"));
                payment.setUserId(rs.getInt("user_id"));
                payment.setPatientName(rs.getString("patient_name") != null ? rs.getString("patient_name") : "Chưa xác định");
                payment.setAmount(rs.getDouble("amount"));
                payment.setMethod(rs.getString("method"));
                payment.setStatus(rs.getString("status"));
                payment.setCreatedAt(rs.getTimestamp("created_at"));
                payment.setTreatmentStageId(rs.getInt("treatment_stage_id"));
                payment.setTreatmentStageName(rs.getString("treatment_stage_name"));

                list.add(payment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PaymentModel> getRecentPayments(int limit) {
        List<PaymentModel> list = new ArrayList<>();
        String sql = """
                SELECT TOP (?) p.id, p.user_id, u.fullname AS patient_name, p.amount, p.method, p.status, p.created_at, p.treatment_stage_id, ts.stage_name AS treatment_stage_name
                FROM payments p
                LEFT JOIN users u ON p.user_id = u.id
                LEFT JOIN treatment_stages ts ON p.treatment_stage_id = ts.id
                ORDER BY p.id DESC
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentModel payment = new PaymentModel();
                    payment.setId(rs.getInt("id"));
                    payment.setUserId(rs.getInt("user_id"));
                    payment.setPatientName(rs.getString("patient_name") != null ? rs.getString("patient_name") : "Chưa xác định");
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setMethod(rs.getString("method"));
                    payment.setStatus(rs.getString("status"));
                    payment.setCreatedAt(rs.getTimestamp("created_at"));
                    payment.setTreatmentStageId(rs.getInt("treatment_stage_id"));
                    payment.setTreatmentStageName(rs.getString("treatment_stage_name"));
    
                    list.add(payment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public PaymentModel getPaymentById(int id) {
        String sql = """
                SELECT p.id, p.user_id, u.fullname AS patient_name, p.amount, p.method, p.status, p.created_at, p.treatment_stage_id, ts.stage_name AS treatment_stage_name
                FROM payments p
                LEFT JOIN users u ON p.user_id = u.id
                LEFT JOIN treatment_stages ts ON p.treatment_stage_id = ts.id
                WHERE p.id = ?
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PaymentModel payment = new PaymentModel();
                    payment.setId(rs.getInt("id"));
                    payment.setUserId(rs.getInt("user_id"));
                    payment.setPatientName(rs.getString("patient_name") != null ? rs.getString("patient_name") : "Chưa xác định");
                    payment.setAmount(rs.getDouble("amount"));
                    payment.setMethod(rs.getString("method"));
                    payment.setStatus(rs.getString("status"));
                    payment.setCreatedAt(rs.getTimestamp("created_at"));
                    payment.setTreatmentStageId(rs.getInt("treatment_stage_id"));
                    payment.setTreatmentStageName(rs.getString("treatment_stage_name"));
                    return payment;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertPayment(PaymentModel payment) {
        String sql = """
                INSERT INTO payments
                (
                    user_id,
                    amount,
                    method,
                    status,
                    created_at,
                    treatment_stage_id
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, payment.getUserId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getMethod());
            ps.setString(4, payment.getStatus());
            ps.setTimestamp(5, payment.getCreatedAt());
            if (payment.getTreatmentStageId() > 0) {
                ps.setInt(6, payment.getTreatmentStageId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePayment(PaymentModel payment) {
        String sql = """
                UPDATE payments
                SET user_id = ?,
                    amount = ?,
                    method = ?,
                    status = ?,
                    treatment_stage_id = ?
                WHERE id = ?
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, payment.getUserId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getMethod());
            ps.setString(4, payment.getStatus());
            if (payment.getTreatmentStageId() > 0) {
                ps.setInt(5, payment.getTreatmentStageId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, payment.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePayment(int id) {
        String sql = """
                DELETE FROM payments
                WHERE id = ?
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UserModel> getAllPatients() {
        List<UserModel> list = new ArrayList<>();
        String sql = "SELECT id, fullname, phone FROM users WHERE role_id = 3 ORDER BY fullname ASC";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setFullName(rs.getString("fullname"));
                user.setPhone(rs.getString("phone"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TreatmentStageOption> getAllTreatmentStages() {
        List<TreatmentStageOption> list = new ArrayList<>();
        String sql = "SELECT id, stage_name FROM treatment_stages ORDER BY stage_name ASC";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(new TreatmentStageOption(rs.getInt("id"), rs.getString("stage_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
