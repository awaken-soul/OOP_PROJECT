package com.logistics.gui;

import com.logistics.models.User;
import com.logistics.services.RetailerService;
import com.logistics.services.VehicleService;
import com.logistics.services.WarehouseService;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    public AdminDashboardFrame(User adminUser, VehicleService vehicleService, RetailerService retailerService, WarehouseService warehouseService) {
        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Navigation Panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navigationPanel.setPreferredSize(new Dimension(200, 0));

        JLabel adminNameLabel = new JLabel(adminUser.getName());
        adminNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        adminNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton manageVendorsButton = new JButton("Manage Vendors");
        JButton manageVehiclesButton = new JButton("Manage Vehicles");
        JButton manageComplaintsButton = new JButton("Manage Complaints");
        JButton logoutButton = new JButton("Logout");

        Dimension buttonSize = new Dimension(180, 40);
        manageVendorsButton.setMaximumSize(buttonSize);
        manageVehiclesButton.setMaximumSize(buttonSize);
        manageComplaintsButton.setMaximumSize(buttonSize);
        logoutButton.setMaximumSize(buttonSize);

        navigationPanel.add(adminNameLabel);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        navigationPanel.add(manageVendorsButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(manageVehiclesButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(manageComplaintsButton);
        navigationPanel.add(Box.createVerticalGlue());
        navigationPanel.add(logoutButton);

        // Create the panels for the CardLayout
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.add(new JLabel("Welcome, Admin! Select an option from the left menu."));
        ManageVehiclesPanel manageVehiclesPanel = new ManageVehiclesPanel(vehicleService);
        ManageVendorsPanel manageVendorsPanel = new ManageVendorsPanel(retailerService, warehouseService);

        // Add panels to the content panel
        contentPanel.add(welcomePanel, "WELCOME");
        contentPanel.add(manageVehiclesPanel, "MANAGE_VEHICLES");
        contentPanel.add(manageVendorsPanel, "MANAGE_VENDORS");

        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Add ActionListeners to switch panels
        manageVehiclesButton.addActionListener(e -> cardLayout.show(contentPanel, "MANAGE_VEHICLES"));
        manageVendorsButton.addActionListener(e -> cardLayout.show(contentPanel, "MANAGE_VENDORS"));
    }
}
