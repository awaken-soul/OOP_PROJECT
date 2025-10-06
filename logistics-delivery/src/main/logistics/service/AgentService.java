package com.logistics.service;

import com.logistics.data.ShipmentDao;
import com.logistics.model.Shipment;
import java.util.List;

/**
 * Service layer for handling business logic related to Agent operations.
 */
public class AgentService {

    private final ShipmentDao shipmentDao;

    public AgentService(ShipmentDao shipmentDao) {
        this.shipmentDao = shipmentDao;
    }

    /**
     * Retrieves all shipments that are available for an agent to accept.
     * These are shipments with the status 'REQUESTED'.
     * @return A list of available shipments.
     */
    public List<Shipment> getAvailableShipments() {
        return shipmentDao.findByStatus("REQUESTED");
    }

    /**
     * Retrieves all shipments assigned to a specific agent.
     * @param agentId The ID of the agent.
     * @return A list of the agent's assigned shipments.
     */
    public List<Shipment> getMyShipments(int agentId) {
        return shipmentDao.findByAgentId(agentId);
    }

    /**
     * Assigns a shipment to an agent and updates its status.
     * @param shipmentId The ID of the shipment to accept.
     * @param agentId The ID of the agent accepting the shipment.
     * @return true if the operation was successful.
     */
    public boolean acceptShipment(int shipmentId, int agentId) {
        return shipmentDao.updateShipmentAgentAndStatus(shipmentId, agentId, "ACCEPTED");
    }
}
