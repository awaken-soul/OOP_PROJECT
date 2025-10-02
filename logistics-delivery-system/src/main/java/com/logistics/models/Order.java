package com.logistics.models;

import java.time.LocalDateTime;

/**
 * Represents a single transaction in the logistics system. 
 * This can be a product Purchase, a cargo Shipment, or a Transport service request.
 */
public class Order {
    
    // Attributes from the Orders Table
    private int orderID;
    private int userID; // FK to User table (Customer)
    private Integer productID; // Nullable if shipment/transport only
    private String orderType; // Enum: Purchase, Shipment, Transport
    private String sourceAddress; // Pickup location
    private String destinationAddress; // Delivery location
    private String status; // Enum: Pending, In Warehouse, Out for Delivery, Delivered
    private Integer assignedAgentID; // FK to User table (Delivery Agent)
    private Integer vehicleID; // FK to Vehicle table (optional)
    private String paymentStatus; // Enum: Pending, Paid, COD
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // --- CONSTRUCTORS ---

    /**
     * Creation Constructor: Used when a customer places a new request.
     */
    public Order(int userID, Integer productID, String orderType, String sourceAddress, String destinationAddress) {
        this.userID = userID;
        this.productID = productID;
        this.orderType = orderType;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        
        this.status = "Pending"; 
        this.paymentStatus = "Pending";
        this.assignedAgentID = null;
        this.vehicleID = null;
        this.orderID = -1;
    }

    /**
     * Full Constructor: Used when retrieving an existing order from the database.
     */
    public Order(int orderID, int userID, Integer productID, String orderType, String sourceAddress, String destinationAddress, 
                 String status, Integer assignedAgentID, Integer vehicleID, String paymentStatus, 
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderID = orderID;
        this.userID = userID;
        this.productID = productID;
        this.orderType = orderType;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.assignedAgentID = assignedAgentID;
        this.vehicleID = vehicleID;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // --- GETTERS AND SETTERS ---
    
    public int getOrderID() { return orderID; }
    public int getUserID() { return userID; }
    public Integer getProductID() { return productID; }
    public String getOrderType() { return orderType; }
    public String getSourceAddress() { return sourceAddress; }
    public String getDestinationAddress() { return destinationAddress; }
    public String getStatus() { return status; }
    public Integer getAssignedAgentID() { return assignedAgentID; }
    public Integer getVehicleID() { return vehicleID; }
    public String getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setOrderID(int orderID) { this.orderID = orderID; }
    public void setStatus(String status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setAssignedAgentID(Integer assignedAgentID) { 
        this.assignedAgentID = assignedAgentID;
        this.updatedAt = LocalDateTime.now();
    }
    public void setVehicleID(Integer vehicleID) { 
        this.vehicleID = vehicleID;
        this.updatedAt = LocalDateTime.now();
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
