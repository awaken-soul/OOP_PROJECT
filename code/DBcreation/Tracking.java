package logistics;

import java.sql.Timestamp;

public class Tracking {
    private int orderId;
    private String productName;
    private String orderStatus;
    private String currentStatus;
    private String location;
    private Timestamp updatedAt;

    // Constructor
    public Tracking(int orderId, String productName, String orderStatus,
                    String currentStatus, String location, Timestamp updatedAt) {
        this.orderId = orderId;
        this.productName = productName;
        this.orderStatus = orderStatus;
        this.currentStatus = currentStatus;
        this.location = location;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public String getProductName() { return productName; }
    public String getOrderStatus() { return orderStatus; }
    public String getCurrentStatus() { return currentStatus; }
    public String getLocation() { return location; }
    public Timestamp getUpdatedAt() { return updatedAt; }
}
