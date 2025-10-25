package com.logistics.service;

import com.logistics.dao.*;
import com.logistics.model.*;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
public class ManagerService {

	
    private final WarehouseDao warehouseDao = new WarehouseDao();
    private final OrderDAO orderDao = new OrderDAO();
    private final ProductDAO productDao = new ProductDAO();

    /** ✅ Fetch all warehouses managed by this manager */
    public List<Warehouse> getWarehousesByManager(int managerId) {
        return warehouseDao.getWarehousesByManager(managerId);
    }

    /** ✅ Fetch orders related to this manager’s warehouses */
  /*  public List<Order> getOrdersForManager(int managerId) {
        List<Order> allOrders = orderDao.getAvailableOrders();
        List<Warehouse> managerWarehouses = warehouseDao.getWarehousesByManager(managerId);
        List<String> managedWarehouseNames = new ArrayList<>();

        for (Warehouse w : managerWarehouses) {
            managedWarehouseNames.add(w.getName());
        }

        List<Order> filteredOrders = new ArrayList<>();
        for (Order o : allOrders) {
            if (o.getWarehouseName() != null &&
                managedWarehouseNames.contains(o.getWarehouseName())) {
                filteredOrders.add(o);
            }
        }

        return filteredOrders;
    }
*/
public List<Order> getOrdersForManager(int managerId) {
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
        JOIN Warehouse w ON p.warehouse_id = w.warehouse_id
        WHERE w.manager_id = ?
        AND o.status NOT IN ('Delivered', 'Out for Delivery')
        AND o.payment_status != 'Pending'
        ORDER BY o.order_id DESC
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, managerId);
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

    /** ✅ Fetch products (inventory) for a given warehouse */
    public List<Product> getInventoryByWarehouse(int warehouseId) {
        List<Product> allProducts = productDao.getAllProducts();
        List<Product> filtered = new ArrayList<>();

        for (Product p : allProducts) {
            // each product belongs to a warehouse (via warehouse_id)
            try {
                // fetch directly from DB if ProductDAO stores warehouse_id internally
                String sql = "SELECT warehouse_id FROM Product WHERE product_id = " + p.getProductId();
                try (var conn = DBConnection.getConnection();
                     var stmt = conn.createStatement();
                     var rs = stmt.executeQuery(sql)) {
                    if (rs.next() && rs.getInt("warehouse_id") == warehouseId) {
                        filtered.add(p);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filtered;
    }

    /** ✅ Assign agent to a given order */
    public boolean assignAgent(int orderId, int agentId) {
        return orderDao.assignAgent(orderId, agentId);
    }

    /** ✅ Update stock quantity for a given product */
    public boolean updateStock(int productId, int newQuantity) {
        String sql = "UPDATE Product SET quantity = ? WHERE product_id = ?";
        try (var conn = DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
