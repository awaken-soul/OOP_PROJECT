package com.logistics.database;

import com.logistics.models.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations (CRUD) for the Product/Inventory table.
 * Supports Warehouse Manager and Retailer functions.
 */
public class InventoryDAO {

    /**
     * Maps a ResultSet row to an Inventory object.
     */
    private Inventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        return new Inventory(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getInt("quantity"), // stockLevel
            rs.getInt("warehouse_id"),
            rs.getObject("retailer_id") != null ? rs.getInt("retailer_id") : null
        );
    }

    /**
     * Retrieves all products/inventory items managed by a specific warehouse.
     * @param warehouseId The ID of the warehouse.
     * @return A list of Inventory objects.
     */
    public List<Inventory> getInventoryByWarehouse(int warehouseId) {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE warehouse_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, warehouseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    inventoryList.add(mapResultSetToInventory(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving inventory: " + e.getMessage());
        }
        return inventoryList;
    }

    /**
     * Updates the stock level for an existing product.
     * Corresponds to 'assignProducts' logic (reducing stock) or receiving new stock.
     */
    public boolean updateStockLevel(int productId, int newQuantity) {
        String sql = "UPDATE Product SET quantity = ? WHERE product_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error updating stock: " + e.getMessage());
            return false;
        }
    }
    
    // NOTE: Additional methods (e.g., saveNewProduct, deleteProduct) would be added as needed.
}
