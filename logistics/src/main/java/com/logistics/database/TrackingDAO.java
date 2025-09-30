package com.logistics.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval for the Tracking table.
 */
public class TrackingDAO {

    // Simple inner class to represent a tracking point for retrieval
    public static class TrackingRecord {
        public String status;
        public String location;
        public LocalDateTime timestamp;
        public int agentId;

        public TrackingRecord(String status, String location, LocalDateTime timestamp, int agentId) {
            this.status = status;
            this.location = location;
            this.timestamp = timestamp;
            this.agentId = agentId;
        }
        
        @Override
        public String toString() {
             return String.format("[%s] Status: %s, Location: %s", 
                                  timestamp.toString(), status, location != null ? location : "N/A");
        }
    }

    /**
     * Retrieves the complete tracking history for a given Order ID.
     * This is used by the Customer's trackOrder() feature.
     * @param orderId The ID of the order to track.
     * @return A list of TrackingRecord objects, ordered by time.
     */
    public List<TrackingRecord> getTrackingHistory(int orderId) {
        List<TrackingRecord> history = new ArrayList<>();
        String sql = "SELECT current_status, location, updated_at, agent_id FROM Tracking WHERE order_id = ? ORDER BY updated_at ASC";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    history.add(new TrackingRecord(
                        rs.getString("current_status"),
                        rs.getString("location"),
                        LocalDateTime.parse(rs.getString("updated_at")),
                        rs.getInt("agent_id")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving tracking history: " + e.getMessage());
        }
        return history;
    }
}
