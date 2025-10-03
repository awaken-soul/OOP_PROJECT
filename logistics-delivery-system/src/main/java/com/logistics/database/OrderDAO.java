package com.logistics.database;

import com.logistics.models.Order;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public boolean saveNewOrder(Order order) {
        String sql = "INSERT INTO Orders (user_id, product_id, order_type, source_address, destination_address, status, payment_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            LocalDateTime now = LocalDateTime.now();
            
            pstmt.setInt(1, order.getUserID());
    
            if (order.getProductID() == null) {
                pstmt.setNull(2, Types.INTEGER); 
            } else {
                pstmt.setInt(2, order.getProductID());
            }
            pstmt.setString(3, order.getOrderType());
            pstmt.setString(4, order.getSourceAddress());
            pstmt.setString(5, order.getDestinationAddress());
            pstmt.setString(6, order.getStatus());
            pstmt.setString(7, order.getPaymentStatus());
            pstmt.setString(8, now.toString());
            pstmt.setString(9, now.toString());

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
              
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newID = generatedKeys.getInt(1);
                        order.setOrderID(newID); 
                        System.out.println("New order saved successfully with ID: " + newID);
                        createInitialTrackingRecord(conn, newID, order.getStatus());
                        
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Database error during order creation: " + e.getMessage());
            return false;
        }
    }
    
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during order retrieval: " + e.getMessage());
        }
        return null;
    }

    public boolean updateOrderStatus(int orderId, String newStatus, Integer agentId, Integer vehicleId) {
        String sql = "UPDATE Orders SET status = ?, assigned_agent_id = ?, vehicle_id = ?, updated_at = ? WHERE order_id = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            if (agentId == null) {
                 pstmt.setNull(2, Types.INTEGER);
            } else {
                 pstmt.setInt(2, agentId);
            }
            if (vehicleId == null) {
                 pstmt.setNull(3, Types.INTEGER);
            } else {
                 pstmt.setInt(3, vehicleId);
            }
            pstmt.setString(4, LocalDateTime.now().toString());
            pstmt.setInt(5, orderId);

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                updateTrackingRecord(conn, orderId, newStatus, agentId, null); 
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Database error during order status update: " + e.getMessage());
            return false;
        }
    }
    
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        return new Order(
            rs.getInt("order_id"),
            rs.getInt("user_id"),
            rs.getObject("product_id") != null ? rs.getInt("product_id") : null,
            rs.getString("order_type"),
            rs.getString("source_address"),
            rs.getString("destination_address"),
            rs.getString("status"),
            rs.getObject("assigned_agent_id") != null ? rs.getInt("assigned_agent_id") : null,
            rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null,
            rs.getString("payment_status"),
            LocalDateTime.parse(rs.getString("created_at")),
            LocalDateTime.parse(rs.getString("updated_at"))
        );
    }
    
    private void createInitialTrackingRecord(Connection conn, int orderId, String status) throws SQLException {
        String sql = "INSERT INTO Tracking (order_id, current_status, updated_at) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setString(2, status);
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.executeUpdate();
            System.out.println("Tracking record initialized for Order ID: " + orderId);
        }
    }
    
    public void updateTrackingRecord(Connection conn, int orderId, String newStatus, Integer agentId, String location) throws SQLException {
        String sql = "INSERT INTO Tracking (order_id, agent_id, current_status, location, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            if (agentId == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, agentId);
            }
            pstmt.setString(3, newStatus);
            pstmt.setString(4, location);
            pstmt.setString(5, LocalDateTime.now().toString());
            pstmt.executeUpdate();
            System.out.println("Tracking update recorded for Order ID: " + orderId);
        } catch (SQLException e) {
            System.err.println("Error updating tracking record: " + e.getMessage());
            throw e; 
        }
    }
    
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving orders by user ID: " + e.getMessage());
        }
        return orders;
    }
   
    public boolean updatePaymentStatusField(int orderId, String newPaymentStatus) {
        String sql = "UPDATE Orders SET payment_status = ?, updated_at = ? WHERE order_id = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPaymentStatus);
            pstmt.setString(2, LocalDateTime.now().toString());
            pstmt.setInt(3, orderId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DB Error updating order payment status: " + e.getMessage());
            return false;
        }
    }
    public List<Order> getOrdersByAgentId(int agentId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE assigned_agent_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, agentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving orders by agent ID: " + e.getMessage());
        }
        return orders;
    }
}
