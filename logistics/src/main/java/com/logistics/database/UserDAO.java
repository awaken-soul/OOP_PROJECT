]package com.logistics.database;

import com.logistics.models.*; // Import all user models (User, Customer, Admin, etc.)
import com.logistics.utils.PasswordHasher; // Used for hashing/verification

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database operations (CRUD) for the User table.
 */
public class UserDAO {

    /**
     * Maps a ResultSet row to the appropriate concrete User subclass object.
     * @param rs The ResultSet containing the user data.
     * @return A concrete User object (Customer, Admin, etc.) or null if role is unknown.
     * @throws SQLException if a database access error occurs.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int userID = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String passwordHash = rs.getString("password");
        String role = rs.getString("role");
        String contactNumber = rs.getString("contact_number");
        String address = rs.getString("address");

        // Factory-like pattern to return the correct object type
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

    /**
     * Saves a new User object (must have role and plain password set for hashing) to the database.
     * @param user The User object to save (the ID will be updated upon success).
     * @param plainPassword The plain text password entered by the user.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean registerNewUser(User user, String plainPassword) {
        // Hash the password before insertion
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
                // Retrieve the auto-generated user_id
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newID = generatedKeys.getInt(1);
                        user.setUserID(newID); // Update the model object with the new ID
                        System.out.println("New user registered successfully with ID: " + newID);
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            // Handle unique constraint violation (e.g., email already exists)
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
     * @param email The login email.
     * @return A User object containing all data, or null if the user is not found.
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Map the found record to a concrete User object
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during user retrieval: " + e.getMessage());
        }
        return null; // User not found or error occurred
    }
    
    /**
     * Retrieves a list of all users whose role is 'Agent' for assignment purposes.
     * This method is required by OrderService.
     * @return A List of DeliveryAgent objects.
     */
    public List<DeliveryAgent> getAvailableAgents() {
        List<DeliveryAgent> agents = new ArrayList<>();
        String sql = "SELECT * FROM User WHERE role = 'Agent'"; 

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Use the existing mapper and cast to the specific model
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
