package com.logistics.gui;

import com.logistics.database.UserDAO;
import com.logistics.models.User;
import com.logistics.services.UserService;

import javax.swing.*;

public class MainApplication {

    // Instantiate all DAOs and Services here. This acts as our dependency injection hub.
    private static final UserDAO userDAO = new UserDAO();
    private static final UserService userService = new UserService(userDAO);
    //... instantiate other DAOs and Services as they are created

    public static void main(String args) {
        // Use SwingUtilities to ensure UI is created on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(userService);
            loginFrame.setVisible(true);
        });
    }

    public static void showDashboardForUser(User user) {
        // This method is called by LoginFrame upon successful login to navigate to the correct dashboard.
        JFrame dashboard;
        switch (user.getRole()) {
            case ADMIN:
                dashboard = new AdminDashboardFrame(user);
                dashboard.setVisible(true);
                break;
            case AGENT:
                dashboard = new DeliveryAgentDashboardFrame(user);
                dashboard.setVisible(true);
                break;
            case CUSTOMER:
                dashboard = new CustomerDashboardFrame(user);
                dashboard.setVisible(true);
                break;
            case MANAGER:
                dashboard = new WarehouseManagerDashboardFrame(user);
                dashboard.setVisible(true);
                break;
            default:
                throw new IllegalStateException("Unsupported user role: " + user.getRole());
        }
    }
}
