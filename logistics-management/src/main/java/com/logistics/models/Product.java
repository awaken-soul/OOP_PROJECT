package com.logistics.models;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int warehouseId;
    private int retailerId;

    public Product(int productId, String name, String description, double price, int quantity, int warehouseId, int retailerId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.warehouseId = warehouseId;
        this.retailerId = retailerId;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }
    public int getRetailerId() { return retailerId; }
    public void setRetailerId(int retailerId) { this.retailerId = retailerId; }
}
