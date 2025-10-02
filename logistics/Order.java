public class Order {
    private int orderId;
    private Product product;
    private Agent agent;
    private int quantity;
    private String status;

    public Order(int orderId, Product product, Agent agent, int quantity) {
        this.orderId = orderId;
        this.product = product;
        this.agent = agent;
        this.quantity = quantity;
        this.status = "Pending";  // default
    }

    // Getters
    public int getOrderId() { return orderId; }
    public Product getProduct() { return product; }
    public Agent getAgent() { return agent; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    // Actions
    public void dispatch() {
        this.status = "Dispatched";
    }

    public void deliver() {
        this.status = "Delivered";
    }
}
