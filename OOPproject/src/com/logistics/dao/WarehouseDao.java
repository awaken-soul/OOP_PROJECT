package com.logistics.dao;

import com.logistics.model.Warehouse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDao {

    /** Create a new warehouse */
    public boolean createWarehouse(String name, String address, int capacity, int managerId) {
        String sql = "INSERT INTO Warehouse (name, address, capacity, manager_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setInt(3, capacity);
            stmt.setInt(4, managerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating warehouse: " + e.getMessage());
            return false;
        }
    }

    /** Get all warehouses */
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM Warehouse";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Warehouse w = new Warehouse();
                w.setWarehouseId(rs.getInt("warehouse_id"));
                w.setName(rs.getString("name"));
                w.setAddress(rs.getString("address"));
                w.setCapacity(rs.getInt("capacity"));
                w.setManagerId(rs.getInt("manager_id"));
                list.add(w);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching warehouses: " + e.getMessage());
        }
        return list;
    }

    /** Get warehouses managed by a specific manager */
    public List<Warehouse> getWarehousesByManager(int managerId) {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM Warehouse WHERE manager_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Warehouse w = new Warehouse();
                w.setWarehouseId(rs.getInt("warehouse_id"));
                w.setName(rs.getString("name"));
                w.setAddress(rs.getString("address"));
                w.setCapacity(rs.getInt("capacity"));
                w.setManagerId(rs.getInt("manager_id"));
                warehouses.add(w);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching warehouses for manager: " + e.getMessage());
        }
        return warehouses;
    }
    
    public Warehouse getWarehouseById(int warehouseId) {
        Warehouse warehouse = null;
        String sql = "SELECT * FROM Warehouse WHERE warehouse_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, warehouseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                warehouse = new Warehouse();
                warehouse.setWarehouseId(rs.getInt("warehouse_id"));
                warehouse.setName(rs.getString("name"));
                warehouse.setAddress(rs.getString("address"));
                warehouse.setCapacity(rs.getInt("capacity"));
                warehouse.setManagerId(rs.getInt("manager_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching warehouse by ID: " + e.getMessage());
        }

        return warehouse;
    }
    
}

/*
package com.logistics.dao;

import com.logistics.model.Warehouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDao {


    public boolean createWarehouse(String name, String address, int capacity, int managerId) {
        String sql = "INSERT INTO Warehouse (name, address, capacity, manager_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setInt(3, capacity);
            stmt.setInt(4, managerId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating warehouse: " + e.getMessage());
            return false;
        }
    }


    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM Warehouse";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Warehouse w = new Warehouse();
                w.setWarehouseId(rs.getInt("warehouse_id"));
                w.setName(rs.getString("name"));
                w.setAddress(rs.getString("address"));
                w.setCapacity(rs.getInt("capacity"));
                w.setManagerId(rs.getInt("manager_id"));
                list.add(w);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching warehouses: " + e.getMessage());
        }
        return list;
    }

    public Warehouse getWarehouseById(int warehouseId) {
        Warehouse warehouse = null;
        String sql = "SELECT * FROM Warehouse WHERE warehouse_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, warehouseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                warehouse = new Warehouse();
                warehouse.setWarehouseId(rs.getInt("warehouse_id"));
                warehouse.setName(rs.getString("name"));
                warehouse.setAddress(rs.getString("address"));
                warehouse.setCapacity(rs.getInt("capacity"));
                warehouse.setManagerId(rs.getInt("manager_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching warehouse by ID: " + e.getMessage());
        }

        return warehouse;
    }
}

*/