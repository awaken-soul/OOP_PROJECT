package com.logistics.models;

import com.logistics.services.UserService;

public class DeliveryAgent extends User {
    
    public DeliveryAgent(String name, String email, String passwordHash, String contactNumber, String address) {
        super(name, email, passwordHash, "Agent", contactNumber, address);
    }
    
    public DeliveryAgent(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        // Line 21 error fix: Ensure the constructor block is properly formed
        super(userID, name, email, passwordHash, "Agent", contactNumber, address);
    }

    public boolean acceptDelivery(int orderID) {
        System.out.println("Agent " + getName() + " accepted delivery request for Order ID: " + orderID);
        return true; 
    }
    // ... other agent methods ...

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
        System.out.println("Delivery Agent " + getName() + " logged out successfully.");
    }
}
