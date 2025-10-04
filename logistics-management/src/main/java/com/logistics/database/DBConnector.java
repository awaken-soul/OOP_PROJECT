package com.logistics.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;

    private DBConnector() {
        try {
            // Create a temporary SQLite database file
            Path tempDb = Files.createTempFile("logistics", ".db");
            String dbUrl = "jdbc:sqlite:" + tempDb.toAbsolutePath();

            connection = DriverManager.getConnection(dbUrl);
            System.out.println("Connection to SQLite has been established.");
            
            // Initialize schema (create tables)
            initializeSchema();
        } catch (SQLException | java.io.IOException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw new RuntimeException(e);
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
            // Users Table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL CHECK(role IN ('CUSTOMER', 'ADMIN', 'AGENT', 'MANAGER')), " +
                    "contact_number TEXT, " +
                    "address TEXT)");

            // Warehouse Table
            stmt.execute("CREATE TABLE IF NOT EXISTS warehouse (" +
                    "warehouse_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT, " +
                    "capacity INTEGER, " +
                    "manager_id INTEGER, " +
                    "FOREIGN KEY (manager_id) REFERENCES users(user_id))");

            // Retailer Table
            stmt.execute("CREATE TABLE IF NOT EXISTS retailer (" +
                    "retailer_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT, " +
                    "contact_number TEXT)");

            // Product Table
            stmt.execute("CREATE TABLE IF NOT EXISTS product (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT
