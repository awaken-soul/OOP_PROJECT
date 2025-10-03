package com.logistics.gui;

import com.logistics.models.Order;
import com.logistics.models.Tracking;
import com.logistics.models.User;
import com.logistics.services.OrderService;
import com.logistics.services.TrackingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeliveryAgentDashboardFrame extends JFrame {

    private final JTable deliveriesTable;
    private final DefaultTableModel tableModel;
    private final User agentUser;
    private final OrderService orderService;
    private final TrackingService trackingService;

    public DeliveryAgentDashboardFrame(User agentUser, OrderService orderService, TrackingService trackingService) {
        this.agentUser = agentUser;
        this.orderService = orderService;
        this.trackingService = trackingService;

        setTitle("Delivery Agent Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel welcomeLabel = new JLabel("Welcome, Agent " + agentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Assigned Deliveries"));

        String columnNames = {"Order ID", "Destination Address", "Status", "Last Updated"};
        tableModel = new DefaultTableModel(columnNames, 0);
        deliveriesTable = new JTable(tableModel);
        deliveriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(deliveriesTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton updateStatusButton = new JButton("Update Status");
        JButton viewHistoryButton = new JButton("View History");
        bottomPanel.add(updateStatusButton);
        bottomPanel.add(viewHistoryButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateStatusButton.addActionListener(e -> updateSelectedOrderStatus());
        viewHistoryButton.addActionListener(e -> showTrackingHistory());

        loadAgentOrders();
    }

    private void loadAgentOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderService.getOrdersForAgent(agentUser.getUserId());
        for (Order order : orders) {
            tableModel.addRow(new Object{order.getOrderId(), order.getDestinationAddress(), order.getStatus(), order.getUpdatedAt()});
        }
    }

    private void showTrackingHistory() {
        int selectedRow = deliveriesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to view its history.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer orderId = (Integer) deliveriesTable.getValueAt(selectedRow, 0);
        List<Tracking> history = trackingService.getTrackingHistoryForOrder(orderId);

        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tracking history found for this order.", "No History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        TrackingHistoryDialog dialog = new TrackingHistoryDialog(this, orderId, history);
        dialog.setVisible(true);
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
            if (orderService.updateOrderStatus(orderId, newStatus.trim())) {
                JOptionPane.showMessageDialog(this, "Order status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAgentOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update order status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
