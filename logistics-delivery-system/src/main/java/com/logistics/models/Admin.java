package com.logistics.models;

import com.logistics.services.UserService;

/**
 * Represents the System Administrator. 
 */
public class Admin extends User {
    
    public Admin(String name, String email, String passwordHash, String contactNumber, String address) {
        super(name, email, passwordHash, "Admin", contactNumber, address);
    }
    
    /** Full Constructor (DB Retrieval) */
    // Ensure this line and the block below it are correct (Line 23 area)
    public Admin(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        super(userID, name, email, passwordHash, "Admin", contactNumber, address);
    }

    // ... (rest of the Admin methods are fine)

    public void manageWarehouse() {
        System.out.println("Admin " + getName() + " is managing warehouse data (e.g., adding capacity).");
    }

    public void manageVehicle() {
        System.out.println("Admin " + getName() + " is managing vehicle fleet and status.");
    }
    
    public void resolveComplaints() {
        System.out.println("Admin " + getName() + " is reviewing and resolving user complaints.");
    }
    
    public void verifyVendorData(int vendorID, String vendorType) {
        System.out.println("Admin is validating " + vendorType + " (ID: " + vendorID + ") documents.");
    }

    @Override
    public boolean login(String enteredEmail, String enteredPassword) { 
        UserService service = new UserService();
        User authenticatedUser = service.loginUser(enteredEmail, enteredPassword);
    
        if (authenticatedUser != null && authenticatedUser.getRole().equals(this.getRole())) {
            System.out.println(this.getRole() + " login via service succeeded.");
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        System.out.println("Admin " + getName() + " logged out successfully.");
    }
}
