package com.logistics.gui;

import com.logistics.models.Order;
import com.logistics.models.User;
import com.logistics.services.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeliveryAgentDashboardFrame extends JFrame {

    private final JTable deliveriesTable;
    private final DefaultTableModel tableModel;
    private final User agentUser;
    private final OrderService orderService;

    public DeliveryAgentDashboardFrame(User agentUser, OrderService orderService) {
        this.agentUser = agentUser;
        this.orderService = orderService;

        setTitle("Delivery Agent Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel welcomeLabel = new JLabel("Welcome, Agent " + agentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton logoutButton = new JButton("Logout");
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Assigned Deliveries"));

        String columnNames = {"Order ID", "Destination Address", "Status", "Last Updated"};
        tableModel = new DefaultTableModel(columnNames, 0);
        deliveriesTable = new JTable(tableModel);
        deliveriesTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(deliveriesTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton updateStatusButton = new JButton("Update Status");
        bottomPanel.add(updateStatusButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateStatusButton.addActionListener(e -> updateSelectedOrderStatus());

        loadAgentOrders();
    }

    private void loadAgentOrders() {
        // Clear existing data
        tableModel.setRowCount(0);

        List<Order> orders = orderService.getOrdersForAgent(agentUser.getUserId());
        for (Order order : orders) {
            Object rowData = {
                    order.getOrderId(),
                    order.getDestinationAddress(),
                    order.getStatus(),
                    order.getUpdatedAt()
            };
            tableModel.addRow(rowData);
        }
    }

    private void updateSelectedOrderStatus() {
        int selectedRow = deliveriesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer orderId = (Integer) deliveriesTable.getValueAt(selectedRow, 0);
        String currentStatus = (String) deliveriesTable.getValueAt(selectedRow, 2);

        String newStatus = JOptionPane.showInputDialog(this, "Enter new status for Order ID " + orderId + ":", "Update Status", JOptionPane.PLAIN_MESSAGE);

        if (newStatus!= null &&!newStatus.trim().isEmpty() &&!newStatus.equals(currentStatus)) {
            boolean success = orderService.updateOrderStatus(orderId, newStatus.trim());
            if (success) {
                JOptionPane.showMessageDialog(this, "Order status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAgentOrders(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update order status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
