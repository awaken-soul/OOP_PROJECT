package com.logistics.models;

/**
 * Represents a Delivery Agent (driver/transporter). 
 * Handles deliveries, pickups, and status updates.
 */
public class DeliveryAgent extends User {
    
    // --- CONSTRUCTORS ---

    /** Registration Constructor (New User) */
    public DeliveryAgent(String name, String email, String passwordHash, String contactNumber, String address) {
        super(name, email, passwordHash, "Agent", contactNumber, address);
    }
    
    /** Full Constructor (DB Retrieval) */
    public DeliveryAgent(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        super(userID, name, email, passwordHash, "Agent", contactNumber, address);
    }

    [cite_start]// --- AGENT-SPECIFIC FUNCTIONALITY [cite: 187-188] ---
    
    /**
     * Accepts a delivery or pickup request assigned by the system.
     * Corresponds to the 'Accept Request' use case.
     */
    public boolean acceptDelivery(int orderID) {
        // Calls OrderService to assign this agent to the order and update status.
        System.out.println("Agent " + getName() + " accepted delivery request for Order ID: " + orderID);
        return true; 
    }

    /**
     * Updates the status and location of an ongoing delivery.
     * Corresponds to 'Update Agent Details' use case.
     */
    public void updateStatus(int orderID, String newStatus) {
        // Calls TrackingService/OrderService to update the order's status.
        System.out.println("Agent " + getName() + " updated Order " + orderID + " status to: " + newStatus);
    }
    
    /**
     * Records the details of a completed or canceled delivery.
     */
    public void maintainDeliveryRecords() {
        // Calls OrderService to fetch and review personal history.
        System.out.println("Agent " + getName() + " is reviewing delivery history.");
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
        System.out.println("Delivery Agent " + getName() + " logged out successfully.");
    }
}
