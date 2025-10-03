package com.logistics.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    private static final String URL = "jdbc:sqlite:logistics.db";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC"); 
            conn = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }
    
    public static void createTables() {
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement()) {
            String sqlUser = "CREATE TABLE IF NOT EXISTS User (" +
                             "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                             "name TEXT NOT NULL," +
                             "email TEXT UNIQUE NOT NULL," +
                             "password TEXT NOT NULL," + 
                             "role TEXT NOT NULL," + 
                             "contact_number TEXT," +
                             "address TEXT" +
                             ");";
            stmt.execute(sqlUser);
            
            String sqlRetailer = "CREATE TABLE IF NOT EXISTS Retailer (" +
                                 "retailer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                 "name TEXT NOT NULL," +
                                 "address TEXT," +
                                 "contact_number TEXT" +
                                 ");";
            stmt.execute(sqlRetailer);

            String sqlWarehouse = "CREATE TABLE IF NOT EXISTS Warehouse (" +
                                  "warehouse_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                  "name TEXT NOT NULL," +
                                  "address TEXT," +
                                  "capacity INTEGER," +
                                  "manager_id INTEGER," +
                                  "FOREIGN KEY (manager_id) REFERENCES User(user_id)" +
                                  ");";
            stmt.execute(sqlWarehouse);

            String sqlVehicle = "CREATE TABLE IF NOT EXISTS Vehicle (" +
                                "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "vehicle_type TEXT NOT NULL," +
                                "license_plate TEXT UNIQUE NOT NULL," +
                                "status TEXT NOT NULL," + 
                                "driver_id INTEGER," +
                                "current_location TEXT," +
                                "FOREIGN KEY (driver_id) REFERENCES User(user_id)" +
                                ");";
            stmt.execute(sqlVehicle);
            
            String sqlProduct = "CREATE TABLE IF NOT EXISTS Product (" +
                                "product_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "name TEXT NOT NULL," +
                                "description TEXT," +
                                "price REAL," +
                                "quantity INTEGER," + 
                                "warehouse_id INTEGER," +
                                "retailer_id INTEGER," +
                                "FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id)," +
                                "FOREIGN KEY (retailer_id) REFERENCES Retailer(retailer_id)" +
                                ");";
            stmt.execute(sqlProduct);

            String sqlOrders = "CREATE TABLE IF NOT EXISTS Orders (" +
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
                                "created_at DATETIME," +
                                "updated_at DATETIME," +
                                "FOREIGN KEY (user_id) REFERENCES User(user_id)," +
                                "FOREIGN KEY (product_id) REFERENCES Product(product_id)," +
                                "FOREIGN KEY (assigned_agent_id) REFERENCES User(user_id)," +
                                "FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)" +
                                ");";
            stmt.execute(sqlOrders);

            String sqlPayment = "CREATE TABLE IF NOT EXISTS Payment (" +
                                "payment_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "order_id INTEGER NOT NULL," +
                                "amount REAL NOT NULL," +
                                "method TEXT NOT NULL," + 
                                "status TEXT NOT NULL," +
                                "payment_date DATETIME," +
                                "FOREIGN KEY (order_id) REFERENCES Orders(order_id)" +
                                ");";
            stmt.execute(sqlPayment);

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

    public static void main(String[] args) {
        createTables();
    }
}
