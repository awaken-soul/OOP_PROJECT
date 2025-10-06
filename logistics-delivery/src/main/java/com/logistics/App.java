package com.logistics;

import com.logistics.data.Database;
import com.logistics.ui.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the application.
 * Initializes the database and launches the GUI.
 */
public class App {
    public static void main(String[] args) {
        // Initialize the database connection on startup.
        Database.getConnection();

        // Add a shutdown hook to cleanly close the database connection when the JVM terminates.
        // This is important for releasing the file lock on logistics.db.
        Runtime.getRuntime().addShutdownHook(new Thread(Database::close));

        // Run the GUI on the Event Dispatch Thread for thread safety.
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}

