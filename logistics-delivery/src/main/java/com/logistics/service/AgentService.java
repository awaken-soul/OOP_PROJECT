package com.logistics.service;

import com.logistics.data.ShipmentDao;
import com.logistics.data.WarehouseDao;
import com.logistics.model.Shipment;
import com.logistics.model.Warehouse;

import java.util.List;

/**
 * Provides business logic for agent operations.
 */
public class AgentService {

    private final ShipmentDao shipmentDao;
    private final WarehouseDao warehouseDao; // <-- ADD THIS

    // --- FIX 1: UPDATE THE CONSTRUCTOR ---
    public AgentService(ShipmentDao shipmentDao, WarehouseDao warehouseDao) {
        this.shipmentDao = shipmentDao;
        this.warehouseDao = warehouseDao; // <-- ADD THIS
    }

    public List<Shipment> getAvailableShipments() {
        return shipmentDao.findShipmentsByStatus("REQUESTED");
    }

    public List<Shipment> getMyAcceptedShipments(int agentId) {
        return shipmentDao.findShipmentsByAgentId(agentId);
    }

    public boolean acceptShipment(int shipmentId, int agentId) {
        return shipmentDao.updateShipmentAgentAndStatus(shipmentId, agentId, "ACCEPTED");
    }

    public boolean moveShipmentToWarehouse(int shipmentId, int warehouseId) {
        return shipmentDao.updateShipmentWarehouseAndStatus(shipmentId, warehouseId, "IN_WAREHOUSE");
    }

    public boolean markShipmentDelivered(int shipmentId) {
        return shipmentDao.updateShipmentStatus(shipmentId, "DELIVERED");
    }

    // --- FIX 2: ADD THE MISSING METHOD ---
    public List<Warehouse> getAllWarehouses() {
        return warehouseDao.getAllWarehouses();
    }
}

