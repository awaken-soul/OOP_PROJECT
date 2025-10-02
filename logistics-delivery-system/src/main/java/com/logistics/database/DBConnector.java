package com.logistics.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the connection to the SQLite database and ensures the schema (tables) is initialized.
 */
public class DBConnector {

    // Path to the SQLite database file
    private static final String URL = "jdbc:sqlite:src/main/resources/logistics.db";

    /**
     * Establishes a connection to the SQLite database.
     * @return A Connection object if successful, null otherwise.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Loading the SQLite driver is typically automatic since Java 6, 
            // but including Class.forName for robustness in older environments.
            Class.forName("org.sqlite.JDBC"); 
            conn = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Creates all necessary tables for the Logistics and Delivery Management System.
     */
    public static void createTables() {
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement()) {

            // SQL for all tables based on your Database Design [cite: 211-230]

            // 1. User Table
            String sqlUser = "CREATE TABLE IF NOT EXISTS User (" +
                             "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                             "name TEXT NOT NULL," +
                             "email TEXT UNIQUE NOT NULL," +
                             "password TEXT NOT NULL," + // Stores the HASH
                             "role TEXT NOT NULL," + // Enum: Customer, Admin, Agent, Manager [cite: 214]
                             "contact_number TEXT," +
                             "address TEXT" +
                             ");";
            stmt.execute(sqlUser);
            
            // 2. Retailer Table
            String sqlRetailer = "CREATE TABLE IF NOT EXISTS Retailer (" +
                                 "retailer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                 "name TEXT NOT NULL," +
                                 "address TEXT," +
                                 "contact_number TEXT" +
                                 ");";
            stmt.execute(sqlRetailer);

            // 3. Warehouse Table
            String sqlWarehouse = "CREATE TABLE IF NOT EXISTS Warehouse (" +
                                  "warehouse_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                  "name TEXT NOT NULL," +
                                  "address TEXT," +
                                  "capacity INTEGER," +
                                  "manager_id INTEGER," +
                                  "FOREIGN KEY (manager_id) REFERENCES User(user_id)" +
                                  ");";
            stmt.execute(sqlWarehouse);

            // 4. Vehicle Table
            String sqlVehicle = "CREATE TABLE IF NOT EXISTS Vehicle (" +
                                "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "vehicle_type TEXT NOT NULL," + // Enum: Truck, Van, Bike, etc. [cite: 223]
                                "license_plate TEXT UNIQUE NOT NULL," +
                                "status TEXT NOT NULL," + // Enum: Available, On Delivery, Maintenance [cite: 223]
                                "driver_id INTEGER," +
                                "current_location TEXT," +
                                "FOREIGN KEY (driver_id) REFERENCES User(user_id)" +
                                ");";
            stmt.execute(sqlVehicle);

            // 5. Product Table (Inventory)
            String sqlProduct = "CREATE TABLE IF NOT EXISTS Product (" +
                                "product_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "name TEXT NOT NULL," +
                                "description TEXT," +
                                "price REAL," +
                                "quantity INTEGER," + // Current stock level
                                "warehouse_id INTEGER," +
                                "retailer_id INTEGER," +
                                "FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id)," +
                                "FOREIGN KEY (retailer_id) REFERENCES Retailer(retailer_id)" +
                                ");";
            stmt.execute(sqlProduct);

            // 6. Orders Table
            String sqlOrders = "CREATE TABLE IF NOT EXISTS Orders (" +
                                "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "user_id INTEGER NOT NULL," +
                                "product_id INTEGER," + // Nullable if Shipment/Transport only [cite: 226]
                                "order_type TEXT NOT NULL," + // Enum: Purchase, Shipment, Transport [cite: 226]
                                "source_address TEXT NOT NULL," +
                                "destination_address TEXT NOT NULL," +
                                "status TEXT NOT NULL," + // Enum: Pending, In Warehouse, Out for Delivery, Delivered [cite: 226]
                                "assigned_agent_id INTEGER," +
                                "vehicle_id INTEGER," +
                                "payment_status TEXT NOT NULL," + // Enum: Pending, Paid, COD [cite: 226]
                                "created_at DATETIME," +
                                "updated_at DATETIME," +
                                "FOREIGN KEY (user_id) REFERENCES User(user_id)," +
                                "FOREIGN KEY (product_id) REFERENCES Product(product_id)," +
                                "FOREIGN KEY (assigned_agent_id) REFERENCES User(user_id)," +
                                "FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)" +
                                ");";
            stmt.execute(sqlOrders);

            // 7. Payment Table
            String sqlPayment = "CREATE TABLE IF NOT EXISTS Payment (" +
                                "payment_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "order_id INTEGER NOT NULL," +
                                "amount REAL NOT NULL," +
                                "method TEXT NOT NULL," + // Enum: Cash, Card, UPI [cite: 228]
                                "status TEXT NOT NULL," +
                                "payment_date DATETIME," +
                                "FOREIGN KEY (order_id) REFERENCES Orders(order_id)" +
                                ");";
            stmt.execute(sqlPayment);

            // 8. Tracking Table
            String sqlTracking = "CREATE TABLE IF NOT EXISTS Tracking (" +
                                 "tracking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                 "order_id INTEGER NOT NULL," +
                                 "agent_id INTEGER," +
                                 "current_status TEXT NOT NULL," +
                                 "location TEXT," +
                                 "updated_at DATETIME," +
                                 "FOREIGN KEY (order_id) REFERENCES Orders(order_id)," +
                                 "FOREIGN KEY (agent_id) REFERENCES User(user_id)" +
                                 ");";
            stmt.execute(sqlTracking);

            System.out.println("✅ Database schema initialized. All tables created successfully in logistics.db.");

        } catch (SQLException e) {
            System.err.println("Error creating tables. Check SQL syntax: " + e.getMessage());
        }
    }
    
    // Main method to run the setup quickly
    public static void main(String[] args) {
        createTables();
    }
}
