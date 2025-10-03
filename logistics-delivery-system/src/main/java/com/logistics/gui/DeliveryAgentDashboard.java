package com.logistics.gui;

import com.logistics.models.DeliveryAgent;
import com.logistics.models.Order;
import com.logistics.services.OrderService;
import com.logistics.utils.AppColors;
import com.logistics.database.OrderDAO; // Required for fetching assigned orders

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The main dashboard for the Delivery Agent role.
 * Includes panels for assigned deliveries, status updates, and history.
 */
public class DeliveryAgentDashboard extends JFrame {

    private final DeliveryAgent agent;
    private final OrderService orderService;
    private final OrderDAO orderDAO; // DAO dependency for fetching data

    // Components for Assigned Deliveries Panel
    private JList<String> assignedOrdersList;
    private JTextArea deliveryDetailsArea;
    private JComboBox<String> statusUpdateCombo;
    private JTextField orderIdToUpdateField;
    private JLabel updateMessageLabel;

    public DeliveryAgentDashboard(DeliveryAgent agent) {
        super("Delivery Agent Dashboard - Profile: " + agent.getName());
        this.agent = agent;
        this.orderService = new OrderService();
        this.orderDAO = new OrderDAO(); // Initialize the DAO

        // --- Frame Setup ---
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND_WHITE);

        // --- Header ---
        add(createHeaderPanel(), BorderLayout.NORTH);

