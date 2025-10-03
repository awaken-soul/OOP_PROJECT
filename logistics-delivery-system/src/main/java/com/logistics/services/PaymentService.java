package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.database.PaymentDAO;
import com.logistics.models.Payment;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.orderDAO = new OrderDAO();
    }
    
    public boolean processPayment(int orderId, double amount, String method) {
       if (amount <= 0) {
            System.err.println("Payment failed: Invalid amount.");
            return false;
        }
        Payment payment = new Payment(orderId, amount, method);
        if (!paymentDAO.recordPayment(payment)) {
            System.err.println("Payment failed: Could not record transaction in DB.");
            return false;
        }
        boolean orderUpdateSuccess = orderDAO.updateOrderStatus(
            orderId, 
            null, 
            null, 
            null
        );
        
        boolean orderPaymentStatusUpdateSuccess = orderDAO.updatePaymentStatusField(
            orderId, "Paid"
        );
        
        if (orderPaymentStatusUpdateSuccess) {
            paymentDAO.updatePaymentStatus(payment.getPaymentID(), "Completed");
            System.out.println("Payment successful and Order ID " + orderId + " marked as Paid.");
            return true;
        } else {
            System.err.println("Payment recorded, but failed to update order payment status.");
            return false;
        }
    }
}
