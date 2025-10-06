package com.logistics.ui;

import com.logistics.data.*;
import com.logistics.model.Admin;
import com.logistics.model.Agent;
import com.logistics.model.Customer;
import com.logistics.model.User;
import com.logistics.service.AdminService;
import com.logistics.service.AgentService;
import com.logistics.service.AuthService;
import com.logistics.service.CustomerService;

import javax.swing.*;
import java.awt.*;

/**
 * The main JFrame of the application.
 * It holds the CardLayout to switch between different panels (Login, Dashboards).
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // Services
    private final AuthService authService;
    private final CustomerService customerService;
    private final AdminService adminService;
    private final AgentService agentService;

    public MainFrame() {
        // Initialize DAOs
        UserDao userDao = new UserDao();
        ProductDao productDao = new ProductDao();
        ShipmentDao shipmentDao = new ShipmentDao();
        WarehouseDao warehouseDao = new WarehouseDao();

        // Initialize Services with their required DAOs
        this.authService = new AuthService(userDao);
        this.customerService = new CustomerService(productDao, shipmentDao);
        this.adminService = new AdminService(authService, warehouseDao);
        this.agentService = new AgentService(shipmentDao, warehouseDao);

        initFrame();
        showLoginPanel();
    }

    private void initFrame() {
        setTitle("Logistics Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        add(mainPanel);
    }

    private void showLoginPanel() {
        LoginPanel loginPanel = new LoginPanel(this, authService);
        mainPanel.add(loginPanel, "Login");
        cardLayout.show(mainPanel, "Login");
    }

    /**
     * Called by LoginPanel upon successful login to switch to the correct dashboard.
     * @param user The successfully logged-in user.
     */
    public void showDashboard(User user) {
        JPanel dashboardPanel;
        String panelName;

        switch (user.getRole()) {
            case "CUSTOMER":
                dashboardPanel = new CustomerPanel(customerService, (Customer) user);
                panelName = "CustomerDashboard";
                break;
            case "AGENT":
                dashboardPanel = new AgentPanel(agentService, (Agent) user);
                panelName = "AgentDashboard";
                break;
            case "ADMIN":
                dashboardPanel = new AdminPanel(adminService);
                panelName = "AdminDashboard";
                break;
            default:
                // Fallback to login if role is unknown
                showLoginPanel();
                return;
        }

        mainPanel.add(dashboardPanel, panelName);
        cardLayout.show(mainPanel, panelName);
    }
}

