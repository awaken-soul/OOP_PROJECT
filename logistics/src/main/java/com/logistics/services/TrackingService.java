package com.logistics.services;

import com.logistics.database.TrackingDAO;
import com.logistics.database.TrackingDAO.TrackingRecord;

import java.util.List;

/**
 * Handles all business logic related to shipment tracking and reporting history.
 */
public class TrackingService {

    private final TrackingDAO trackingDAO;

    public TrackingService() {
        this.trackingDAO = new TrackingDAO();
    }

    /**
     * Retrieves and formats the complete history for a specific order.
     * Corresponds to the Customer's 'trackOrder()' function.
     * @param orderId The ID of the order.
     * @return A formatted string of the entire tracking history.
     */
    public String getFormattedTrackingHistory(int orderId) {
        List<TrackingRecord> history = trackingDAO.getTrackingHistory(orderId);

        if (history.isEmpty()) {
            return "Tracking ID " + orderId + " not found or no tracking events recorded yet.";
        }

        StringBuilder sb = new StringBuilder("Tracking History for Order ID: " + orderId + "\n");
        
        // Loop through records, newest first (optional, ASC/DESC sorting in DAO)
        for (TrackingRecord record : history) {
            sb.append("  -> ");
            sb.append(record.timestamp.toLocalTime());
            sb.append(": ");
            sb.append(record.status);
            sb.append(record.location != null ? " at " + record.location : "");
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Retrieves only the latest status update.
     * @param orderId The ID of the order.
     * @return The latest status string.
     */
    public String getLatestStatus(int orderId) {
        List<TrackingRecord> history = trackingDAO.getTrackingHistory(orderId);
        
        if (history.isEmpty()) {
            return "Pending tracking data.";
        }
        // Assuming the DAO query returns chronologically ordered history (ASC)
        TrackingRecord latest = history.get(history.size() - 1);
        return latest.status;
    }
}
