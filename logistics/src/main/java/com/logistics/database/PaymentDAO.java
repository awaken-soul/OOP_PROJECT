package com.logistics.database;

import com.logistics.models.Payment;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Handles all database operations (CRUD) for the Payment table.
 */
public class PaymentDAO {

    /**
     * Maps a ResultSet row to a Payment object.
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("payment_id"),
            rs.getInt("order_id"),
            rs.getDouble("amount"),
            rs.getString("method"),
            rs.getString("status"),
            LocalDateTime.parse(rs.getString("payment_date"))
        );
    }

    /**
     * Saves a new Payment record to the database.
     * @param payment The Payment object to save (paymentID will be updated).
     * @return true if insertion was successful, false otherwise.
     */
    public boolean recordPayment(Payment payment) {
        String sql = "INSERT INTO Payment (order_id, amount, method, status, payment_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            LocalDateTime now = LocalDateTime.now();
            
            pstmt.setInt(1, payment.getOrderID());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getMethod());
            pstmt.setString(4, payment.getStatus());
            pstmt.setString(5, now.toString()); // Save LocalDateTime as TEXT

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payment.setPaymentID(generatedKeys.getInt(1));
                        System.out.println("Payment recorded successfully for Order ID: " + payment.getOrderID());
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Database error during payment recording: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates the status of an existing payment record.
     */
    public boolean updatePaymentStatus(int paymentId, String newStatus) {
        String sql = "UPDATE Payment SET status = ? WHERE payment_id = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, paymentId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error updating payment status: " + e.getMessage());
            return false;
        }
    }
}
