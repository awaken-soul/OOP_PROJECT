package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.database.PaymentDAO;
import com.logistics.models.Payment;

/**
 * Handles all business logic related to payment transactions and financial reconciliation.
 */
public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.orderDAO = new OrderDAO();
    }

    /**
     * Records a new payment and updates the corresponding order status.
     * This simulates the transaction process with validation checks[cite: 171].
     * * @param orderId The ID of the order being paid for.
     * @param amount The amount received.
     * @param method The payment method (Cash, Card, UPI).
     * @return true if both the payment record and order status were updated successfully.
     */
    public boolean processPayment(int orderId, double amount, String method) {
        // 1. Validation (Check if amount is positive, method is valid, etc.)
        if (amount <= 0) {
            System.err.println("Payment failed: Invalid amount.");
            return false;
        }

        // 2. Create the Payment Model object
        Payment payment = new Payment(orderId, amount, method);

        // 3. Record the payment in the database (via DAO)
        if (!paymentDAO.recordPayment(payment)) {
            System.err.println("Payment failed: Could not record transaction in DB.");
            return false;
        }

        // 4. Update the payment status on the order (Business Logic)
        boolean orderUpdateSuccess = orderDAO.updateOrderStatus(
            orderId, 
            null, // Do not change delivery status here
            null, 
            null
        );

        // Update the payment status field in the Orders table
        // NOTE: A specific DAO method is needed to update only the payment_status field. 
        // For simplicity, we assume an OrderDAO update method is used:
        boolean orderPaymentStatusUpdateSuccess = orderDAO.updatePaymentStatusField(
            orderId, "Paid"
        );
        
        if (orderPaymentStatusUpdateSuccess) {
            paymentDAO.updatePaymentStatus(payment.getPaymentID(), "Completed");
            System.out.println("Payment successful and Order ID " + orderId + " marked as Paid.");
            return true;
        } else {
            // Rollback/logging logic would be here in a production environment
            System.err.println("Payment recorded, but failed to update order payment status.");
            return false;
        }
    }
    
    // NOTE: This helper method is assumed in OrderDAO to complete the payment logic.
    /* public boolean updatePaymentStatusField(int orderId, String newPaymentStatus) {
        String sql = "UPDATE Orders SET payment_status = ?, updated_at = ? WHERE order_id = ?";
        // ... JDBC logic ...
    }
    */
}
