package com.logistics.gui;

import com.logistics.models.DeliveryAgent;
import com.logistics.models.Order;
import com.logistics.services.OrderService;
import com.logistics.utils.AppColors;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeliveryAgentDashboard extends JFrame {

    private final DeliveryAgent agent;
    private final OrderService orderService;
    
    private JList<String> assignedOrdersList;
    private JTextArea deliveryDetailsArea;
    private JComboBox<String> statusUpdateCombo;
    private JTextField orderIdToUpdateField;
    private JLabel updateMessageLabel;

    public DeliveryAgentDashboard(DeliveryAgent agent) {
        super("Delivery Agent Dashboard - Profile: " + agent.getName());
        this.agent = agent;
        this.orderService = new OrderService();

      
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND_WHITE);

        add(createHeaderPanel(), BorderLayout.NORTH);

   
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createAssignedOrdersPanel(),
                createDeliveryDetailsPanel());
        mainSplit.setDividerLocation(300);
        add(mainSplit, BorderLayout.CENTER);

        loadAssignedOrders();
        
        setVisible(true);
    }
    
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

        String[] orders = {
            "ID: 101 - Pickup: Warehouse A - Status: Assigned",
            "ID: 105 - Pickup: Customer Z - Status: En Route",
            "ID: 109 - Pickup: Retailer B - Status: Pending Acceptance"
        };
        assignedOrdersList.setListData(orders);
    }
    
    private int extractOrderId(String orderString) {
        try {
            return Integer.parseInt(orderString.substring(4, orderString.indexOf(" - ")));
        } catch (Exception e) {
            return -1;
        }
    }

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
        String details = String.format(
            "Order ID: %d\n" +
            "Agent ID: %d\n" +
            "Address: [Placeholder Address]\n" +
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
            
            boolean success = orderService.agentAcceptsDelivery(orderId, agent.getUserID());
            
            if (success) {
                updateMessageLabel.setText("Order " + orderId + " status updated to: " + newStatus);
                updateMessageLabel.setForeground(AppColors.ACCENT_GREEN);
                agent.updateStatus(orderId, newStatus);
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
