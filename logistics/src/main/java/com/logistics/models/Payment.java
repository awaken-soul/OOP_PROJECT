package com.logistics.models;

import java.time.LocalDateTime;

/**
 * Represents a payment transaction record in the system. 
 * Linked to a specific Order.
 */
public class Payment {
    
    // Attributes from the Payment Table 
    private int paymentID;
    private int orderID; // FK to Orders table
    private double amount;
    private String method; // Enum: Cash, Card, UPI
    private String status; // Enum: Pending, Completed
    private LocalDateTime paymentDate;
    
    // --- CONSTRUCTORS ---

    /**
     * Creation Constructor: Used when a customer or agent initiates a payment record.
     * The DB generates paymentID and paymentDate.
     */
    public Payment(int orderID, double amount, String method) {
        this.orderID = orderID;
        this.amount = amount;
        this.method = method;
        this.status = "Pending"; // Default status
        this.paymentID = -1; // Temporary placeholder
    }

    /**
     * Full Constructor: Used when retrieving an existing payment record from the database.
     */
    public Payment(int paymentID, int orderID, double amount, String method, String status, LocalDateTime paymentDate) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paymentDate = paymentDate;
    }
    
    // --- GETTERS AND SETTERS ---
    
    // Getters
    public int getPaymentID() { return paymentID; }
    public int getOrderID() { return orderID; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public LocalDateTime getPaymentDate() { return paymentDate; }

    // Setters (Used by PaymentService to update the record)
    public void setPaymentID(int paymentID) { this.paymentID = paymentID; } // Set after DB insertion
    public void setStatus(String status) { this.status = status; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    // --- UTILITY ---
    
    @Override
    public String toString() {
        return String.format("Payment [ID=%d, OrderID=%d, Amount=%.2f, Method=%s, Status=%s]", 
                             paymentID, orderID, amount, method, status);
    }
}
