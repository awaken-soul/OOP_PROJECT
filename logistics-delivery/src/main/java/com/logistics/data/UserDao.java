package com.logistics.data;

import com.logistics.model.Admin;
import com.logistics.model.Agent;
import com.logistics.model.Customer;
import com.logistics.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        // Get the connection outside the try-with-resources
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String userEmail = rs.getString("email");
                    String userPassword = rs.getString("password");
                    String role = rs.getString("role");

                    switch (role) {
                        case "CUSTOMER": return new Customer(id, userEmail, userPassword);
                        case "AGENT": return new Agent(id, userEmail, userPassword);
                        case "ADMIN": return new Admin(id, userEmail, userPassword);
                        default: return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(String email, String password, String role) {
        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        Connection conn = Database.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                System.err.println("Error: User with email " + email + " already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}

