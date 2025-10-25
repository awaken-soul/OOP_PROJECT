package com.logistics.dao;

import com.logistics.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // ✅ Fetch active orders for a given user (used in CustomerDashboard)
    public List<Order> getActiveOrders(int userId) {
        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT o.order_id, o.user_id, o.product_id, o.order_type,
                   o.source_address, o.destination_address,
                   o.status, o.payment_status,
                   u.name AS customer_name,
                   p.name AS product_name,
                   w.name AS warehouse_name
            FROM Orders o
            JOIN User u ON o.user_id = u.user_id
            JOIN Product p ON o.product_id = p.product_id
            LEFT JOIN Warehouse w ON p.warehouse_id = w.warehouse_id
            WHERE o.user_id = ?
            ORDER BY o.order_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setProductId(rs.getInt("product_id"));
                order.setOrderType(rs.getString("order_type"));
                order.setSourceAddress(rs.getString("source_address"));
                order.setDestinationAddress(rs.getString("destination_address"));
                order.setStatus(rs.getString("status"));
                order.setPaymentStatus(rs.getString("payment_status"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setProductName(rs.getString("product_name"));
                order.setWarehouseName(rs.getString("warehouse_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // ✅ Fetch available (unassigned) orders for agents (Pending + Paid)
    public List<Order> getAvailableOrders() {
        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT o.order_id, o.user_id, o.product_id, o.order_type,
                   o.source_address, o.destination_address,
                   o.status, 
                   u.name AS customer_name,
                   p.name AS product_name,
                   w.name AS warehouse_name
            FROM Orders o
            JOIN User u ON o.user_id = u.user_id
            JOIN Product p ON o.product_id = p.product_id
            JOIN Payment y ON o.order_id = y.order_id
            LEFT JOIN Warehouse w ON p.warehouse_id = w.warehouse_id
            WHERE y.status = 'Completed' 
              AND (o.status = 'Pending' OR o.status = 'In Warehouse')
              AND o.assigned_agent_id IS NULL
            ORDER BY o.order_id DESC
        """;

      
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setProductId(rs.getInt("product_id"));
                order.setOrderType(rs.getString("order_type"));
                order.setSourceAddress(rs.getString("source_address"));
                order.setDestinationAddress(rs.getString("destination_address"));
                order.setStatus(rs.getString("status"));
 //               order.setPaymentStatus(rs.getString("payment_status"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setProductName(rs.getString("product_name"));
                order.setWarehouseName(rs.getString("warehouse_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // ✅ Fetch orders assigned to a specific agent
    public List<Order> getOrdersByAgent(int agentId) {
        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT o.order_id, o.user_id, o.product_id, o.order_type,
                   o.source_address, o.destination_address,
                   o.status, o.payment_status,
                   u.name AS customer_name,
                   p.name AS product_name,
                   w.name AS warehouse_name
            FROM Orders o
            JOIN User u ON o.user_id = u.user_id
            JOIN Product p ON o.product_id = p.product_id
            LEFT JOIN Warehouse w ON p.warehouse_id = w.warehouse_id
            WHERE o.assigned_agent_id = ?
            ORDER BY o.order_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, agentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setProductId(rs.getInt("product_id"));
                order.setOrderType(rs.getString("order_type"));
                order.setSourceAddress(rs.getString("source_address"));
                order.setDestinationAddress(rs.getString("destination_address"));
                order.setStatus(rs.getString("status"));
                order.setPaymentStatus(rs.getString("payment_status"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setProductName(rs.getString("product_name"));
                order.setWarehouseName(rs.getString("warehouse_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
/*
    // ✅ Assign order to an agent
    public boolean assignAgent(int orderId, int agentId) {
        String sql = "UPDATE Orders SET assigned_agent_id = ?, status = 'In Warehouse' WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agentId);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql2 = "UPDATE Tracking SET current_status = 'In Warehouse', location = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps2 = conn.prepareStatement(sql2)) {
            ps2.setString(1, "Central Warehouse");
            ps2.setInt(2, orderId);
            return ps2.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

/*    // ✅ Move shipment/order to a warehouse
    public boolean moveOrderToWarehouse(int orderId, int warehouseId) {
        String sql = "UPDATE Orders SET status = 'In Warehouse' WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
         //   ps.setInt(1, warehouseId);
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql2 = """
        	    UPDATE Tracking
        	    SET current_status = 'In Warehouse',
        	        location = (SELECT warehouse_name FROM Warehouse WHERE warehouse_id = ?)
        	    WHERE order_id = ?
        	""";
        		
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps3 = conn.prepareStatement(sql2)) {
            ps3.setInt(1, warehouseId);
            ps3.setInt(2, orderId);
            return ps3.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 */
 /*   public boolean assignAgent(int orderId, int agentId) {
        boolean orderUpdated = false;
        boolean trackingUpdated = false;

        String sql1 = "UPDATE Orders SET assigned_agent_id = ?, status = 'In Warehouse' WHERE order_id = ?";
        String sql2 = "UPDATE Tracking SET current_status = 'In Warehouse', location = ? WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sql1);
                 PreparedStatement ps2 = conn.prepareStatement(sql2)) {

                ps1.setInt(1, agentId);
                ps1.setInt(2, orderId);
                orderUpdated = ps1.executeUpdate() > 0;

                ps2.setString(1, "Central Warehouse");
                ps2.setInt(2, orderId);
                trackingUpdated = ps2.executeUpdate() > 0;

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Assigning agent " + agentId + " to order " + orderId);
        System.out.println("SQL executed successfully? " + orderUpdated + " / " + trackingUpdated);
        return orderUpdated && trackingUpdated;
    }
*/
    public boolean assignAgent(int orderId, int agentId) {
        boolean orderUpdated = false;
        boolean trackingUpdated = false;

        String sql1 = "UPDATE Orders SET assigned_agent_id = ?, status = 'In Warehouse' WHERE order_id = ?";
        String sqlCheck = "SELECT COUNT(*) FROM Tracking WHERE order_id = ?";
        String sqlUpdateTracking = "UPDATE Tracking SET current_status = 'In Warehouse', location = ? WHERE order_id = ?";
        String sqlInsertTracking = "INSERT INTO Tracking (order_id, agent_id, current_status, location) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sql1)) {
                ps1.setInt(1, agentId);
                ps1.setInt(2, orderId);
                orderUpdated = ps1.executeUpdate() > 0;
            }

            if (orderUpdated) {
                // check if tracking exists
                boolean trackingExists = false;
                try (PreparedStatement checkPs = conn.prepareStatement(sqlCheck)) {
                    checkPs.setInt(1, orderId);
                    ResultSet rs = checkPs.executeQuery();
                    if (rs.next()) trackingExists = rs.getInt(1) > 0;
                }

                if (trackingExists) {
                    try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateTracking)) {
                        ps2.setString(1, "Central Warehouse");
                        ps2.setInt(2, orderId);
                        trackingUpdated = ps2.executeUpdate() > 0;
                    }
                } else {
                    try (PreparedStatement ps3 = conn.prepareStatement(sqlInsertTracking)) {
                        ps3.setInt(1, orderId);
                        ps3.setInt(2, agentId);
                        ps3.setString(3, "In Warehouse");
                        ps3.setString(4, "Central Warehouse");
                        trackingUpdated = ps3.executeUpdate() > 0;
                    }
                }
            }

            conn.commit();
            System.out.println("Assigning agent " + agentId + " to order " + orderId);
            System.out.println("SQL executed successfully? " + orderUpdated + " / " + trackingUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderUpdated && trackingUpdated;
    }

    
 // ✅ Move shipment/order to a warehouse (Order + Tracking update)
    public boolean moveOrderToWarehouse(int orderId, int warehouseId) {
        boolean orderUpdated = false;
        boolean trackingUpdated = false;

        String sqlOrder = "UPDATE Orders SET status = 'In Warehouse' WHERE order_id = ?";
        String sqlTrack = """
            UPDATE Tracking
            SET current_status = 'In Warehouse',
                location = (SELECT name FROM Warehouse WHERE warehouse_id = ?)
            WHERE order_id = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlOrder);
                 PreparedStatement ps2 = conn.prepareStatement(sqlTrack)) {

                // Step 1: update Orders table
                ps1.setInt(1, orderId);
                orderUpdated = ps1.executeUpdate() > 0;

                // Step 2: update Tracking table
                ps2.setInt(1, warehouseId);
                ps2.setInt(2, orderId);
                trackingUpdated = ps2.executeUpdate() > 0;

                // If no existing tracking row, insert one
                if (!trackingUpdated) {
                    String insertTrack = """
                        INSERT INTO Tracking (order_id, agent_id, current_status, location)
                        VALUES (?, (SELECT assigned_agent_id FROM Orders WHERE order_id = ?), 'In Warehouse',
                                (SELECT name FROM Warehouse WHERE warehouse_id = ?))
                    """;
                    try (PreparedStatement ps3 = conn.prepareStatement(insertTrack)) {
                        ps3.setInt(1, orderId);
                        ps3.setInt(2, orderId);
                        ps3.setInt(3, warehouseId);
                        trackingUpdated = ps3.executeUpdate() > 0;
                    }
                }

                conn.commit();
                System.out.println("[DEBUG] moveOrderToWarehouse: Order " + orderId +
                                   " moved to Warehouse " + warehouseId +
                                   " | Orders updated: " + orderUpdated +
                                   " | Tracking updated: " + trackingUpdated);

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderUpdated && trackingUpdated;
    }
    
 // ✅ Update order status (for Agent Dashboard)
 // Keeps both Orders and Tracking tables in sync.
 public boolean updateOrderStatus(int orderId, String status) {
     boolean orderUpdated = false;
     boolean trackingUpdated = false;

     // Step 1: Update Orders table
     String sqlOrder = "UPDATE Orders SET status = ? WHERE order_id = ?";

     // Step 2: Update Tracking table (update if exists)
     String sqlTrack = """
         UPDATE Tracking
         SET current_status = ?,
             location = CASE 
                            WHEN ? = 'Out for Delivery' THEN 'On Route'
                            WHEN ? = 'Delivered' THEN 'Customer Location'
                            ELSE location
                        END,
             updated_at = CURRENT_TIMESTAMP
         WHERE order_id = ?
     """;

     // Step 3: Insert new tracking entry if not exists
     String insertTrack = """
         INSERT INTO Tracking (order_id, agent_id, current_status, location)
         SELECT ?, assigned_agent_id, ?, 
                CASE 
                    WHEN ? = 'Out for Delivery' THEN 'On Route'
                    WHEN ? = 'Delivered' THEN 'Customer Location'
                    ELSE 'Warehouse'
                END
         FROM Orders WHERE order_id = ?
         AND NOT EXISTS (SELECT 1 FROM Tracking WHERE order_id = ?)
     """;

     try (Connection conn = DBConnection.getConnection()) {
         conn.setAutoCommit(false);

         try (PreparedStatement ps1 = conn.prepareStatement(sqlOrder);
              PreparedStatement ps2 = conn.prepareStatement(sqlTrack);
              PreparedStatement ps3 = conn.prepareStatement(insertTrack)) {

             // --- Orders update ---
             ps1.setString(1, status);
             ps1.setInt(2, orderId);
             orderUpdated = ps1.executeUpdate() > 0;

             // --- Tracking update ---
             ps2.setString(1, status);
             ps2.setString(2, status);
             ps2.setString(3, status);
             ps2.setInt(4, orderId);
             trackingUpdated = ps2.executeUpdate() > 0;

             // --- If no Tracking row, insert new one ---
             if (!trackingUpdated) {
                 ps3.setInt(1, orderId);
                 ps3.setString(2, status);
                 ps3.setString(3, status);
                 ps3.setString(4, status);
                 ps3.setInt(5, orderId);
                 ps3.setInt(6, orderId);
                 trackingUpdated = ps3.executeUpdate() > 0;
             }

             conn.commit();
             System.out.println("[DEBUG] updateOrderStatus: Order " + orderId +
                                " -> " + status +
                                " | Orders updated: " + orderUpdated +
                                " | Tracking updated: " + trackingUpdated);

         } catch (SQLException e) {
             conn.rollback();
             e.printStackTrace();
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }

     return orderUpdated && trackingUpdated;
 }

}
