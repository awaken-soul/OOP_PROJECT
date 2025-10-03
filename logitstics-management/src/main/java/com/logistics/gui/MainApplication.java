package com.logistics.gui;

import com.logistics.database.DBConnector;
import com.logistics.database.UserDAO;
import com.logistics.database.VehicleDAO;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainApplication {

    public static void main(String[] args) {
        // Set System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Silently fall back to default
        }
        
        System.out.println("Starting Logistics and Delivery Management System...");
        
        // 1. Initialize the Database Schema (creates tables)
        DBConnector.createTables(); 
        
        // 2. Initialize Test Data (must run synchronously to guarantee data exists)
        UserDAO userDAO = new UserDAO();
        userDAO.initializeTestUsers();
        new VehicleDAO().initializeTestVehicles();
        
        System.out.println("System initialization complete. Launching Login Frame...");

        // 3. Start the Swing Frontend on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true); 
        });
    }
}
