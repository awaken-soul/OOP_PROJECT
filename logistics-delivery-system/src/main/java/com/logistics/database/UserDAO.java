package com.logistics.database;

import com.logistics.models.*; 
// PasswordHasher is no longer imported as plain passwords are used

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations (CRUD) for the User table, and includes 
 * methods for injecting initial test data.
 */
public class UserDAO {
    
    // --- Test Data Constant (Plain Password for all test users) ---
    private static final String TEST_PASSWORD = "password"; 

    // --- Database Initialization Method (Called once by MainApplication) ---
    
    /**
     * Inserts test data for all four roles if the User table is currently empty.
     */
    public void initializeTestUsers() {
        if (getUserCount() > 0) {
            System.out.println("UserDAO: Test users already exist.");
            return;
        }

        System.out.println("UserDAO: Inserting default test users (password='password')...");
        
        // 1. Admin
        registerTestUser(100, "Admin Root", "admin@logistics.com", TEST_PASSWORD, "Admin", "9990001111", "HQ - City Center");
        
        // 2. Customer
        registerTestUser(101, "Alice Customer", "alice@customer.com", TEST_PASSWORD, "Customer", "9876543210", "123 Main St");
        
        // 3. Agent
        registerTestUser(102, "Bob Agent", "bob@agent.com", TEST_PASSWORD, "Agent", "7775553333", "456 North St");
        
        // 4. Warehouse Manager
        registerTestUser(103, "Charlie Manager", "charlie@wh.com", TEST_PASSWORD, "Manager", "6664442222", "Warehouse 1 Depot");
        
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
    
    // Simplified registration helper for initialization (uses user_id and plain password directly)
    private boolean registerTestUser(int id, String name, String email, String password, String role, String contact, String address) {
        String sql = "INSERT INTO User (user_id, name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password); // Storing plain text password
            pstmt.setString(5, role);
            pstmt.setString(6, contact);
            pstmt.setString(7, address);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DB Error inserting test user: " + e.getMessage());
            return false;
        }
    }

    // --- Main DAO Methods ---

    /**
     * Maps a ResultSet row to the appropriate concrete User subclass object.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int userID = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password"); // Retrieving plain password
        String role = rs.getString("role");
        String contactNumber = rs.getString("contact_number");
        String address = rs.getString("address");

        return switch (role) {
            // Note: Constructors updated to use plain 'password'
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
        // Storing plainPassword directly.
        String sql = "INSERT INTO User (name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, plainPassword); // Storing PLAIN PASSWORD
            pstmt.setString(4, user.getRole());
            // FIX: Uses public getters for address and contact number
            pstmt.setString(5, user.getContactNumber()); 
            pstmt.setString(6, user.getAddress());

            int rowsAffected = pstmt.executeUpdate();
            
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
    
    /**
     * Retrieves a list of all users whose role is 'Agent' for assignment purposes.
     */
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
