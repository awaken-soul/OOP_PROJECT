package com.logistics.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
            // Copy the DB from resources to a temp file
            File tempDbFile = extractDatabaseFromResources("logistics.db");
            String dbUrl = "jdbc:sqlite:" + tempDbFile.getAbsolutePath();

            connection = DriverManager.getConnection(dbUrl);
            System.out.println("Connection to SQLite has been established.");
            initializeSchema(); // create tables if they don't exist
        } catch (SQLException | IOException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
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

    private File extractDatabaseFromResources(String resourceName) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            throw new IOException("Database resource not found: " + resourceName);
        }

        // Create a temp file that will be deleted on JVM exit
        File tempFile = File.createTempFile("logistics_", ".db");
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        }

        return tempFile;
    }

    private void initializeSchema() {
        try (Statement stmt = connection.createStatement()) {
            // Example: Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL CHECK(role IN ('CUSTOMER','ADMIN','AGENT','MANAGER'))" +
                    ")");

            // Add other tables here as needed
            System.out.println("Database schema initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
