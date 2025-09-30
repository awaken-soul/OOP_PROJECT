package com.logistics.gui;

import com.logistics.database.DBConnector;

import javax.swing.SwingUtilities;

/**
 * The main entry point for the Logistics and Delivery Management System.
 * Initializes the database and starts the Swing GUI.
 */
public class MainApplication {

    public static void main(String[] args) {
        System.out.println("Starting Logistics and Delivery Management System...");
        
        // 1. Initialize the Database Schema
        DBConnector.createTables(); 
        
        // 2. Start the Swing Frontend on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Placeholder: The actual GUI will be initialized here
            System.out.println("GUI initialization started...");
            
            // new LoginFrame().setVisible(true); // Uncomment when LoginFrame is created
        });
        
        System.out.println("System initialization complete.");
    }
}
