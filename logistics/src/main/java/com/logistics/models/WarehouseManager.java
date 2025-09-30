package com.logistics.models;

/**
 * Represents a Warehouse Manager. 
 * Facilitates monitoring of warehouse stock, assigning products for delivery, and handling shipments.
 */
public class WarehouseManager extends User {

    // --- CONSTRUCTORS ---

    /** Registration Constructor (New User) */
    public WarehouseManager(String name, String email, String passwordHash, String contactNumber, String address) {
        super(name, email, passwordHash, "Manager", contactNumber, address);
    }
    
    /** Full Constructor (DB Retrieval) */
    public WarehouseManager(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        super(userID, name, email, passwordHash, "Manager", contactNumber, address);
    }

    [cite_start]// --- MANAGER-SPECIFIC FUNCTIONALITY [cite: 191-192] ---
    
    /**
     * Retrieves and displays current stock levels and inventory data.
     * Corresponds to 'monitorStock()' in the Class Diagram.
     */
    public void monitorStock() {
        // Calls InventoryService/InventoryDAO to retrieve stock levels.
        System.out.println("Manager " + getName() + " is monitoring warehouse stock levels.");
    }

    /**
     * Assigns specific products from stock to be dispatched for delivery.
     * Corresponds to 'assignProducts()' in the Class Diagram.
     */
    public void assignProducts(int orderID, int quantity) {
        // Calls InventoryService to update stock and link products to the order.
        System.out.println("Manager " + getName() + " assigned " + quantity + " units for Order " + orderID + " dispatch.");
    }
    
    /**
     * Accepts shipments arriving at the warehouse for sorting or storage.
     * Corresponds to 'Accept Storage Requests' use case.
     */
    public void handleInboundShipment(int shipmentID) {
        // Calls OrderService to update the shipment status to 'In Warehouse'.
        System.out.println("Manager " + getName() + " confirmed receipt of inbound shipment ID: " + shipmentID);
    }

    // --- ABSTRACT METHOD IMPLEMENTATIONS ---
    
    @Override
    public boolean login(String enteredEmail, String enteredPassword) {
        UserService service = new UserService();
        User authenticatedUser = service.loginUser(enteredEmail, enteredPassword);
        
        // Check if authentication succeeded AND the retrieved role matches the expected role
        if (authenticatedUser != null && authenticatedUser.getRole().equals(this.getRole())) {
            // If successful, update the current object's state (essential for the session)
            // Note: For simplicity, the GUI will handle the session object directly from the service return.
            System.out.println(this.getRole() + " login via service succeeded.");
            return true;
        }
        return false;
    }
    @Override
    public void logout() {
        System.out.println("Warehouse Manager " + getName() + " logged out successfully.");
    }
}
