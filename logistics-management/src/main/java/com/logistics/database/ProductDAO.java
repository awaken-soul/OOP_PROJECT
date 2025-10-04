package com.logistics.database;

import com.logistics.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO implements Dao<Product> {

    private final Connection connection;

    public ProductDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    @Override
    public Optional<Product> findById(int id) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding product by id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all products.", e);
        }
        return products;
    }

    @Override
    public Optional<Integer> save(Product product) {
        String sql = "INSERT INTO product(name, description, price, quantity, warehouse_id, retailer_id) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());
            pstmt.setInt(5, product.getWarehouseId());
            pstmt.setInt(6, product.getRetailerId());

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
            throw new DataAccessException("Error saving product.", e);
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, quantity = ?, warehouse_id = ?, retailer_id = ? WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());
            pstmt.setInt(5, product.getWarehouseId());
            pstmt.setInt(6, product.getRetailerId());
            pstmt.setInt(7, product.getProductId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating product.", e);
        }
    }

    public boolean updateQuantity(int productId, int newQuantity) {
        String sql = "UPDATE product SET quantity = ? WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating product quantity.", e);
        }
    }

    @Override
    public boolean delete(Product product) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, product.getProductId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting product.", e);
        }
    }

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getInt("warehouse_id"),
                rs.getInt("retailer_id")
        );
    }
}
