package com.logistics.database;

import com.logistics.models.*; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String TEST_PASSWORD = "password"; 
    
    public void initializeTestUsers() {
        if (getUserCount() > 0) {
            System.out.println("UserDAO: Test users already exist.");
            return;
        }

        System.out.println("UserDAO: Inserting default test users (password='password')...");
        
        registerTestUser(100, "Admin Root", "admin@logistics.com", TEST_PASSWORD, "Admin", "9990001111", "HQ - City Center");
        
        registerTestUser(101, "Alice Customer", "alice@customer.com", TEST_PASSWORD, "Customer", "9876543210", "123 Main St");
        
        registerTestUser(102, "Bob Agent", "bob@agent.com", TEST_PASSWORD, "Agent", "7775553333", "456 North St");
   
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
    
    private boolean registerTestUser(int id, String name, String email, String password, String role, String contact, String address) {
        String sql = "INSERT INTO User (user_id, name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, role);
            pstmt.setString(6, contact);
            pstmt.setString(7, address);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DB Error inserting test user: " + e.getMessage());
            return false;
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int userID = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");
        String contactNumber = rs.getString("contact_number");
        String address = rs.getString("address");

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

    public boolean registerNewUser(User user, String plainPassword) {
        String sql = "INSERT INTO User (name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, plainPassword);
            pstmt.setString(4, user.getRole());
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

    public boolean updateValidationStatus(int userId, boolean approved) {
        String sql = "UPDATE User SET is_validated = ? WHERE user_id = ?"; 
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, approved ? 1 : 0);
            pstmt.setInt(2, userId);
    
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DB Error validating user ID " + userId + ": " + e.getMessage());
            return false;
        }
    }
}
