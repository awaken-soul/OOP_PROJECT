package com.logistics.services;

import com.logistics.database.UserDAO;
import com.logistics.database.VehicleDAO;
import com.logistics.models.DeliveryAgent;
import com.logistics.models.Vehicle;

public class AdminService {

    private final UserDAO userDAO;
    private final VehicleDAO vehicleDAO;

    public AdminService() {
        this.userDAO = new UserDAO();
        this.vehicleDAO = new VehicleDAO();
    }
    
    public boolean validateVendor(int userId, String role, boolean approved) {
        System.out.println("AdminService: Validating " + role + " with ID " + userId + " - Approval: " + approved);
        return true; 
    }

    public boolean addNewVehicle(String type, String plate, Integer driverId, String location) {
        Vehicle newVehicle = new Vehicle(type, plate, driverId, location);
        return vehicleDAO.saveNewVehicle(newVehicle);
    }

    public boolean updateVehicleStatus(int vehicleId, String newStatus, String newLocation) {
        System.out.println("AdminService: Updating vehicle " + vehicleId + " status to " + newStatus);
        return vehicleDAO.updateVehicleStatus(vehicleId, newStatus, newLocation);
    }

    public boolean closeComplaint(int complaintId, String resolutionNotes) {
        System.out.println("AdminService: Complaint " + complaintId + " resolved. Notes: " + resolutionNotes);
        return true; 
    }
}
