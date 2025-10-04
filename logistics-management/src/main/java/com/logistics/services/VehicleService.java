package com.logistics.services;

import com.logistics.database.VehicleDAO;
import com.logistics.models.Vehicle;

import java.util.List;

public class VehicleService {

    private final VehicleDAO vehicleDAO;

    public VehicleService(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleDAO.findAvailableVehicles();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.findAll();
    }

    public boolean addVehicle(Vehicle vehicle) {
        return vehicleDAO.save(vehicle).isPresent();
    }

    public boolean updateVehicle(Vehicle vehicle) {
        return vehicleDAO.update(vehicle);
    }

    public boolean deleteVehicle(Vehicle vehicle) {
        return vehicleDAO.delete(vehicle);
    }
}
