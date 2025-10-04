package com.logistics.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;

    private DBConnector() {
        try {
            // Load DB from resources inside the JAR
            String dbPath = Paths.get(getClass().getClassLoader()
                    .getResource("logistics.db").toURI()).toString();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Connection to SQLite has been established.");
            initializeSchema();
        } catch (SQLException | URISyntaxException e) {
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
        // Your existing schema initialization code
    }
}
