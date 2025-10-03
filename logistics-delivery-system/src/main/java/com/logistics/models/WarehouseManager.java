package com.logistics.models;

import com.logistics.services.UserService;

public class WarehouseManager extends User {

    public WarehouseManager(String name, String email, String passwordHash, String contactNumber, String address) {
        super(name, email, passwordHash, "Manager", contactNumber, address);
    }
    
    public WarehouseManager(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        super(userID, name, email, passwordHash, "Manager", contactNumber, address);
    }

    public void monitorStock() {
      System.out.println("Manager " + getName() + " is monitoring warehouse stock levels.");
    }

    public void assignProducts(int orderID, int quantity) {
       System.out.println("Manager " + getName() + " assigned " + quantity + " units for Order " + orderID + " dispatch.");
    }
    
    public void handleInboundShipment(int shipmentID) {
        System.out.println("Manager " + getName() + " confirmed receipt of inbound shipment ID: " + shipmentID);
    }

    @Override
    public boolean login(String enteredEmail, String enteredPassword) { 
        UserService service = new UserService();
        User authenticatedUser = service.loginUser(enteredEmail, enteredPassword);
        
        if (authenticatedUser != null && authenticatedUser.getRole().equals(this.getRole())) {
            System.out.println(this.getRole() + " login successful.");
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        System.out.println("Warehouse Manager " + getName() + " logged out successfully.");
    }
}
