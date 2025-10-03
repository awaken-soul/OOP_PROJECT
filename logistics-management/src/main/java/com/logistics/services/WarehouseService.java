package com.logistics.services;

import com.logistics.database.WarehouseDAO;
import com.logistics.models.Warehouse;

import java.util.List;

public class WarehouseService {

    private final WarehouseDAO warehouseDAO;

    public WarehouseService(WarehouseDAO warehouseDAO) {
        this.warehouseDAO = warehouseDAO;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseDAO.findAll();
    }

    public boolean addWarehouse(Warehouse warehouse) {
        return warehouseDAO.save(warehouse);
    }

    public boolean updateWarehouse(Warehouse warehouse) {
        return warehouseDAO.update(warehouse);
    }

    public boolean deleteWarehouse(Warehouse warehouse) {
        return warehouseDAO.delete(warehouse);
    }
}
