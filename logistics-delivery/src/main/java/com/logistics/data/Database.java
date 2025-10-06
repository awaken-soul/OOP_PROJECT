package com.logistics.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the connection to the SQLite database.
 * Creates the database file and tables if they don't exist.
 */
public class Database {

    // The URL points to a file in the standard Maven resources folder.
    // The driver will create this file automatically if it doesn't exist.
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/logistics.db";

    // A single, static connection that is shared throughout the application's lifecycle.
    private static Connection connection;

    /**
     * Gets the single instance of the database connection.
     * If the connection does not exist, it is created.
     *
     * @return The database connection.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Establish the connection. The file is created here if it's missing.
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Database connection established to " + DB_URL);
                initializeDb();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Closes the database connection.
     * This should be called when the application is shutting down.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the database schema (tables) if they do not already exist.
     * Uses SQLite-compatible syntax.
     */
    private static void initializeDb() {
        try (Statement stmt = connection.createStatement()) {
            // User Table: Stores all users (customers, agents, admins)
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)");

            // Warehouse Table
            stmt.execute("CREATE TABLE IF NOT EXISTS warehouses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "location TEXT NOT NULL)");

            // Product Table
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT)");

            // Shipment Table
            stmt.execute("CREATE TABLE IF NOT EXISTS shipments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customerId INTEGER NOT NULL," +
                    "productId INTEGER NOT NULL," +
                    "source TEXT NOT NULL," +
                    "destination TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "agentId INTEGER," +
                    "warehouseId INTEGER," +
                    "FOREIGN KEY (customerId) REFERENCES users(id)," +
                    "FOREIGN KEY (productId) REFERENCES products(id)," +
                    "FOREIGN KEY (agentId) REFERENCES users(id)," +
                    "FOREIGN KEY (warehouseId) REFERENCES warehouses(id))");

            // Insert a default admin user if one doesn't already exist.
            // INSERT OR IGNORE is the SQLite way to handle this.
            stmt.execute("INSERT OR IGNORE INTO users (email, password, role) VALUES ('admin@logistics.com', 'adminpass', 'ADMIN')");

            System.out.println("Database schema initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database schema.");
            e.printStackTrace();
        }
    }
}

