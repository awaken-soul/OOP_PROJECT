package com.logistics.services;

import com.logistics.database.VehicleDAO;
import com.logistics.models.Vehicle;

import java.util.List;

public class VehicleService {

    private final VehicleDAO vehicleDAO;

    public VehicleService(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    /**
     * Retrieves all vehicles that are currently available for assignment.
     * @return A list of available Vehicle objects.
     */
    public List<Vehicle> getAvailableVehicles() {
        return vehicleDAO.findAvailableVehicles();
    }
}
