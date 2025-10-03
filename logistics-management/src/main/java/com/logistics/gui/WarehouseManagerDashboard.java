package com.logistics.gui;

import com.logistics.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WarehouseManagerDashboardFrame extends JFrame {

    private JTable stockTable;
    private DefaultTableModel tableModel;

    public WarehouseManagerDashboardFrame(User managerUser) {
        setTitle("Warehouse Manager Dashboard");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel for Profile and Logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel profileLabel = new JLabel("WAREHOUSE PROFILE | Welcome, " + managerUser.getName());
        profileLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton logoutButton = new JButton("Logout");
        topPanel.add(profileLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Stock Levels Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Stock Levels"));

        String columnNames = {"Product ID", "Product Name", "Quantity", "Status"};
        // Placeholder data, to be replaced by database calls
        Object data = {
                {"P1001", "Laptop", 50, "In Stock"},
                {"P1002", "Smartphone", 120, "In Stock"},
                {"P1003", "Headphones", 75, "Low Stock"}
        };
        tableModel = new DefaultTableModel(data, columnNames);
        stockTable = new JTable(tableModel);
        stockTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(stockTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Action Panel for buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionPanel.setPreferredSize(new Dimension(200, 0));

        JButton assignProductsButton = new JButton("Assign Products");
        JButton trackOrderButton = new JButton("Track Order");
        JButton trackShipmentsButton = new JButton("Track Shipments");

        Dimension buttonSize = new Dimension(180, 40);
        assignProductsButton.setMaximumSize(buttonSize);
        trackOrderButton.setMaximumSize(buttonSize);
        trackShipmentsButton.setMaximumSize(buttonSize);

        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(assignProductsButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(trackOrderButton);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(trackShipmentsButton);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // TODO: Add ActionListeners for buttons to call service layer methods
    }
}
