package com.logistics.gui;

import com.logistics.database.DBConnector;
import com.logistics.database.UserDAO;

import javax.swing.SwingUtilities;

public class MainApplication {

    public static void main(String[] args) {
        System.out.println("Starting Logistics and Delivery Management System...");
        
        // 1. Initialize the Database Schema (creates tables)
        DBConnector.createTables(); 
        
        // 2. Initialize Test Data (Runs synchronously on the main thread)
        // CRITICAL FIX: Ensure test users are fully inserted before launching the GUI.
        new UserDAO().initializeTestUsers(); 
        
        System.out.println("System initialization complete. Launching Login Frame...");

        // 3. Start the Swing Frontend on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true); 
        });
    }
}
