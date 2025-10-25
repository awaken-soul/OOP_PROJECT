package com.logistics.model;

public class Payment {
    private int paymentId;
    private int orderId;
    private double amount;
    private String method;
    private String status;

    // Constructor
    public Payment(int paymentId, int orderId, double amount, String method, String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    // Getters
    public int getPaymentId() { return paymentId; }
    public int getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }

    // Setters
    public void setMethod(String method) { this.method = method; }
    public void setStatus(String status) { this.status = status; }
}
