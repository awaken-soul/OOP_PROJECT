package com.logistics.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;

    private DBConnector() {
        try {
            // Load the DB from resources and copy to a temp file
            InputStream dbStream = getClass().getClassLoader().getResourceAsStream("logistics.db");
            if (dbStream == null) {
                throw new RuntimeException("Database 'logistics.db' not found in resources!");
            }

            Path tempDb = Files.createTempFile("logistics", ".db");
            Files.copy(dbStream, tempDb, StandardCopyOption.REPLACE_EXISTING);

            String dbUrl = "jdbc:sqlite:" + tempDb.toAbsolutePath();
            connection = DriverManager.getConnection(dbUrl);

            System.out.println("Connection to SQLite has been established.");
            initializeSchema();

        } catch (SQLException | IOException e) {
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
            // Example table creation, you can add all your tables here
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL CHECK(role IN ('CUSTOMER', 'ADMIN', 'AGENT', 'MANAGER'))" +
                    ")");
            // Add your other table creation statements here...
            System.out.println("Database schema initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
        }
    }
}
