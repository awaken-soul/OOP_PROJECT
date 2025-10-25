package com.logistics.dao;

import com.logistics.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean createUser(String name, String email, String password, String role,
                              String contact, String address) {
        String sql = "INSERT INTO User (name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.setString(5, contact);
            stmt.setString(6, address);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM User WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setContactNumber(rs.getString("contact_number"));
                    user.setAddress(rs.getString("address"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User WHERE role = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setContactNumber(rs.getString("contact_number"));
                    user.setAddress(rs.getString("address"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users by role: " + e.getMessage());
        }
        return users;
    }
}


/*package com.logistics.dao;

import com.logistics.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Add a new user
    public boolean addUser(User user) {
        String sql = "INSERT INTO \"User\" (name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getContactNumber());
            stmt.setString(6, user.getAddress());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    // Find user by email & password
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM \"User\" WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    // Get all users of a specific role (used by AdminDashboard)
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM \"User\" WHERE LOWER(role) = LOWER(?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUser(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting users by role: " + e.getMessage());
        }
        return users;
    }

    // Helper to build User object
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setContactNumber(rs.getString("contact_number"));
        user.setAddress(rs.getString("address"));
        return user;
    }
}
*/