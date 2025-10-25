package com.logistics.model;

public class Order {
    private int orderId;
    private int userId;
    private int productId;
    private String orderType;
    private String sourceAddress;
    private String destinationAddress;
    private String status;
    private String paymentStatus;

    // Nullable fields
    private Integer assignedAgentId;
    private Integer vehicleId;

    // Joined/display fields (not directly in Orders table)
    private String customerName;
    private String productName;
    private String warehouseName;

    // ✅ Full constructor (DB-level)
    public Order(int orderId, int userId, int productId, String orderType,
                 String sourceAddress, String destinationAddress,
                 String status, String paymentStatus,
                 Integer assignedAgentId, Integer vehicleId) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.orderType = orderType;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.assignedAgentId = assignedAgentId;
        this.vehicleId = vehicleId;
    }

    // ✅ Empty constructor for DAO and TableModel
    public Order() {}

    // --- Getters ---
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public String getOrderType() { return orderType; }
    public String getSourceAddress() { return sourceAddress; }
    public String getDestinationAddress() { return destinationAddress; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public Integer getAssignedAgentId() { return assignedAgentId; }
    public Integer getVehicleId() { return vehicleId; }
    public String getCustomerName() { return customerName; }
    public String getProductName() { return productName; }
    public String getWarehouseName() { return warehouseName; }

    // --- Setters ---
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public void setSourceAddress(String sourceAddress) { this.sourceAddress = sourceAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    public void setStatus(String status) { this.status = status; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setAssignedAgentId(Integer assignedAgentId) { this.assignedAgentId = assignedAgentId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    // ✅ Extended constructor for joined queries (used in DAO JOINs)
    public Order(int orderId, int userId, int productId, String orderType,
                 String sourceAddress, String destinationAddress,
                 String status, String paymentStatus,
                 String customerName, String productName, String warehouseName) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.orderType = orderType;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.customerName = customerName;
        this.productName = productName;
        this.warehouseName = warehouseName;
    }

    // ✅ for debugging and JComboBox display
    @Override
    public String toString() {
        String prod = (productName != null && !productName.isBlank()) ? productName : "Unknown Product";
        String ware = (warehouseName != null && !warehouseName.isBlank()) ? " @ " + warehouseName : "";
        return "Order #" + orderId + " [" + prod + "] - " + status + ware;
    }
}
