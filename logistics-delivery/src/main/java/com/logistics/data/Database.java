package com.logistics.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Manages the connection to the H2 database.
 * This class uses the Singleton pattern to ensure only one database connection is created.
 * It also handles the initial setup of database tables.
 */
public class Database {
    // The path to the database file. It will be created in the project's root directory.
    private static final String DB_URL = "jdbc:h2:./logistics";
    private static Connection connection;

    /**
     * Gets the single, shared connection to the database.
     * If the connection doesn't exist, it creates it and initializes the database tables.
     *
     * @return The active database connection.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Establish the connection to the H2 database file.
                connection = DriverManager.getConnection(DB_URL, "sa", ""); // "sa" and "" are default user/pass for H2
                initializeDb();
            } catch (SQLException e) {
                // Print a more user-friendly error message if the connection fails.
                System.err.println("Database connection failed. Please ensure the H2 driver is in the classpath.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Creates all the necessary tables if they don't already exist.
     * This makes the application self-contained and easy to set up.
     * It also seeds the database with a default admin user.
     */
    private static void initializeDb() {
        try (Statement stmt = connection.createStatement()) {
            // User Table: Stores all users, identified by their role.
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "email VARCHAR(255) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(50) NOT NULL)");

            // Warehouse Table: Stores physical warehouse locations.
            stmt.execute("CREATE TABLE IF NOT EXISTS warehouses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "location VARCHAR(255) NOT NULL)");
            
            // Product Table: Represents the item being shipped.
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "description VARCHAR(255))");

            // Shipment Table: The central table linking customers, products, agents, and warehouses.
            stmt.execute("CREATE TABLE IF NOT EXISTS shipments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "customerId INT NOT NULL," +
                    "productId INT NOT NULL," +
                    "source VARCHAR(255) NOT NULL," +
                    "destination VARCHAR(255) NOT NULL," +
                    "status VARCHAR(100) NOT NULL," +
                    "agentId INT," + // Can be NULL if not yet assigned
                    "warehouseId INT," + // Can be NULL if not in a warehouse
                    "FOREIGN KEY (customerId) REFERENCES users(id)," +
                    "FOREIGN KEY (productId) REFERENCES products(id)," +
                    "FOREIGN KEY (agentId) REFERENCES users(id)," +
                    "FOREIGN KEY (warehouseId) REFERENCES warehouses(id))");

            // Seed the database with a default admin user if no users exist.
            // This ensures the admin can log in on the first run.
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM users");
            if (rs.next() && rs.getInt("count") == 0) {
                 stmt.execute("INSERT INTO users (email, password, role) VALUES ('admin@logistics.com', 'adminpass', 'ADMIN')");
                 System.out.println("Default admin user created: admin@logistics.com / adminpass");
            }

        } catch (SQLException e) {
            System.err.println("Error initializing database tables.");
            e.printStackTrace();
        }
    }
}

