package com.logistics.database;

import com.logistics.models.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleDAO implements Dao<Vehicle> {

    private final Connection connection;

    public VehicleDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding vehicle by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all vehicles.", e);
        }
        return vehicles;
    }

    public List<Vehicle> findAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE status = 'Available'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(mapRowToVehicle(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding available vehicles.", e);
        }
        return vehicles;
    }

    @Override
    public boolean save(Vehicle vehicle) {
        // Placeholder for future implementation
        return false;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        // Placeholder for future implementation
        return false;
    }

    @Override
    public boolean delete(Vehicle vehicle) {
        // Placeholder for future implementation
        return false;
    }

    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("vehicle_id"),
                rs.getString("vehicle_type"),
                rs.getString("license_plate"),
                rs.getString("status"),
                rs.getInt("driver_id"),
                rs.getString("current_location")
        );
    }
}
