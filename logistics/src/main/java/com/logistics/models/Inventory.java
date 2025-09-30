package com.logistics.models;

/**
 * Represents the stock of a specific product within a warehouse or managed by a retailer.
 * Corresponds to the Inventory and Product Tables' stock-keeping function.
 */
public class Inventory {
    
    // Attributes from the Inventory Table (aligned with Product Table)
    private int productID; // PK
    private String name; // Product name
    private String description; // Optional product description
    private double price; // Product price
    private int stockLevel; // Current stock count (from Inventory table)
    private int warehouseID; // FK to Warehouse table
    private Integer retailerID; // FK to Retailer table (can be null if only stored in warehouse)

    // --- CONSTRUCTORS ---

    /**
     * Creation Constructor: Used when a new product/stock is added by a Retailer or Warehouse Manager.
     */
    public Inventory(String name, String description, double price, int stockLevel, int warehouseID, Integer retailerID) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockLevel = stockLevel;
        this.warehouseID = warehouseID;
        this.retailerID = retailerID;
        this.productID = -1; // Temporary placeholder
    }

    /**
     * Full Constructor: Used when retrieving an existing inventory item/product from the database.
     */
    public Inventory(int productID, String name, String description, double price, int stockLevel, int warehouseID, Integer retailerID) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockLevel = stockLevel;
        this.warehouseID = warehouseID;
        this.retailerID = retailerID;
    }

    // --- GETTERS AND SETTERS ---
    
    // Getters
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStockLevel() { return stockLevel; }
    public int getWarehouseID() { return warehouseID; }
    public Integer getRetailerID() { return retailerID; }

    // Setters (Used by InventoryService/WarehouseManager to manage stock)
    public void setProductID(int productID) { this.productID = productID; } // Set after DB insertion
    public void setStockLevel(int stockLevel) { this.stockLevel = stockLevel; }
    public void setPrice(double price) { this.price = price; }
    public void setWarehouseID(int warehouseID) { this.warehouseID = warehouseID; }

    // --- UTILITY ---
    
    @Override
    public String toString() {
        return String.format("Inventory [ID=%d, Product=%s, Stock=%d, Warehouse=%d]", 
                             productID, name, stockLevel, warehouseID);
    }
}
