package com.logistics.database;

import com.logistics.models.Tracking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackingDAO {

    private final Connection connection;

    public TrackingDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    public boolean save(Tracking tracking) {
        String sql = "INSERT INTO tracking(order_id, agent_id, current_status, location, updated_at) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tracking.getOrderId());
            // agent_id can be nullable if the update is system-generated (e.g., "Pending")
            if (tracking.getAgentId() > 0) {
                pstmt.setInt(2, tracking.getAgentId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, tracking.getCurrentStatus());
            pstmt.setString(4, tracking.getLocation());
            pstmt.setTimestamp(5, Timestamp.valueOf(tracking.getUpdatedAt()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving tracking entry.", e);
        }
    }

    public List<Tracking> findByOrderId(int orderId) {
        List<Tracking> trackingHistory = new ArrayList<>();
        String sql = "SELECT * FROM tracking WHERE order_id =? ORDER BY updated_at ASC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                trackingHistory.add(mapRowToTracking(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding tracking history by order id.", e);
        }
        return trackingHistory;
    }

    private Tracking mapRowToTracking(ResultSet rs) throws SQLException {
        return new Tracking(
                rs.getInt("tracking_id"),
                rs.getInt("order_id"),
                rs.getInt("agent_id"),
                rs.getString("current_status"),
                rs.getString("location"),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}
