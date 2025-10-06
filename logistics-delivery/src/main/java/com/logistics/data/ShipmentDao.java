package com.logistics.data;

import com.logistics.model.Shipment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations for the Shipment entity.
 */
public class ShipmentDao {

    /**
     * Creates a new shipment in the database.
     *
     * @param customerId  The ID of the customer placing the order.
     * @param productId   The ID of the product being shipped.
     * @param source      The origin address.
     * @param destination The destination address.
     * @return true if the creation was successful, false otherwise.
     */
    public boolean createShipment(int customerId, int productId, String source, String destination) {
        String sql = "INSERT INTO shipments (customerId, productId, source, destination, status) VALUES (?, ?, ?, ?, 'REQUESTED')";
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, productId);
            stmt.setString(3, source);
            stmt.setString(4, destination);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating shipment.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds all shipments for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of Shipment objects.
     */
    public List<Shipment> findByCustomerId(int customerId) {
        String sql = "SELECT s.*, p.name AS productName FROM shipments s JOIN products p ON s.productId = p.id WHERE s.customerId = ?";
        List<Shipment> shipments = new ArrayList<>();
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shipments.add(mapRowToShipment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding shipments by customer ID.");
            e.printStackTrace();
        }
        return shipments;
    }

    /**
     * Finds all shipments with a specific status. Used by agents to find available jobs.
     *
     * @param status The status to search for (e.g., "REQUESTED").
     * @return A list of Shipment objects.
     */
    public List<Shipment> findShipmentsByStatus(String status) {
        String sql = "SELECT s.*, p.name AS productName FROM shipments s JOIN products p ON s.productId = p.id WHERE s.status = ?";
        List<Shipment> shipments = new ArrayList<>();
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shipments.add(mapRowToShipment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding shipments by status.");
            e.printStackTrace();
        }
        return shipments;
    }

    /**
     * Finds all shipments assigned to a specific agent.
     *
     * @param agentId The ID of the agent.
     * @return A list of Shipment objects.
     */
    public List<Shipment> findShipmentsByAgentId(int agentId) {
        String sql = "SELECT s.*, p.name AS productName FROM shipments s JOIN products p ON s.productId = p.id WHERE s.agentId = ?";
        List<Shipment> shipments = new ArrayList<>();
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shipments.add(mapRowToShipment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shipments;
    }


    /**
     * Updates a shipment's status and assigns an agent to it.
     *
     * @param shipmentId The ID of the shipment to update.
     * @param agentId    The ID of the agent to assign.
     * @param newStatus  The new status of the shipment.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateShipmentAgentAndStatus(int shipmentId, int agentId, String newStatus) {
        String sql = "UPDATE shipments SET agentId = ?, status = ? WHERE id = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, agentId);
            stmt.setString(2, newStatus);
            stmt.setInt(3, shipmentId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating shipment status and agent.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a shipment's status and warehouse.
     *
     * @param shipmentId The ID of the shipment to update.
     * @param warehouseId The ID of the warehouse to assign.
     * @param newStatus   The new status of the shipment.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateShipmentWarehouseAndStatus(int shipmentId, int warehouseId, String newStatus) {
        String sql = "UPDATE shipments SET warehouseId = ?, status = ? WHERE id = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, warehouseId);
            stmt.setString(2, newStatus);
            stmt.setInt(3, shipmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates only the status of a shipment. Used for marking as "DELIVERED".
     *
     * @param shipmentId The ID of the shipment to update.
     * @param newStatus  The new status.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateShipmentStatus(int shipmentId, String newStatus) {
        String sql = "UPDATE shipments SET status = ? WHERE id = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, shipmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Helper method to map a row from the ResultSet to a Shipment object.
     * This avoids code duplication in the find methods.
     *
     * @param rs The ResultSet containing shipment data.
     * @return A Shipment object.
     * @throws SQLException If a column is not found.
     */
    private Shipment mapRowToShipment(ResultSet rs) throws SQLException {
        return new Shipment(
                rs.getInt("id"),
                rs.getInt("customerId"),
                rs.getInt("productId"),
                rs.getString("productName"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getString("status"),
                (Integer) rs.getObject("agentId"),
                (Integer) rs.getObject("warehouseId")
        );
    }
}

