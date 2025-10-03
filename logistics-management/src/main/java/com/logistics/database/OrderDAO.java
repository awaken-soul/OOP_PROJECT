package com.logistics.database;

import com.logistics.models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO implements Dao<Order> {

    private final Connection connection;

    public OrderDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    @Override
    public Optional<Order> findById(int id) {
        String sql = "SELECT * FROM orders WHERE order_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding order by id.", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all orders.", e);
        }
        return orders;
    }

    public List<Order> findByAgentId(int agentId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE assigned_agent_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders by agent id.", e);
        }
        return orders;
    }

     public List<Order> findByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders by user id.", e);
        }
        return orders;
    }
    
    @Override
    public boolean save(Order order) {
        // Implementation for saving a new order
        // This would be more complex in a real app, setting all fields
        String sql = "INSERT INTO orders(user_id, product_id, order_type, source_address, destination_address, status, payment_status) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getProductId());
            pstmt.setString(3, order.getOrderType());
            pstmt.setString(4, order.getSourceAddress());
            pstmt.setString(5, order.getDestinationAddress());
            pstmt.setString(6, order.getStatus());
            pstmt.setString(7, order.getPaymentStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving order.", e);
        }
    }

    @Override
    public boolean update(Order order) {
        // Implementation for updating an existing order
        return false; // Placeholder
    }

    public boolean updateStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status =?, updated_at = CURRENT_TIMESTAMP WHERE order_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating order status.", e);
        }
    }

    public List<Order> findByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding orders by status.", e);
        }
        return orders;
    }

    public boolean assignAgentAndVehicle(int orderId, int agentId, int vehicleId, String newStatus) {
        String sql = "UPDATE orders SET assigned_agent_id =?, vehicle_id =?, status =?, updated_at = CURRENT_TIMESTAMP WHERE order_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agentId);
            pstmt.setInt(2, vehicleId);
            pstmt.setString(3, newStatus);
            pstmt.setInt(4, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error assigning agent to order.", e);
        }
    }


    @Override
    public boolean delete(Order order) {
        // Implementation for deleting an order
        return false; // Placeholder
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("order_id"),
                rs.getInt("user_id"),
                rs.getInt("product_id"),
                rs.getString("order_type"),
                rs.getString("source_address"),
                rs.getString("destination_address"),
                rs.getString("status"),
                rs.getInt("assigned_agent_id"),
                rs.getInt("vehicle_id"),
                rs.getString("payment_status"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}
