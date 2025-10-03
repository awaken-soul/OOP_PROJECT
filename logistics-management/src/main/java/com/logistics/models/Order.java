package com.logistics.models;

import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int userId;
    private int productId;
    private String orderType;
    private String sourceAddress;
    private String destinationAddress;
    private String status;
    private int assignedAgentId;
    private int vehicleId;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(int userId, int productId, String orderType, String sourceAddress, String destinationAddress) {
        this.userId = userId;
        this.productId = productId;
        this.orderType = orderType;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.status = "Pending"; // Default status
        this.paymentStatus = "Pending"; // Default payment status
    }
    
    public Order(int orderId, int userId, int productId, String orderType, String sourceAddress, String destinationAddress, String status, int assignedAgentId, int vehicleId, String paymentStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.orderType = orderType;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.assignedAgentId = assignedAgentId;
        this.vehicleId = vehicleId;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public String getSourceAddress() { return sourceAddress; }
    public void setSourceAddress(String sourceAddress) { this.sourceAddress = sourceAddress; }
    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getAssignedAgentId() { return assignedAgentId; }
    public void setAssignedAgentId(int assignedAgentId) { this.assignedAgentId = assignedAgentId; }
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
