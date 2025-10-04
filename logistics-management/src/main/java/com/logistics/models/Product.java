package com.logistics.models;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Integer warehouseId; // Use Integer to allow null
    private Integer retailerId;  // Use Integer to allow null

    public Product(int productId, String name, String description, double price, int quantity, Integer warehouseId, Integer retailerId) {
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

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public Integer getRetailerId() { return retailerId; }
    public void setRetailerId(Integer retailerId) { this.retailerId = retailerId; }
}
