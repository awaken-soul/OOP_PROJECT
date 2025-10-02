package com.logistics.gui;

import com.logistics.database.DBConnector;

import javax.swing.SwingUtilities;

public class MainApplication {

    public static void main(String[] args) {
        System.out.println("Starting Logistics and Delivery Management System...");
        
        // 1. Initialize the Database Schema
        DBConnector.createTables(); 
        
        // 2. Start the Swing Frontend on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            System.out.println("Launching Login Frame...");
            new LoginFrame().setVisible(true); // Launch the login screen
        });
        
        System.out.println("System initialization complete.");
    }
}
