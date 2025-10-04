package com.logistics.database;

import com.logistics.models.Retailer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RetailerDAO implements Dao<Retailer> {

    private final Connection connection;

    public RetailerDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    @Override
    public Optional<Retailer> findById(int id) {
        String sql = "SELECT * FROM retailer WHERE retailer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToRetailer(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding retailer by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Retailer> findAll() {
        List<Retailer> retailers = new ArrayList<>();
        String sql = "SELECT * FROM retailer";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                retailers.add(mapRowToRetailer(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all retailers.", e);
        }
        return retailers;
    }

    @Override
    public Optional<Integer> save(Retailer retailer) {
        String sql = "INSERT INTO retailer(name, address, contact_number) VALUES(?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, retailer.getName());
            pstmt.setString(2, retailer.getAddress());
            pstmt.setString(3, retailer.getContactNumber());

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
            throw new DataAccessException("Error saving retailer.", e);
        }
    }

    @Override
    public boolean update(Retailer retailer) {
        String sql = "UPDATE retailer SET name = ?, address = ?, contact_number = ? WHERE retailer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, retailer.getName());
            pstmt.setString(2, retailer.getAddress());
            pstmt.setString(3, retailer.getContactNumber());
            pstmt.setInt(4, retailer.getRetailerId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating retailer.", e);
        }
    }

    @Override
    public boolean delete(Retailer retailer) {
        String sql = "DELETE FROM retailer WHERE retailer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, retailer.getRetailerId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting retailer.", e);
        }
    }

    private Retailer mapRowToRetailer(ResultSet rs) throws SQLException {
        return new Retailer(
                rs.getInt("retailer_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("contact_number")
        );
    }
}
