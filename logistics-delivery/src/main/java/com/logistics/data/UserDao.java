package com.logistics.data;

import com.logistics.model.Admin;
import com.logistics.model.Agent;
import com.logistics.model.Customer;
import com.logistics.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Handles all database operations for the User entity.
 * This includes finding users for login and creating new users.
 */
public class UserDao {

    /**
     * Finds a user by their email and password for authentication.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return a User object (Admin, Agent, or Customer) if credentials are correct, otherwise null.
     */
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String userEmail = rs.getString("email");
                    String userPassword = rs.getString("password");
                    String role = rs.getString("role");

                    // Based on the role from the database, create the specific user type.
                    switch (role) {
                        case "CUSTOMER":
                            return new Customer(id, userEmail, userPassword);
                        case "AGENT":
                            return new Agent(id, userEmail, userPassword);
                        case "ADMIN":
                            return new Admin(id, userEmail, userPassword);
                        default:
                            return null; // Should not happen if data is consistent
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during user authentication.");
            e.printStackTrace();
        }
        return null; // Return null if no user is found or an error occurs
    }

    /**
     * Creates a new user in the database.
     *
     * @param email    The email for the new user (must be unique).
     * @param password The password for the new user.
     * @param role     The role of the new user (e.g., "CUSTOMER", "AGENT").
     * @return true if the user was created successfully, false otherwise.
     */
    public boolean createUser(String email, String password, String role) {
        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            // This specific exception is caught when the "UNIQUE" constraint on email fails.
            System.err.println("Error: User with email " + email + " already exists.");
            return false;
        } catch (SQLException e) {
            System.err.println("Error creating user.");
            e.printStackTrace();
            return false;
        }
    }
}

