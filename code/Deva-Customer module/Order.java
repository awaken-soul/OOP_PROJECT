package logistics;

public class Order {
    private int orderId;
    private int userId;
    private int productId;
    private String orderType;
    private String sourceAddress;
    private String destinationAddress;
    private String status;
    private String paymentStatus;

    // Constructor
    public Order(int orderId, int userId, int productId, String orderType,
                 String sourceAddress, String destinationAddress,
                 String status, String paymentStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.orderType = orderType;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public String getOrderType() { return orderType; }
    public String getSourceAddress() { return sourceAddress; }
    public String getDestinationAddress() { return destinationAddress; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
