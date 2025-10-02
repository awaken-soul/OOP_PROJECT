public class Product {
    private int id;
    private String name;
    private int quantity;

    public Product(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void displayInfo() {
        System.out.println("Product ID: " + id + ", Name: " + name + ", Qty: " + quantity);
    }

    // 👇 Add this
    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}

