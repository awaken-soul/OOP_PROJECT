package com.logistics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles all database operations related to the Product entity.
 */
public class ProductDao {

    /**
     * Creates a new product in the database and returns its auto-generated ID.
     * This is a critical step in creating a new shipment.
     *
     * @param name The name of the product.
     * @param description The description of the product.
     * @return The auto-generated ID of the newly created product, or -1 if the creation failed.
     */
    public int createProductAndGetId(String name, String description) {
        // SQL query to insert a new product. The Statement.RETURN_GENERATED_KEYS flag
        // tells the JDBC driver to make the generated keys available after the insert.
        String sql = "INSERT INTO products (name, description) VALUES (?, ?)";
        int productId = -1; // Default to -1 to indicate failure

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            
            int affectedRows = stmt.executeUpdate();

            // Check if the insert was successful
            if (affectedRows > 0) {
                // Retrieve the auto-generated keys (in this case, just the product ID)
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating product failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating product in database.");
            e.printStackTrace();
        }
        
        return productId;
    }
}

