/*package com.logistics.model;


public class Warehouse {

    private int warehouseId;
    private String name;
    private String address;
    private int capacity;
    private int managerId;

    // Default constructor
    public Warehouse() {}

    // Parameterized constructor
    public Warehouse(int warehouseId, String name, String address, int capacity, int managerId) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.managerId = managerId;
    }

    // Getters and setters
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }


    @Override
    public String toString() {
        // You can also return name + " - " + address if you want more detail
        return name;
    }
}


*/
package com.logistics.model;

public class Warehouse {
    private int warehouseId;
    private String name;
    private String address;
    private int capacity;
    private int managerId;

    // Getters and Setters
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return name + " (Cap: " + capacity + ")";
    }
}
