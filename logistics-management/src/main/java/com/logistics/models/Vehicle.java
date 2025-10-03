
package com.logistics.models;

public class Vehicle {
    private int vehicleId;
    private String vehicleType;
    private String licensePlate;
    private String status;
    private int driverId;
    private String currentLocation;

    public Vehicle(String vehicleType, String licensePlate, String status, String currentLocation) {
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.status = status;
        this.currentLocation = currentLocation;
    }
    
    public Vehicle(int vehicleId, String vehicleType, String licensePlate, String status, int driverId, String currentLocation) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.status = status;
        this.driverId = driverId;
        this.currentLocation = currentLocation;
    }

    // Getters and Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
}
