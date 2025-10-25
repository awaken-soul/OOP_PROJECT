package com.logistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ClearDatabase {
    private static final String DB_URL = "jdbc:sqlite:logistics.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Disable foreign key constraints temporarily
            stmt.execute("PRAGMA foreign_keys = OFF;");

            // Delete all data
            String[] tables = {
                "Tracking",
                "Payment",
                "Orders",
                "Vehicle",
                "Product",
                "Warehouse",
                "Retailer",
                "User"
            };

            for (String table : tables) {
                stmt.executeUpdate("DELETE FROM " + table + ";");
                stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='" + table + "';"); // reset autoincrement
                System.out.println("✅ Cleared table: " + table);
            }

            // Re-enable foreign key constraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            System.out.println("🎉 All tables cleared successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
