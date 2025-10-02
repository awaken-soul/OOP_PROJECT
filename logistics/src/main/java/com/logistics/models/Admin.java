package com.logistics.models;

import com.logistics.services.UserService; // <-- REQUIRED IMPORT HERE

/**
 * Represents the System Administrator. 
 * Manages the core system components, including vendors, vehicles, and user concerns.
 */
public class Admin extends User {
    
    // --- CONSTRUCTORS ---

    /** Registration Constructor (New User) */
    public Admin(String name, String email, String passwordHash, String contactNumber, String address) {
        super(name, email, passwordHash, "Admin", contactNumber, address);
    }
    
    /** Full Constructor (DB Retrieval) */
    public Admin(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        super(userID, name, email, passwordHash, "Admin", contactNumber, address);
    }

    [cite_start]// --- ADMIN-SPECIFIC FUNCTIONALITY [cite: 176-178] ---
    
    /**
     * Oversees the vendor/warehouse management process (add, edit, remove warehouses).
     * Corresponds to 'manageWarehouse()' in the Class Diagram.
     */
    public void manageWarehouse() {
        // Calls AdminService for warehouse CRUD operations.
        System.out.println("Admin " + getName() + " is managing warehouse data (e.g., adding capacity).");
    }

    /**
     * Manages the vehicle fleet (add, update status, assign to agents).
     * Corresponds to 'manageVehicle()' in the Class Diagram.
     */
    public void manageVehicle() {
        // Calls AdminService to interact with VehicleDAO (e.g., set status to 'Maintenance').
        System.out.println("Admin " + getName() + " is managing vehicle fleet and status.");
    }

    /**
     * Reviews and updates the status of user-submitted complaints and issues.
     * Corresponds to 'resolveComplaints()' in the Class Diagram and 'Manage Issues' use case.
     */
    public void resolveComplaints() {
        // Calls AdminService to retrieve and update complaint records.
        System.out.println("Admin " + getName() + " is reviewing and resolving user complaints.");
    }
    
    /**
     * Validates new vendor accounts (retailers, warehouses, delivery agents) based on documents.
     * [cite_start]Corresponds to the 'Verify Vendor Data' use case and Admin's role in moderating the system[cite: 64].
     */
    public void verifyVendorData(int vendorID, String vendorType) {
        // Calls AdminService to update the 'is_validated' status for a user/retailer.
        System.out.println("Admin is validating " + vendorType + " (ID: " + vendorID + ") documents.");
    }

    // --- ABSTRACT METHOD IMPLEMENTATIONS ---
    
    @Override
    public boolean login(String enteredEmail, String enteredPassword) {
        // Fix: UserService is now accessible due to the import.
        UserService service = new UserService();
        User authenticatedUser = service.loginUser(enteredEmail, enteredPassword);
    
        // Check if authentication succeeded AND the retrieved role matches the expected role
        if (authenticatedUser != null && authenticatedUser.getRole().equals(this.getRole())) {
            // If successful, update the current object's state (essential for the session)
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
