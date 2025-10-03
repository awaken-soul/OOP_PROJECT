package com.logistics.models;

import java.time.LocalDateTime;

public class Payment {
    
   
    private int paymentID;
    private int orderID; 
    private double amount;
    private String method; 
    private String status; 
    private LocalDateTime paymentDate;
    
    public Payment(int orderID, double amount, String method) {
        this.orderID = orderID;
        this.amount = amount;
        this.method = method;
        this.status = "Pending";
        this.paymentID = -1; 
    }


    public Payment(int paymentID, int orderID, double amount, String method, String status, LocalDateTime paymentDate) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paymentDate = paymentDate;
    }
    
   
    
  
    public int getPaymentID() { return paymentID; }
    public int getOrderID() { return orderID; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public LocalDateTime getPaymentDate() { return paymentDate; }

   
    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }
    public void setStatus(String status) { this.status = status; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return String.format("Payment [ID=%d, OrderID=%d, Amount=%.2f, Method=%s, Status=%s]", 
                             paymentID, orderID, amount, method, status);
    }
}
