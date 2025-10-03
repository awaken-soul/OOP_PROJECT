package com.logistics.gui;

import com.logistics.database.DBConnector;
import com.logistics.database.UserDAO;

import javax.swing.SwingUtilities;

public class MainApplication {

    public static void main(String[] args) {
        System.out.println("Starting Logistics and Delivery Management System...");
        DBConnector.createTables(); 
    
        new UserDAO().initializeTestUsers(); 
        new VehicleDAO().initializeTestVehicles();
        
        System.out.println("System initialization complete. Launching Login Frame...");

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true); 
        });
    }
}
