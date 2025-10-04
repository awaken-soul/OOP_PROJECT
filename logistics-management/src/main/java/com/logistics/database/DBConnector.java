package com.logistics.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;

    private DBConnector() {
        try {
            // Copy DB from JAR to temporary file
            String dbName = "logistics.db";
            File tempFile = File.createTempFile("logistics-temp-", ".db");
            tempFile.deleteOnExit();

            try (InputStream is = getClass().getResourceAsStream("/database/" + dbName);
                 FileOutputStream fos = new FileOutputStream(tempFile)) {

                if (is == null) {
                    throw new RuntimeException("Database file not found in resources: " + dbName);
                }

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            // Load JDBC driver and connect
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + tempFile.getAbsolutePath());

            // Initialize tables if they don't exist
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
