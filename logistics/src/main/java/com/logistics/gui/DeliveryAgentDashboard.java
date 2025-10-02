package com.logistics.gui;

import com.logistics.models.DeliveryAgent;
import com.logistics.services.OrderService;
import com.logistics.utils.AppColors;

import javax.swing.*;
import java.awt.*;

/**
 * The main dashboard for the Delivery Agent role.
 * Includes panels for assigned deliveries, status updates, and history.
 */
public class DeliveryAgentDashboard extends JFrame {

    private final DeliveryAgent agent;
    private final OrderService orderService;

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

        // --- Frame Setup ---
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND_WHITE);

        // --- Header ---
        add(createHeaderPanel(), BorderLayout.NORTH);

        // --- Main Content: Split Pane (Matching wireframe style) ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createAssignedOrdersPanel(),
                createDeliveryDetailsPanel());
        mainSplit.setDividerLocation(300);
        add(mainSplit, BorderLayout.CENTER);

        // Initialize Data (Simulated list for demonstration)
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
    // LEFT PANEL: Assigned Deliveries & History (Matching wireframe)
    // ==========================================================
    private JPanel createAssignedOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        [cite_start]// --- Assigned Deliveries List (Matching 'Assigned Deliveries' section) [cite: 142] ---
        assignedOrdersList = new JList<>();
        assignedOrdersList.setFont(new Font("Arial", Font.PLAIN, 14));
        assignedOrdersList.setBorder(BorderFactory.createTitledBorder("Assigned Deliveries"));
        
        // Listener to show details when an item is clicked
        assignedOrdersList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && assignedOrdersList.getSelectedIndex() != -1) {
                String selectedOrder = assignedOrdersList.getSelectedValue();
                int orderId = extractOrderId(selectedOrder);
                loadDeliveryDetails(orderId);
            }
        });

        panel.add(new JScrollPane(assignedOrdersList), BorderLayout.CENTER);
        
        [cite_start]// --- History Button (Matching 'History' section) [cite: 145] ---
        JButton historyButton = new JButton("View Delivery History");
        historyButton.setBackground(AppColors.PRIMARY_BLUE);
        historyButton.setForeground(AppColors.BACKGROUND_WHITE);
        historyButton.addActionListener(e -> {
            agent.maintainDeliveryRecords(); // Log action
            JOptionPane.showMessageDialog(this, "Fetching historical records for Agent " + agent.getUserID());
        });
        panel.add(historyButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAssignedOrders() {
        // SIMULATION: In a real system, you'd call userDAO.getAssignedOrders(agent.getUserID());
        // For demonstration, we simulate data:
        String[] orders = {
            "ID: 101 - Pickup: Warehouse A - Status: Assigned",
            "ID: 105 - Pickup: Customer Z - Status: En Route",
            "ID: 109 - Pickup: Retailer B - Status: Pending Acceptance"
        };
        assignedOrdersList.setListData(orders);
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
    // RIGHT PANEL: Delivery Details & Status Update (Matching wireframe fields)
    // ==========================================================
    private JPanel createDeliveryDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // --- Details Area ---
        deliveryDetailsArea = new JTextArea("Select an assigned delivery on the left to view details...");
        deliveryDetailsArea.setEditable(false);
        deliveryDetailsArea.setBorder(BorderFactory.createTitledBorder("Delivery Information"));
        panel.add(new JScrollPane(deliveryDetailsArea), BorderLayout.CENTER);
        
        [cite_start]// --- Status Update Panel (Matching fields: Status, Update Status) [cite: 144, 146] ---
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
        // SIMULATION: In a real system, you'd call orderDAO.getOrderById(orderId);
        String details = String.format(
            "Order ID: %d\n" +
            [cite_start]"Agent ID: %d\n" + // Matching 'ID' field in wireframe [cite: 141]
            [cite_start]"Address: [Placeholder Address]\n" + // Matching 'Address' field [cite: 143]
            "Vehicle ID: V-404\n" +
            "Pickup: 123 Main St, Warehouse A\n" +
            "Drop-off: 456 Oak Ave, Customer Home\n" +
            "Status: Assigned - Ready to Move\n" +
            "--- ACTION ---\n" +
            "Required: Pick up cargo, then update status.",
            orderId,
            agent.getUserID()
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
            
            // Call OrderService to update the status and tracking record
            // For simulation, we assume agentAcceptsDelivery can be reused for any status update
            boolean success = orderService.agentAcceptsDelivery(orderId, agent.getUserID());
            
            if (success) {
                // Log and refresh UI message
                updateMessageLabel.setText("Order " + orderId + " status updated to: " + newStatus);
                updateMessageLabel.setForeground(AppColors.ACCENT_GREEN);
                agent.updateStatus(orderId, newStatus); // Log action in console
            } else {
                updateMessageLabel.setText("Update failed! Check if Order ID is valid.");
                updateMessageLabel.setForeground(AppColors.WARNING_RED);
            }
        } catch (NumberFormatException e) {
            updateMessageLabel.setText("Invalid Order ID.");
            updateMessageLabel.setForeground(AppColors.WARNING_RED);
        }
    }
}
