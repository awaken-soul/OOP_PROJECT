package com.logistics.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/logistics.db";
    private static DBConnector instance;
    private Connection connection;

    private DBConnector() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
            initializeSchema();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    public static synchronized DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeSchema() {
        try (Statement stmt = connection.createStatement()) {
            // User Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL CHECK(role IN ('CUSTOMER', 'ADMIN', 'AGENT', 'MANAGER')), " +
                    "contact_number TEXT, " +
                    "address TEXT)");

            // Warehouse Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS warehouse (" +
                    "warehouse_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT, " +
                    "capacity INTEGER, " +
                    "manager_id INTEGER, " +
                    "FOREIGN KEY (manager_id) REFERENCES users(user_id))");

            // Retailer Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS retailer (" +
                    "retailer_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT, " +
                    "contact_number TEXT)");

            // Product Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS product (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "price REAL, " +
                    "quantity INTEGER, " +
                    "warehouse_id INTEGER, " +
                    "retailer_id INTEGER, " +
                    "FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id), " +
                    "FOREIGN KEY (retailer_id) REFERENCES retailer(retailer_id))");

            // Vehicle Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS vehicles (" +
                    "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "vehicle_type TEXT NOT NULL, " +
                    "license_plate TEXT UNIQUE NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "driver_id INTEGER, " +
                    "current_location TEXT, " +
                    "FOREIGN KEY (driver_id) REFERENCES users(user_id))");

            // Orders Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "product_id INTEGER, " +
                    "order_type TEXT NOT NULL, " +
                    "source_address TEXT NOT NULL, " +
                    "destination_address TEXT NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "assigned_agent_id INTEGER, " +
                    "vehicle_id INTEGER, " +
                    "payment_status TEXT NOT NULL, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                    "FOREIGN KEY (product_id) REFERENCES product(product_id), " +
                    "FOREIGN KEY (assigned_agent_id) REFERENCES users(user_id), " +
                    "FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id))");

            // Payment Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS payment (" +
                    "payment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "order_id INTEGER NOT NULL, " +
                    "amount REAL, " +
                    "method TEXT, " +
                    "status TEXT, " +
                    "payment_date DATETIME, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(order_id))");

            // Tracking Table [1]
            stmt.execute("CREATE TABLE IF NOT EXISTS tracking (" +
                    "tracking_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "order_id INTEGER NOT NULL, " +
                    "agent_id INTEGER, " +
                    "current_status TEXT, " +
                    "location TEXT, " +
                    "updated_at DATETIME, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(order_id), " +
                    "FOREIGN KEY (agent_id) REFERENCES users(user_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS complaints (" +
                    "complaint_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "order_id INTEGER, " + // Optional, can be null
                    "subject TEXT NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "status TEXT NOT NULL, " + // e.g., 'Open', 'Resolved'
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                    "FOREIGN KEY (order_id) REFERENCES orders(order_id))");

            System.out.println("Database schema initialized successfully.");

            System.out.println("Database schema initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
        }
    }
}
