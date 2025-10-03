package com.logistics.models;

public class Vehicle {
    
   
    private int vehicleID;
    private String vehicleType; 
    private String licensePlate;
    private String status; 
    private Integer driverID; 
    private String currentLocation;

    public Vehicle(String vehicleType, String licensePlate, Integer driverID, String currentLocation) {
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.driverID = driverID;
        this.currentLocation = currentLocation;
        this.status = "Available";
        this.vehicleID = -1; 
    }
    public Vehicle(int vehicleID, String vehicleType, String licensePlate, String status, Integer driverID, String currentLocation) {
        this.vehicleID = vehicleID;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.status = status;
        this.driverID = driverID;
        this.currentLocation = currentLocation;
    }

    public int getVehicleID() { return vehicleID; }
    public String getVehicleType() { return vehicleType; }
    public String getLicensePlate() { return licensePlate; }
    public String getStatus() { return status; }
    public Integer getDriverID() { return driverID; }
    public String getCurrentLocation() { return currentLocation; }


    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; } 
    public void setStatus(String status) { this.status = status; }
    public void setDriverID(Integer driverID) { this.driverID = driverID; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    
    @Override
    public String toString() {
        return String.format("Vehicle [ID=%d, Type=%s, Plate=%s, Status=%s, DriverID=%s]", 
                             vehicleID, vehicleType, licensePlate, status, 
                             driverID != null ? String.valueOf(driverID) : "Unassigned");
    }
}
