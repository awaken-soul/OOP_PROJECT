package com.logistics.services;

import com.logistics.database.WarehouseDAO;
import com.logistics.models.Warehouse;

import java.util.List;
import java.util.Optional;

public class WarehouseService {

    private final WarehouseDAO warehouseDAO;

    public WarehouseService(WarehouseDAO warehouseDAO) {
        this.warehouseDAO = warehouseDAO;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseDAO.findAll();
    }

    public boolean addWarehouse(Warehouse warehouse) {
        Optional<Integer> generatedId = warehouseDAO.save(warehouse);
        return generatedId.isPresent();
    }

    public boolean updateWarehouse(Warehouse warehouse) {
        return warehouseDAO.update(warehouse);
    }

    public boolean deleteWarehouse(Warehouse warehouse) {
        return warehouseDAO.delete(warehouse);
    }
}
