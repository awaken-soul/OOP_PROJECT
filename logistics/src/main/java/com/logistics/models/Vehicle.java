package com.logistics.models;

/**
 * Represents a transport unit in the system (e.g., Truck, Van, Bike).
 * Tracks status, location, and the assigned Delivery Agent.
 */
public class Vehicle {
    
    // Attributes from the Vehicle Table 
    private int vehicleID;
    private String vehicleType; // Enum: Truck, Van, Bike, etc.
    private String licensePlate; // Unique
    private String status; // Enum: Available, On Delivery, Maintenance
    private Integer driverID; // FK to User table (Delivery Agent), can be null if unassigned
    private String currentLocation; // Optional coordinates/address

    // --- CONSTRUCTORS ---

    /**
     * Creation Constructor: Used when the Admin adds a new vehicle to the fleet.
     * @param vehicleType The type of vehicle.
     * @param licensePlate The unique license plate number.
     * @param driverID The ID of the initial Delivery Agent assigned (can be null).
     * @param currentLocation The vehicle's starting location.
     */
    public Vehicle(String vehicleType, String licensePlate, Integer driverID, String currentLocation) {
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.driverID = driverID;
        this.currentLocation = currentLocation;
        this.status = "Available"; // Default status upon creation
        this.vehicleID = -1; // Temporary placeholder
    }

    /**
     * Full Constructor: Used when retrieving an existing vehicle from the database.
     */
    public Vehicle(int vehicleID, String vehicleType, String licensePlate, String status, Integer driverID, String currentLocation) {
        this.vehicleID = vehicleID;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.status = status;
        this.driverID = driverID;
        this.currentLocation = currentLocation;
    }

    // --- GETTERS AND SETTERS ---
    
    // Getters
    public int getVehicleID() { return vehicleID; }
    public String getVehicleType() { return vehicleType; }
    public String getLicensePlate() { return licensePlate; }
    public String getStatus() { return status; }
    public Integer getDriverID() { return driverID; }
    public String getCurrentLocation() { return currentLocation; }

    // Setters (Used by AdminService or OrderService for updates)
    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; } // Set after DB insertion
    public void setStatus(String status) { this.status = status; }
    public void setDriverID(Integer driverID) { this.driverID = driverID; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    
    // --- UTILITY ---
    
    @Override
    public String toString() {
        return String.format("Vehicle [ID=%d, Type=%s, Plate=%s, Status=%s, DriverID=%s]", 
                             vehicleID, vehicleType, licensePlate, status, 
                             driverID != null ? String.valueOf(driverID) : "Unassigned");
    }
}
