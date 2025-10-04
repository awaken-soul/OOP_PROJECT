package com.logistics.database;

import com.logistics.models.Payment;

import java.sql.*;
import java.util.Optional;

public class PaymentDAO {

    private final Connection connection;

    public PaymentDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    public boolean save(Payment payment) {
        String sql = "INSERT INTO payment(order_id, amount, method, status, payment_date) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getMethod());
            pstmt.setString(4, payment.getStatus());
            pstmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving payment.", e);
        }
    }

    public Optional<Payment> findByOrderId(int orderId) {
        String sql = "SELECT * FROM payment WHERE order_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToPayment(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding payment by order id.", e);
        }
        return Optional.empty();
    }

    private Payment mapRowToPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("payment_id"),
                rs.getInt("order_id"),
                rs.getDouble("amount"),
                rs.getString("method"),
                rs.getString("status"),
                rs.getTimestamp("payment_date").toLocalDateTime()
        );
    }
}
