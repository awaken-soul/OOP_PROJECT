package com.logistics.database;

import com.logistics.models.Warehouse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarehouseDAO implements Dao<Warehouse> {

    private final Connection connection;

    public WarehouseDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    @Override
    public Optional<Warehouse> findById(int id) {
        String sql = "SELECT * FROM warehouse WHERE warehouse_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToWarehouse(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding warehouse by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Warehouse> findAll() {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM warehouse";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                warehouses.add(mapRowToWarehouse(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all warehouses.", e);
        }
        return warehouses;
    }

    @Override
    public Optional<Integer> save(Warehouse warehouse) {
        String sql = "INSERT INTO warehouse(name, address, capacity, manager_id) VALUES(?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, warehouse.getName());
            pstmt.setString(2, warehouse.getAddress());
            pstmt.setInt(3, warehouse.getCapacity());
            pstmt.setInt(4, warehouse.getManagerId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) return Optional.empty();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(generatedKeys.getInt(1));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error saving warehouse.", e);
        }
    }

    @Override
    public boolean update(Warehouse warehouse) {
        String sql = "UPDATE warehouse SET name = ?, address = ?, capacity = ?, manager_id = ? WHERE warehouse_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, warehouse.getName());
            pstmt.setString(2, warehouse.getAddress());
            pstmt.setInt(3, warehouse.getCapacity());
            pstmt.setInt(4, warehouse.getManagerId());
            pstmt.setInt(5, warehouse.getWarehouseId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating warehouse.", e);
        }
    }

    @Override
    public boolean delete(Warehouse warehouse) {
        String sql = "DELETE FROM warehouse WHERE warehouse_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, warehouse.getWarehouseId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting warehouse.", e);
        }
    }

    private Warehouse mapRowToWarehouse(ResultSet rs) throws SQLException {
        return new Warehouse(
                rs.getInt("warehouse_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getInt("capacity"),
                rs.getInt("manager_id")
        );
    }
}
