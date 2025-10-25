package com.logistics.ui;

import com.logistics.model.User;
import com.logistics.model.Warehouse;
import com.logistics.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Admin Dashboard for managing Warehouses and Agents.
 * Fully aligned with the official DB schema.
 */
public class AdminDashboard extends JFrame {

    private final AdminService adminService;
    private JTable warehouseTable;
    private JTable agentTable;

    public AdminDashboard(AdminService adminService) {
        this.adminService = adminService;

        setTitle("Admin Dashboard - Logistics Management");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Warehouses Tab
        JPanel warehousePanel = new JPanel(new BorderLayout(10, 10));
        warehouseTable = new JTable();
        refreshWarehouses();
        warehousePanel.add(new JScrollPane(warehouseTable), BorderLayout.CENTER);

        JButton addWarehouseBtn = new JButton("➕ Add Warehouse");
        addWarehouseBtn.addActionListener(this::addWarehouseAction);
        JPanel whBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        whBtnPanel.add(addWarehouseBtn);
        warehousePanel.add(whBtnPanel, BorderLayout.SOUTH);

        tabs.addTab("Warehouses", warehousePanel);

        // Agents Tab
        JPanel agentPanel = new JPanel(new BorderLayout(10, 10));
        agentTable = new JTable();
        refreshAgents();
        agentPanel.add(new JScrollPane(agentTable), BorderLayout.CENTER);

        JButton addAgentBtn = new JButton("➕ Add Agent");
        addAgentBtn.addActionListener(this::addAgentAction);
        JPanel agBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        agBtnPanel.add(addAgentBtn);
        agentPanel.add(agBtnPanel, BorderLayout.SOUTH);

        tabs.addTab("Agents", agentPanel);

        add(tabs, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            System.exit(0);
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /** ------------------------- ACTIONS ------------------------- **/

    private void addWarehouseAction(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField managerIdField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Address:", addressField,
                "Capacity:", capacityField,
                "Manager ID:", managerIdField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Warehouse", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String address = addressField.getText().trim();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                int managerId = Integer.parseInt(managerIdField.getText().trim());

                boolean success = adminService.addWarehouse(name, address, capacity, managerId);
                JOptionPane.showMessageDialog(this,
                        success ? "Warehouse added successfully!" : "Failed to add warehouse.",
                        success ? "Success" : "Error",
                        success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                refreshWarehouses();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Check all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addAgentAction(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField contactField = new JTextField();
        JTextField addressField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Email:", emailField,
                "Password:", passwordField,
                "Contact Number:", contactField,
                "Address:", addressField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Agent", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();

            boolean success = adminService.addAgent(name, email, password, contact, address);
            JOptionPane.showMessageDialog(this,
                    success ? "Agent added successfully!" : "Failed to add agent.",
                    success ? "Success" : "Error",
                    success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            refreshAgents();
        }
    }

    /** ------------------------- REFRESH TABLES ------------------------- **/

    private void refreshWarehouses() {
        List<Warehouse> warehouses = adminService.getAllWarehouses();
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Warehouse ID", "Name", "Address", "Capacity", "Manager ID"}, 0);
        for (Warehouse w : warehouses) {
            model.addRow(new Object[]{w.getWarehouseId(), w.getName(), w.getAddress(), w.getCapacity(), w.getManagerId()});
        }
        warehouseTable.setModel(model);
    }

    private void refreshAgents() {
        List<User> agents = adminService.getAllAgents();
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"User ID", "Name", "Email", "Contact", "Address"}, 0);
        for (User a : agents) {
            model.addRow(new Object[]{a.getUserId(), a.getName(), a.getEmail(), a.getContactNumber(), a.getAddress()});
        }
        agentTable.setModel(model);
    }
}
