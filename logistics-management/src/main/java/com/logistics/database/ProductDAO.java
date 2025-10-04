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
            pstmt.setString(2, product.getDescription
