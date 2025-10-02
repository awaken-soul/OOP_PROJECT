public class App {
    public static void main(String[] args) {
        // Create product
        Product p1 = new Product(1, "Laptop", 50);
        p1.displayInfo();

        // Create agent
        Agent a1 = new Agent(101, "Ravi");
        a1.displayInfo();

        // Create order (requires product + agent + quantity)
        Order o1 = new Order(1001, p1, a1, 2);
        System.out.println("Order created: ID=" + o1.getOrderId() +
                           ", Product=" + o1.getProduct().getName() +
                           ", Agent=" + o1.getAgent().getName() +
                           ", Qty=" + o1.getQuantity() +
                           ", Status=" + o1.getStatus());

        // Dispatch order
        o1.dispatch();
        System.out.println("After Dispatch: Order " + o1.getOrderId() + " Status=" + o1.getStatus());

        // Deliver order
        o1.deliver();
        System.out.println("After Delivery: Order " + o1.getOrderId() + " Status=" + o1.getStatus());
    }
}
