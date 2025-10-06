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
     * This version uses the SQLite-specific last_insert_rowid() function.
     *
     * @param name The name of the product.
     * @param description The description of the product.
     * @return The auto-generated ID of the newly created product, or -1 if the creation failed.
     */
    public int createProductAndGetId(String name, String description) {
        String insertSql = "INSERT INTO products (name, description) VALUES (?, ?)";
        int productId = -1;
        Connection conn = Database.getConnection();

        try {
            // Start a transaction to ensure atomicity
            conn.setAutoCommit(false);

            // 1. Execute the INSERT statement
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, name);
                stmt.setString(2, description);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating product failed, no rows affected.");
                }
            }

            // 2. Get the ID of the last inserted row using SQLite's function
            try (Statement idStmt = conn.createStatement();
                 ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    productId = rs.getInt(1);
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }

            // If both operations were successful, commit the transaction
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Error creating product in database; rolling back transaction.");
            e.printStackTrace();
            // If any error occurs, roll back the transaction
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Always return the connection to its normal auto-commit state
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return productId;
    }
}

