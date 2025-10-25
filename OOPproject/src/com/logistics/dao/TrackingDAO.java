package com.logistics.dao;

import com.logistics.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackingDAO {

    // Get all active deliveries for a user
    public List<Tracking> getActiveTracking(int userId) {
        List<Tracking> trackingList = new ArrayList<>();

        String sql = "SELECT o.order_id, p.name AS product_name, o.status AS order_status, " +
                     "t.current_status, t.location, t.updated_at " +
                     "FROM Orders o " +
                     "LEFT JOIN Product p ON o.product_id = p.product_id " +
                     "LEFT JOIN Tracking t ON o.order_id = t.order_id " +
                     "WHERE o.user_id = ? AND o.status != 'Delivered' " +
                     "ORDER BY t.updated_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Tracking tracking = new Tracking(
                        rs.getInt("order_id"),
                        rs.getString("product_name") != null ? rs.getString("product_name") : "N/A",
                        rs.getString("order_status") != null ? rs.getString("order_status") : "N/A",
                        rs.getString("current_status") != null ? rs.getString("current_status") : "N/A",
                        rs.getString("location") != null ? rs.getString("location") : "N/A",
                        rs.getTimestamp("updated_at")
                );
                trackingList.add(tracking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trackingList;
    }
}
