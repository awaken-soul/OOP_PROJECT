package com.logistics.database;

import com.logistics.models.Role;
import com.logistics.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements Dao<User> {

    private final Connection connection;

    public UserDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding user by id.", e);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding user by email.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all users.", e);
        }
        return users;
    }

    @Override
    public Optional<Integer> save(User user) {
        String sql = "INSERT INTO users(name, email, password, role, contact_number, address) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name());
            pstmt.setString(5, user.getContactNumber());
            pstmt.setString(6, user.getAddress());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return Optional.of(generatedKeys.getInt(1));
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException("Error saving user to the database.", e);
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ?, contact_number = ?, address = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name());
            pstmt.setString(5, user.getContactNumber());
            pstmt.setString(6, user.getAddress());
            pstmt.setInt(7, user.getUserId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user.", e);
        }
    }

    @Override
    public boolean delete(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user.getUserId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user.", e);
        }
    }

    public List<User> findAvailableAgents() {
        List<User> agents = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'AGENT'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                agents.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding available agents.", e);
        }
        return agents;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role")),
                rs.getString("contact_number"),
                rs.getString("address")
        );
    }
}
