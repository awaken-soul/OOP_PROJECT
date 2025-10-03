package com.logistics.database;

import com.logistics.models.*; 
// import com.logistics.utils.PasswordHasher; // REMOVED

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations (CRUD) for the User table.
 */
public class UserDAO {

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int userID = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password"); // Retrieving plain password
        String role = rs.getString("role");
        String contactNumber = rs.getString("contact_number");
        String address = rs.getString("address");

        // NOTE: Constructor arguments changed from (..., passwordHash, ...) to (..., password, ...)
        return switch (role) {
            case "Customer" -> new Customer(userID, name, email, password, contactNumber, address);
            case "Admin" -> new Admin(userID, name, email, password, contactNumber, address);
            case "Agent" -> new DeliveryAgent(userID, name, email, password, contactNumber, address);
            case "Manager" -> new WarehouseManager(userID, name, email, password, contactNumber, address);
            default -> {
                System.err.println("Unknown role '" + role + "' found in database for user ID: " + userID);
                yield null;
            }
        };
    }

    /**
     * Saves a new User object using the plain password.
     */
    public boolean registerNewUser(User user, String plainPassword) {
        // HASHING IS SKIPPED. Storing plainPassword directly.
        String sql = "INSERT INTO User (name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, plainPassword); // Storing PLAIN PASSWORD
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getContactNumber());
            pstmt.setString(6, user.getAddress());

            int rowsAffected = pstmt.executeUpdate();
            
            // ... (rest of registration logic remains the same)
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserID(generatedKeys.getInt(1));
                        System.out.println("New user registered successfully with ID: " + user.getUserID());
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")) {
                 System.err.println("Registration failed: Email already in use.");
            } else {
                System.err.println("Database error during registration: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Retrieves a User record by email for authentication purposes.
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during user retrieval: " + e.getMessage());
        }
        return null;
    }
    
    // ... (getAvailableAgents() and other helper methods remain the same)
    public List<DeliveryAgent> getAvailableAgents() {
        List<DeliveryAgent> agents = new ArrayList<>();
        String sql = "SELECT * FROM User WHERE role = 'Agent'"; 

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = mapResultSetToUser(rs); 
                if (user instanceof DeliveryAgent) {
                    agents.add((DeliveryAgent) user);
                }
            }
        } catch (SQLException e) {
            System.err.println("DB Error retrieving available agents: " + e.getMessage());
        }
        return agents;
    }
}
