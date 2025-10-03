package com.logistics.services;

import com.logistics.database.PaymentDAO;
import com.logistics.models.Payment;

import java.time.LocalDateTime;

public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    /**
     * Simulates processing a payment for an order.
     * @param orderId The ID of the order being paid for.
     * @param amount The amount of the payment.
     * @param method The payment method (e.g., "Card", "Cash").
     * @return true if the payment was successfully recorded, false otherwise.
     */
    public boolean processPayment(int orderId, double amount, String method) {
        // In a real application, this would involve calling a payment gateway.
        // Here, we just record the payment as "Completed".
        Payment newPayment = new Payment(0, orderId, amount, method, "Completed", LocalDateTime.now());
        return paymentDAO.save(newPayment);
    }
}
