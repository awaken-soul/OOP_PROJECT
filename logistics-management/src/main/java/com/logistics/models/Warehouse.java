package com.logistics.models;

public class Warehouse {
    private int warehouseId;
    private String name;
    private String address;
    private int capacity;
    private int managerId;

    public Warehouse(int warehouseId, String name, String address, int capacity, int managerId) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.managerId = managerId;
    }

    // Getters and Setters
    public int getWarehouseId() { return warehouseId; }
    public void setWarehouseId(int warehouseId) { this.warehouseId = warehouseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }
}