        // --- Main Content: Split Pane ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createAssignedOrdersPanel(),
                createDeliveryDetailsPanel());
        mainSplit.setDividerLocation(300);
        add(mainSplit, BorderLayout.CENTER);

        // Load real data upon launch
        loadAssignedOrders();
        
        setVisible(true);
    }
    
    // ==========================================================
    // HEADER PANEL
    // ==========================================================
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.PRIMARY_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("DELIVERY AGENT: " + agent.getName());
        titleLabel.setForeground(AppColors.BACKGROUND_WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(AppColors.WARNING_RED);
        logoutButton.setForeground(AppColors.BACKGROUND_WHITE);
        logoutButton.addActionListener(e -> {
            agent.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        return headerPanel;
    }

    // ==========================================================
    // LEFT PANEL: Assigned Deliveries & History
    // ==========================================================
    private JPanel createAssignedOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        assignedOrdersList = new JList<>();
        assignedOrdersList.setFont(new Font("Arial", Font.PLAIN, 14));
        assignedOrdersList.setBorder(BorderFactory.createTitledBorder("Assigned Deliveries"));
        
        assignedOrdersList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && assignedOrdersList.getSelectedIndex() != -1) {
                String selectedOrder = assignedOrdersList.getSelectedValue();
                int orderId = extractOrderId(selectedOrder);
                loadDeliveryDetails(orderId);
            }
        });

        panel.add(new JScrollPane(assignedOrdersList), BorderLayout.CENTER);
        
        JButton historyButton = new JButton("View Delivery History");
        historyButton.setBackground(AppColors.PRIMARY_BLUE);
        historyButton.setForeground(AppColors.BACKGROUND_WHITE);
        historyButton.addActionListener(e -> {
            agent.maintainDeliveryRecords();
            JOptionPane.showMessageDialog(this, "Fetching historical records for Agent " + agent.getUserID());
        });
        panel.add(historyButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAssignedOrders() {
        // FIX: Fetch real assigned orders using the DAO
        List<Order> orders = orderDAO.getOrdersByAgentId(agent.getUserID());
        
        DefaultListModel<String> model = new DefaultListModel<>();
        
        if (orders.isEmpty()) {
             model.addElement("No orders currently assigned.");
        } else {
            for (Order order : orders) {
                String display = String.format("ID: %d - Pickup: %s - Status: %s",
                    order.getOrderID(), 
                    order.getSourceAddress(), 
                    order.getStatus());
                model.addElement(display);
            }
        }
        assignedOrdersList.setModel(model);
    }
    
    private int extractOrderId(String orderString) {
        // Pull ID from "ID: XXX - ..."
        try {
            return Integer.parseInt(orderString.substring(4, orderString.indexOf(" - ")));
        } catch (Exception e) {
            return -1;
        }
    }

    // ==========================================================
    // RIGHT PANEL: Delivery Details & Status Update
    // ==========================================================
    private JPanel createDeliveryDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        deliveryDetailsArea = new JTextArea("Select an assigned delivery on the left to view details...");
        deliveryDetailsArea.setEditable(false);
        deliveryDetailsArea.setBorder(BorderFactory.createTitledBorder("Delivery Information"));
        panel.add(new JScrollPane(deliveryDetailsArea), BorderLayout.CENTER);
        
        JPanel updatePanel = new JPanel(new GridLayout(3, 1, 5, 5));
        updatePanel.setBorder(BorderFactory.createTitledBorder("Update Status (Order Action)"));
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        orderIdToUpdateField = new JTextField(5);
        inputPanel.add(new JLabel("Order ID:"));
        inputPanel.add(orderIdToUpdateField);
        
        statusUpdateCombo = new JComboBox<>(new String[]{
            "Picked Up", 
            "In Transit", 
            "Attempted Delivery", 
            "Delivered (COD required)",
            "Delivered (Paid)"
        });
        
        JButton updateButton = new JButton("Update Delivery Status");
        updateButton.setBackground(AppColors.PRIMARY_BLUE);
        updateButton.setForeground(AppColors.BACKGROUND_WHITE);
        updateButton.addActionListener(e -> handleStatusUpdate());

        updateMessageLabel = new JLabel(" ");
        updateMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateMessageLabel.setFont(new Font("Arial", Font.BOLD, 12));

        updatePanel.add(inputPanel);
        updatePanel.add(statusUpdateCombo);
        updatePanel.add(updateButton);
        
        panel.add(updatePanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadDeliveryDetails(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        
        if (order == null) {
            deliveryDetailsArea.setText("Error: Order details not found.");
            return;
        }
        
        String details = String.format(
            "Order ID: %d\n" +
            "Agent ID: %d\n" +
            "Customer ID: %d\n" +
            "Source: %s\n" +
            "Destination: %s\n" +
            "Type: %s\n" +
            "Status: %s\n" +
            "Payment Status: %s\n" +
            "--- ACTION ---\n" +
            "Required: Change status to 'Picked Up' after loading cargo.",
            order.getOrderID(),
            agent.getUserID(),
            order.getUserID(),
            order.getSourceAddress(),
            order.getDestinationAddress(),
            order.getOrderType(),
            order.getStatus(),
            order.getPaymentStatus()
        );
        deliveryDetailsArea.setText(details);
        orderIdToUpdateField.setText(String.valueOf(orderId));
        updateMessageLabel.setText("Ready to update Order " + orderId);
        updateMessageLabel.setForeground(AppColors.TEXT_BLACK);
    }
    
    private void handleStatusUpdate() {
        try {
            int orderId = Integer.parseInt(orderIdToUpdateField.getText().trim());
            String newStatus = (String) statusUpdateCombo.getSelectedItem();
            
            // This method call handles the status update and tracking record insertion via OrderService/DAO
            boolean success = orderService.agentAcceptsDelivery(orderId, agent.getUserID());
            
            if (success) {
                // Since agentAcceptsDelivery only handles the initial acceptance, 
                // we'll update the status directly via the DAO/service call here for subsequent status changes.
                // Assuming OrderService has a generic update method, or use the Agent's method wrapper:
                agent.updateStatus(orderId, newStatus); 
                
                updateMessageLabel.setText("Order " + orderId + " status updated to: " + newStatus);
                updateMessageLabel.setForeground(AppColors.ACCENT_GREEN);
                loadAssignedOrders(); // Refresh the list
            } else {
                updateMessageLabel.setText("Update failed! Order not assigned to you or invalid.");
                updateMessageLabel.setForeground(AppColors.WARNING_RED);
            }
        } catch (NumberFormatException e) {
            updateMessageLabel.setText("Invalid Order ID.");
            updateMessageLabel.setForeground(AppColors.WARNING_RED);
        }
    }
}
