package com.logistics.database;

import com.logistics.models.*; 
import com.logistics.utils.PasswordHasher; 

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations (CRUD) for the User table.
 */
public class UserDAO {
    
    // --- Test Data Constants ---
    // Plain Password: 'password'
    // SHA-256 Hash for 'password': 5e884898da28047151d0e56f8dc6292773603d0d6a0ab91af8117799d62886f6
    private static final String TEST_PASSWORD_HASH = "5e884898da28047151d0e56f8dc6292773603d0d6a0ab91af8117799d62886f6";

    // --- Database Initialization Method ---
    
    /**
     * Inserts test data for all four roles if the User table is currently empty.
     */
    public void initializeTestUsers() {
        if (getUserCount() > 0) {
            System.out.println("UserDAO: Test users already exist.");
            return;
        }

        System.out.println("UserDAO: Inserting default test users (password='password')...");
        
        // Use the registration logic to insert users
        // NOTE: We pass the HASH directly since registerNewUser() expects plain text, but we override that here 
        // to simplify the initialization. This is a common pattern for seed data.
        
        // 1. Admin
        registerUserWithHash(100, "Admin Root", "admin@logistics.com", TEST_PASSWORD_HASH, "Admin", "9990001111", "HQ - City Center");
        
        // 2. Customer
        registerUserWithHash(101, "Alice Customer", "alice@customer.com", TEST_PASSWORD_HASH, "Customer", "9876543210", "123 Main St");
        
        // 3. Agent (Essential for Order Assignment)
        registerUserWithHash(102, "Bob Agent", "bob@agent.com", TEST_PASSWORD_HASH, "Agent", "7775553333", "456 North St");
        
        // 4. Warehouse Manager
        registerUserWithHash(103, "Charlie Manager", "charlie@wh.com", TEST_PASSWORD_HASH, "Manager", "6664442222", "Warehouse 1 Depot");
        
        System.out.println("UserDAO: Test data injection complete.");
    }

    private int getUserCount() {
        String sql = "SELECT COUNT(*) FROM User";
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("DB Error counting users: " + e.getMessage());
        }
        return 0;
    }
    
    // Simplified registration helper for initialization using pre-calculated hash
    private boolean registerUserWithHash(int id, String name, String email, String hash, String role, String contact, String address) {
        String sql = "INSERT INTO User (user_id, name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, hash);
            pstmt.setString(5, role);
            pstmt.setString(6, contact);
            pstmt.setString(7, address);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DB Error inserting test user: " + e.getMessage());
            return false;
        }
    }


    // --- Existing Methods (mapResultSetToUser, registerNewUser, etc.) ---

    // NOTE: All existing UserDAO methods (mapResultSetToUser, registerNewUser, getUserByEmail, getAvailableAgents) 
    // must remain below the initialization logic.
    
    // ... (All existing methods go here)
    // ... 
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int userID = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String passwordHash = rs.getString("password");
        String role = rs.getString("role");
        String contactNumber = rs.getString("contact_number");
        String address = rs.getString("address");

        return switch (role) {
            case "Customer" -> new Customer(userID, name, email, passwordHash, contactNumber, address);
            case "Admin" -> new Admin(userID, name, email, passwordHash, contactNumber, address);
            case "Agent" -> new DeliveryAgent(userID, name, email, passwordHash, contactNumber, address);
            case "Manager" -> new WarehouseManager(userID, name, email, passwordHash, contactNumber, address);
            default -> {
                System.err.println("Unknown role '" + role + "' found in database for user ID: " + userID);
                yield null;
            }
        };
    }

    public boolean registerNewUser(User user, String plainPassword) {
        String hashedPassword = PasswordHasher.hashPassword(plainPassword);
        if (hashedPassword == null) return false;

        String sql = "INSERT INTO User (name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getContactNumber());
            pstmt.setString(6, user.getAddress());

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newID = generatedKeys.getInt(1);
                        user.setUserID(newID);
                        System.out.println("New user registered successfully with ID: " + newID);
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
