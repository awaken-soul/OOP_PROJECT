package com.logistics;

import com.logistics.ui.MainFrame;
import javax.swing.SwingUtilities;

/**
 * The main entry point for the Logistics Management application.
 * This class is responsible for initializing and launching the user interface.
 */
public class App {
    
    /**
     * The main method that starts the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Swing GUI should be created and updated on the Event Dispatch Thread (EDT).
        // SwingUtilities.invokeLater ensures that this happens.
        SwingUtilities.invokeLater(() -> {
            // Create the main application window (JFrame) and make it visible.
            // The MainFrame constructor will handle the creation of all necessary
            // services and panels.
            new MainFrame().setVisible(true);
        });
    }
}

