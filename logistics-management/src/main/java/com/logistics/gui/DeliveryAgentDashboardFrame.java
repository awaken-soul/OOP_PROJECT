package com.logistics.gui;

import com.logistics.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeliveryAgentDashboardFrame extends JFrame {

    private JTable deliveriesTable;
    private DefaultTableModel tableModel;

    public DeliveryAgentDashboardFrame(User agentUser) {
        setTitle("Delivery Agent Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel with Welcome Message and Logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel welcomeLabel = new JLabel("Welcome, Agent " + agentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton logoutButton = new JButton("Logout");
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Center Panel with Deliveries Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Assigned Deliveries"));

        String columnNames = {"ID", "Assigned Deliveries (Address)", "Status", "History"};
        // Placeholder data - this will be replaced with real data from the database
        Object data = {
                {"101", "123 Maple St, Springfield", "Out for Delivery", "View"},
                {"102", "456 Oak Ave, Shelbyville", "Pending", "View"},
                {"103", "789 Pine Ln, Capital City", "Pending", "View"}
        };

        tableModel = new DefaultTableModel(data, columnNames);
        deliveriesTable = new JTable(tableModel);
        deliveriesTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(deliveriesTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton updateStatusButton = new JButton("Update Status");
        bottomPanel.add(updateStatusButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // TODO: Add ActionListeners for buttons to interact with the service layer
    }
}
