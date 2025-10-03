package com.logistics.services;

import com.logistics.database.TrackingDAO;
import com.logistics.database.TrackingDAO.TrackingRecord;
import java.util.List;

public class TrackingService {

    private final TrackingDAO trackingDAO;

    public TrackingService() {
        this.trackingDAO = new TrackingDAO();
    }
    
    public String getFormattedTrackingHistory(int orderId) {
        List<TrackingRecord> history = trackingDAO.getTrackingHistory(orderId);

        if (history.isEmpty()) {
            return "Tracking ID " + orderId + " not found or no tracking events recorded yet.";
        }
        StringBuilder sb = new StringBuilder("Tracking History for Order ID: " + orderId + "\n");
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
    
    public String getLatestStatus(int orderId) {
        List<TrackingRecord> history = trackingDAO.getTrackingHistory(orderId);
        
        if (history.isEmpty()) {
            return "Pending tracking data.";
        }
        TrackingRecord latest = history.get(history.size() - 1);
        return latest.status;
    }
}
