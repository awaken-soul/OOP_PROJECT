package com.logistics.gui;

import com.logistics.models.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {

    public AdminDashboardFrame(User adminUser) {
        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Left Panel for Navigation
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

        // Set consistent size for buttons
        Dimension buttonSize = new Dimension(180, 40);
        manageVendorsButton.setMaximumSize(buttonSize);
        manageVehiclesButton.setMaximumSize(buttonSize);
        manageComplaintsButton.setMaximumSize(buttonSize);
        logoutButton.setMaximumSize(buttonSize);

        navigationPanel.add(adminNameLabel);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        navigationPanel.add(manageVendorsButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(manageVehiclesButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(manageComplaintsButton);
        navigationPanel.add(Box.createVerticalGlue()); // Pushes logout to the bottom
        navigationPanel.add(logoutButton);

        // Center Panel for displaying content
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Placeholder panels based on wireframe [1]
        JPanel complaintPanel = new JPanel();
        complaintPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Complaint Section", TitledBorder.CENTER, TitledBorder.TOP));

        JPanel vendorPanel = new JPanel();
        vendorPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Vendor List", TitledBorder.CENTER, TitledBorder.TOP));

        JPanel vehiclePanel = new JPanel();
        vehiclePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Vehicle Status", TitledBorder.CENTER, TitledBorder.TOP));

        JPanel agentPanel = new JPanel();
        agentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Agent Profile", TitledBorder.CENTER, TitledBorder.TOP));

        contentPanel.add(complaintPanel);
        contentPanel.add(vendorPanel);
        contentPanel.add(vehiclePanel);
        contentPanel.add(agentPanel);

        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // TODO: Add ActionListeners for buttons to swap content panels
    }
}
