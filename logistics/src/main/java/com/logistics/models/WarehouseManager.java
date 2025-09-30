package com.logistics.models;

import com.logistics.services.UserService;

public class WarehouseManager extends User {

    public WarehouseManager(String name, String email, String passwordHash, String contactNumber, String address) {
        super(name, email, passwordHash, "Manager", contactNumber, address);
    }
    
    public WarehouseManager(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        // Line 21 error fix: Ensure the constructor block is properly formed
        super(userID, name, email, passwordHash, "Manager", contactNumber, address);
    }

    public void monitorStock() {
        System.out.println("Manager " + getName() + " is monitoring warehouse stock levels.");
    }
    // ... other manager methods ...

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
