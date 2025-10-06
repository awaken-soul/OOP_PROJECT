package com.logistics.model;

/**
 * Represents the process or journey of an order. It connects all other models.
 */
public class Shipment {
    private int id;
    private int customerId;
    private int productId;
    private String productName; // Denormalized for easy display
    private String source;
    private String destination;
    private String status;
    private Integer agentId; // Can be null if not assigned
    private Integer warehouseId; // Can be null if not in a warehouse

    public Shipment(int id, int customerId, int productId, String productName, String source, String destination, String status, Integer agentId, Integer warehouseId) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.productName = productName;
        this.source = source;
        this.destination = destination;
        this.status = status;
        this.agentId = agentId;
        this.warehouseId = warehouseId;
    }

    // Getters for all fields
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getStatus() { return status; }
    public Integer getAgentId() { return agentId; }
    public Integer getWarehouseId() { return warehouseId; }

    /**
     * Provides a comprehensive, user-friendly string representation for UI lists.
     * @return A formatted string detailing the shipment.
     */
    @Override
    public String toString() {
        return String.format("ID: %d | Product: %s | From: %s | To: %s | Status: %s",
                id, productName, source, destination, status);
    }
}

