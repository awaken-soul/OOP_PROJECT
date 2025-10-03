package com.logistics.services;

import com.logistics.database.TrackingDAO;
import com.logistics.models.Tracking;

import java.time.LocalDateTime;
import java.util.List;

public class TrackingService {

    private final TrackingDAO trackingDAO;

    public TrackingService(TrackingDAO trackingDAO) {
        this.trackingDAO = trackingDAO;
    }

    /**
     * Creates a new tracking entry for an order.
     * @param orderId The ID of the order being updated.
     * @param agentId The ID of the agent performing the action (can be 0 for system updates).
     * @param status The new status of the order.
     * @param location The current location associated with the status update.
     */
    public void addTrackingUpdate(int orderId, int agentId, String status, String location) {
        Tracking newEntry = new Tracking(0, orderId, agentId, status, location, LocalDateTime.now());
        trackingDAO.save(newEntry);
    }

    /**
     * Retrieves the complete tracking history for a specific order.
     * @param orderId The ID of the order.
     * @return A list of Tracking objects, ordered by time.
     */
    public List<Tracking> getTrackingHistoryForOrder(int orderId) {
        return trackingDAO.findByOrderId(orderId);
    }
}
