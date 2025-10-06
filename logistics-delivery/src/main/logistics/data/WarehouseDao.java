package com.logistics.data;

import com.logistics.model.Warehouse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations related to the Warehouse entity.
 */
public class WarehouseDao {

    /**
     * Creates a new warehouse in the database.
     * @return true if successful, false otherwise.
     */
    public boolean createWarehouse(String name, String location) {
        String sql = "INSERT INTO warehouses (name, location) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all warehouses from the database.
     * @return A list of all Warehouse objects.
     */
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM warehouses";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                warehouses.add(new Warehouse(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouses;
    }
}
