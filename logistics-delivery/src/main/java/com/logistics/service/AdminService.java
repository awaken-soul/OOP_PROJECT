package com.logistics.service;

import com.logistics.data.WarehouseDao;
import com.logistics.model.Warehouse;
import java.util.List;

/**
 * Service layer for handling business logic related to Admin operations.
 */
public class AdminService {

    private final WarehouseDao warehouseDao;
    private final AuthService authService; // Uses another service for creating users

    public AdminService(AuthService authService, WarehouseDao warehouseDao) {
        this.warehouseDao = warehouseDao;
        this.authService = authService;
    }

    /**
     * Adds a new agent user to the system.
     * @param email The new agent's email.
     * @param password The new agent's password.
     * @return true if the agent was added successfully.
     */
    public boolean addAgent(String email, String password) {
        // Delegates the user creation logic to the AuthService
        return authService.signUp(email, password, "AGENT");
    }

    /**
     * Adds a new warehouse to the system.
     * @param name The name of the warehouse.
     * @param location The location of the warehouse.
     * @return true if the warehouse was added successfully.
     */
    public boolean addWarehouse(String name, String location) {
        if (name == null || name.trim().isEmpty() || location == null || location.trim().isEmpty()) {
            return false;
        }
        return warehouseDao.createWarehouse(name, location);
    }

    /**
     * Retrieves all warehouses.
     * @return A list of all Warehouse objects.
     */
    public List<Warehouse> getAllWarehouses() {
        return warehouseDao.getAllWarehouses();
    }
}
