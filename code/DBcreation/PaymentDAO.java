package logistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // Fetch all pending payments for a user
    public List<Payment> getPendingPayments(int userId) {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT pay.payment_id, pay.order_id, pay.amount, pay.method, pay.status " +
                     "FROM Payment pay " +
                     "JOIN Orders o ON pay.order_id = o.order_id " +
                     "WHERE o.user_id = ? AND pay.status = 'Pending'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("order_id"),
                        rs.getDouble("amount"),
                        rs.getString("method"),
                        rs.getString("status")
                );
                payments.add(payment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // Update payment status and method
    public boolean completePayment(int paymentId, String method) {
        String sql = "UPDATE Payment SET status = ?, method = ? WHERE payment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "Completed");
            ps.setString(2, method);
            ps.setInt(3, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
