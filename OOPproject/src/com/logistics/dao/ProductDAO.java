package com.logistics.dao;
import com.logistics.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Fetch all products from DB
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, name, description, price, quantity FROM Product";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Fetch a single product quantity
    public int getAvailableQuantity(int productId) {
        String sql = "SELECT quantity FROM Product WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Update product stock after placing order
    public boolean updateQuantity(int productId, int qty) {
        String sql = "UPDATE Product SET quantity = quantity - ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // Transactional order placement with payment
    public boolean placeOrderWithPayment(int userId, int productId, int qty, String orderType,
                                         String sourceAddress, String destinationAddress) {
        String insertOrderSQL = "INSERT INTO Orders (user_id, product_id, order_type, source_address, destination_address, status, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertPaymentSQL = "INSERT INTO Payment (order_id, amount, method, status) VALUES (?, ?, ?, ?)";
        String updateQtySQL = "UPDATE Product SET quantity = quantity - ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int orderId = -1;

            // Insert into Orders
            try (PreparedStatement ps = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.setString(3, orderType);
                ps.setString(4, sourceAddress);
                ps.setString(5, destinationAddress);
                ps.setString(6, "Pending");
                ps.setString(7, "Pending");
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    orderId = keys.getInt(1);
                }
            }

            // Update product stock
            try (PreparedStatement ps = conn.prepareStatement(updateQtySQL)) {
                ps.setInt(1, qty);
                ps.setInt(2, productId);
                ps.executeUpdate();
            }

            // Fetch product price
            double price = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT price FROM Product WHERE product_id = ?")) {
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    price = rs.getDouble("price");
                }
            }

            double totalAmount = price * qty;

            // Insert into Payment
            try (PreparedStatement ps = conn.prepareStatement(insertPaymentSQL)) {
                ps.setInt(1, orderId);
                ps.setDouble(2, totalAmount);
                ps.setString(3, "UPI");
                ps.setString(4, "Pending");
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
