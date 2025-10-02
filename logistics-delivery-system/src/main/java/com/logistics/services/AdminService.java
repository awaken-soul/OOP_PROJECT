package com.logistics.services;

import com.logistics.database.UserDAO;
import com.logistics.database.VehicleDAO;
import com.logistics.models.DeliveryAgent;
import com.logistics.models.Vehicle;

/**
 * Handles all business logic for the Administrator role, including moderation, 
 * vendor validation, and system overview.
 */
public class AdminService {

    private final UserDAO userDAO;
    private final VehicleDAO vehicleDAO;

    public AdminService() {
        this.userDAO = new UserDAO();
        this.vehicleDAO = new VehicleDAO();
    }

    // --- Core Functional Requirement: Validate Agents/Vendors ---
    
    /**
     * Moderates and validates new Delivery Agents and Retailers before activating them.
     * Corresponds to 'Verify Vendor Data' use case[cite: 32].
     */
    public boolean validateVendor(int userId, String role, boolean approved) {
        // NOTE: In a full system, separate DAO methods exist for Retailer/Warehouse validation.
        // For simplicity, this simulates updating a user/vendor's 'is_approved' status.
        System.out.println("AdminService: Validating " + role + " with ID " + userId + " - Approval: " + approved);
        // userDAO.updateValidationStatus(userId, approved);
        return true; 
    }

    // --- Core Functional Requirement: Manage Vehicles ---
    
    /**
     * Adds a new vehicle to the fleet and assigns it a driver if specified.
     */
    public boolean addNewVehicle(String type, String plate, Integer driverId, String location) {
        Vehicle newVehicle = new Vehicle(type, plate, driverId, location);
        return vehicleDAO.saveNewVehicle(newVehicle);
    }
    
    /**
     * Updates a vehicle's status and location for maintenance or reassignment.
     */
    public boolean updateVehicleStatus(int vehicleId, String newStatus, String newLocation) {
        System.out.println("AdminService: Updating vehicle " + vehicleId + " status to " + newStatus);
        return vehicleDAO.updateVehicleStatus(vehicleId, newStatus, newLocation);
    }

    // --- Core Functional Requirement: Resolve Complaints ---
    
    /**
     * Retrieves and closes a complaint record.
     * Corresponds to 'Manage Issues' use case[cite: 36].
     */
    public boolean closeComplaint(int complaintId, String resolutionNotes) {
        // This would require a ComplaintDAO, but for now, it's a simulated service call.
        System.out.println("AdminService: Complaint " + complaintId + " resolved. Notes: " + resolutionNotes);
        return true; 
    }
}
