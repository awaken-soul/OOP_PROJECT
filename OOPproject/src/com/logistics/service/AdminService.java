package com.logistics.service;

import com.logistics.dao.UserDAO;
import com.logistics.dao.WarehouseDao;
import com.logistics.model.User;
import com.logistics.model.Warehouse;

import java.util.List;

/**
 * Business logic for admin actions: managing warehouses and agents.
 */
public class AdminService {

    private final UserDAO userDAO;
    private final WarehouseDao warehouseDao;

    public AdminService(UserDAO userDAO, WarehouseDao warehouseDao) {
        this.userDAO = userDAO;
        this.warehouseDao = warehouseDao;
    }

    /** Add Agent (role = 'Agent') */
    public boolean addAgent(String name, String email, String password, String contact, String address) {
        return userDAO.createUser(name, email, password, "Agent", contact, address);
    }

    /** Get all Agents (role='Agent') */
    public List<User> getAllAgents() {
        return userDAO.getUsersByRole("Agent");
    }

    /** Add Warehouse */
    public boolean addWarehouse(String name, String address, int capacity, int managerId) {
        return warehouseDao.createWarehouse(name, address, capacity, managerId);
    }

    /** Get all Warehouses */
    public List<Warehouse> getAllWarehouses() {
        return warehouseDao.getAllWarehouses();
    }
}
