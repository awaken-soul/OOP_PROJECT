package com.logistics.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;

    private DBConnector() {
        try {
            // Path to your existing DB file
            String dbPath = "src/main/resources/logistics.db"; // make sure this path is correct relative to your project
            File dbFile = new File(dbPath);

            if (!dbFile.exists()) {
                throw new RuntimeException("Database file not found: " + dbFile.getAbsolutePath());
            }

            // Load JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            // Initialize tables if needed
            initializeTables();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection", e);
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

    private void initializeTables() {
        String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL" +
                ");";

        String productTable = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "quantity INTEGER NOT NULL" +
                ");";

        String orderTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "status TEXT NOT NULL," +
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "FOREIGN KEY(product_id) REFERENCES products(id)" +
                ");";

        String paymentTable = "CREATE TABLE IF NOT EXISTS payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "amount REAL NOT NULL," +
                "status TEXT NOT NULL," +
                "FOREIGN KEY(order_id) REFERENCES orders(id)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(productTable);
            stmt.execute(orderTable);
            stmt.execute(paymentTable);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create database tables", e);
        }
    }
}
