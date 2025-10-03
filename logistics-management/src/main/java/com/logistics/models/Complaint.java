package com.logistics.models;

import java.time.LocalDateTime;

public class Complaint {
    private int complaintId;
    private int userId;
    private Integer orderId; // Use Integer to allow for null values
    private String subject;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    // Constructor for creating NEW complaints
    public Complaint(int userId, Integer orderId, String subject, String description) {
        this.userId = userId;
        this.orderId = orderId;
        this.subject = subject;
        this.description = description;
        this.status = "Open"; // Default status for new complaints
    }

    // Full constructor for retrieving from DB
    public Complaint(int complaintId, int userId, Integer orderId, String subject, String description, String status, LocalDateTime createdAt) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.orderId = orderId;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getComplaintId() { return complaintId; }
    public void setComplaintId(int complaintId) { this.complaintId = complaintId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
