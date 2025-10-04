package com.logistics.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;

    private DBConnector() {
        try {
            // Load database from resources
            URL dbUrl = getClass().getClassLoader().getResource("logistics.db");
            if (dbUrl == null) {
                throw new RuntimeException("Database file 'logistics.db' not found in resources.");
            }
            String path = Paths.get(dbUrl.toURI()).toString();
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            System.out.println("Connection to SQLite has been established.");
            initializeSchema();
        } catch (SQLException | URISyntaxException e) {
            throw new RuntimeException("Failed to connect to the database: " + e.getMessage(), e);
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
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL CHECK(role IN ('CUSTOMER', 'ADMIN', 'AGENT', 'MANAGER'))," +
                    "contact_number TEXT," +
                    "address TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS warehouse (" +
                    "warehouse_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "address TEXT," +
                    "capacity INTEGER," +
                    "manager_id INTEGER," +
                    "FOREIGN KEY (manager_id) REFERENCES users(user_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS retailer (" +
                    "retailer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "address TEXT," +
                    "contact_number TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS product (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT," +
                    "price REAL," +
                    "quantity INTEGER," +
                    "warehouse_id INTEGER," +
                    "retailer_id INTEGER," +
                    "FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id)," +
                    "FOREIGN KEY (retailer_id) REFERENCES retailer(retailer_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS vehicles (" +
                    "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "vehicle_type TEXT NOT NULL," +
                    "license_plate TEXT UNIQUE NOT NULL," +
                    "status TEXT NOT NULL," +
                    "driver_id INTEGER," +
                    "current_location TEXT," +
                    "FOREIGN KEY (driver_id) REFERENCES users(user_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "product_id INTEGER," +
                    "order_type TEXT NOT NULL," +
                    "source_address TEXT NOT NULL," +
                    "destination_address TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "assigned_agent_id INTEGER," +
                    "vehicle_id INTEGER," +
                    "payment_status TEXT NOT NULL," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id)," +
                    "FOREIGN KEY (product_id) REFERENCES product(product_id)," +
                    "FOREIGN KEY (assigned_agent_id) REFERENCES users(user_id)," +
                    "FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id))");

            System.out.println("Database schema initialized successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database schema: " + e.getMessage(), e);
        }
    }
}
