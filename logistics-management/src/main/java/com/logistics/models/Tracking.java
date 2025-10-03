package com.logistics.models;

import java.time.LocalDateTime;

public class Tracking {
    private int trackingId;
    private int orderId;
    private int agentId;
    private String currentStatus;
    private String location;
    private LocalDateTime updatedAt;

    public Tracking(int trackingId, int orderId, int agentId, String currentStatus, String location, LocalDateTime updatedAt) {
        this.trackingId = trackingId;
        this.orderId = orderId;
        this.agentId = agentId;
        this.currentStatus = currentStatus;
        this.location = location;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getTrackingId() { return trackingId; }
    public void setTrackingId(int trackingId) { this.trackingId = trackingId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getAgentId() { return agentId; }
    public void setAgentId(int agentId) { this.agentId = agentId; }
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
