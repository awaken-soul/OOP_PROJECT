package com.logistics.gui;

import com.logistics.database.DBConnector;
import com.logistics.database.UserDAO; // <-- Import UserDAO

import javax.swing.SwingUtilities;

public class MainApplication {

    public static void main(String[] args) {
        System.out.println("Starting Logistics and Delivery Management System...");
        
        // 1. Initialize the Database Schema (creates tables)
        DBConnector.createTables(); 
        
        // 2. Initialize Test Data (runs only if tables are empty)
        new UserDAO().initializeTestUsers(); // <-- New initialization step
        
        // 3. Start the Swing Frontend on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            System.out.println("Launching Login Frame...");
            new LoginFrame().setVisible(true); 
        });
        
        System.out.println("System initialization complete.");
    }
}
