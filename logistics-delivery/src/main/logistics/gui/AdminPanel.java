package com.logistics.ui;

import com.logistics.service.AdminService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JPanel for the Admin dashboard.
 * Provides UI for adding warehouses and agents.
 */
public class AdminPanel extends JPanel {

    private final AdminService adminService;

    // UI Components for adding a warehouse
    private final JTextField warehouseNameField = new JTextField(20);
    private final JTextField warehouseLocationField = new JTextField(20);
    private final JButton addWarehouseButton = new JButton("Add Warehouse");

    // UI Components for adding an agent
    private final JTextField agentEmailField = new JTextField(20);
    private final JPasswordField agentPasswordField = new JPasswordField(20);
    private final JButton addAgentButton = new JButton("Add Agent");

    public AdminPanel(AdminService adminService) {
        this.adminService = adminService;
        initUI();
        attachListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Main panel to hold both forms
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));

        formContainer.add(createWarehousePanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        formContainer.add(createAgentPanel());

        add(new JLabel("Admin Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);
        add(formContainer, BorderLayout.CENTER);
    }

    private JPanel createWarehousePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Warehouse"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        panel.add(warehouseNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Location:"), gbc);

        gbc.gridx = 1;
        panel.add(warehouseLocationField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addWarehouseButton, gbc);

        return panel;
    }

    private JPanel createAgentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Agent"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        panel.add(agentEmailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        panel.add(agentPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addAgentButton, gbc);

        return panel;
    }


    private void attachListeners() {
        addWarehouseButton.addActionListener(this::handleAddWarehouse);
        addAgentButton.addActionListener(this::handleAddAgent);
    }

    private void handleAddWarehouse(ActionEvent e) {
        String name = warehouseNameField.getText();
        String location = warehouseLocationField.getText();

        if (name.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Warehouse name and location cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = adminService.addWarehouse(name, location);
        if (success) {
            JOptionPane.showMessageDialog(this, "Warehouse added successfully!");
            warehouseNameField.setText("");
            warehouseLocationField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add warehouse.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddAgent(ActionEvent e) {
        String email = agentEmailField.getText();
        String password = new String(agentPasswordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agent email and password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = adminService.addAgent(email, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "Agent created successfully!");
            agentEmailField.setText("");
            agentPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create agent. Email might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

