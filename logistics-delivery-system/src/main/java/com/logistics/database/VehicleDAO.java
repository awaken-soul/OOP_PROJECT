package com.logistics.database;

import com.logistics.models.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
            rs.getInt("vehicle_id"),
            rs.getString("vehicle_type"),
            rs.getString("license_plate"),
            rs.getString("status"),
            rs.getObject("driver_id") != null ? rs.getInt("driver_id") : null,
            rs.getString("current_location")
        );
    }

    public boolean saveNewVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO Vehicle (vehicle_type, license_plate, status, driver_id, current_location) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, vehicle.getVehicleType());
            pstmt.setString(2, vehicle.getLicensePlate());
            pstmt.setString(3, vehicle.getStatus());
            
            if (vehicle.getDriverID() == null) {
                pstmt.setNull(4, Types.INTEGER);
            } else {
                pstmt.setInt(4, vehicle.getDriverID());
            }
            
            pstmt.setString(5, vehicle.getCurrentLocation());

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vehicle.setVehicleID(generatedKeys.getInt(1));
                        System.out.println("New vehicle added with ID: " + vehicle.getVehicleID());
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")) {
                 System.err.println("Vehicle addition failed: License plate already exists.");
            } else {
                System.err.println("Database error during vehicle registration: " + e.getMessage());
            }
            return false;
        }
    }
  
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM Vehicle WHERE status = 'Available'";
        
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving available vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    public boolean updateVehicleStatus(int vehicleId, String newStatus, String newLocation) {
        String sql = "UPDATE Vehicle SET status = ?, current_location = ? WHERE vehicle_id = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, newLocation);
            pstmt.setInt(3, vehicleId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error updating vehicle status: " + e.getMessage());
            return false;
        }
    }

    public Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT * FROM Vehicle WHERE vehicle_id = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicle(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving vehicle: " + e.getMessage());
        }
        return null;
    }
    public void initializeTestVehicles() {
        // We assume the vehicle table is empty or we insert regardless for testing
        String sql = "INSERT INTO Vehicle (vehicle_id, vehicle_type, license_plate, status, driver_id, current_location) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Vehicle 1: Linked to Bob (ID 102), Status: Available
            pstmt.setInt(1, 1);
            pstmt.setString(2, "Truck");
            pstmt.setString(3, "TL-001");
            pstmt.setString(4, "Available");
            pstmt.setInt(5, 102); // Bob's Agent ID
            pstmt.setString(6, "Warehouse 1 Depot");
            pstmt.executeUpdate();
    
            System.out.println("VehicleDAO: Test vehicle TL-001 injected successfully.");
            
        } catch (SQLException e) {
            // Ignore unique constraint error if run twice
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("DB Error inserting test vehicle: " + e.getMessage());
            }
        }
    }
}
