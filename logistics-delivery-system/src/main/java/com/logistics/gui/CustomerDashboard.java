package com.logistics.gui;

import com.logistics.models.Customer;
import com.logistics.models.Order;
import com.logistics.services.OrderService;
import com.logistics.services.TrackingService;
import com.logistics.utils.AppColors;
import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JFrame {

    private final Customer customer;
    private final OrderService orderService;
    private final TrackingService trackingService;
    private JComboBox<String> requestTypeCombo;
    private JTextField pickupField;
    private JTextField deliveryField;
    private JTextField productDetailsField;
    private JLabel requestMessageLabel;
    private JTextField trackingIdField;
    private JTextArea trackingResultArea;

    public CustomerDashboard(Customer customer) {
        super("Customer Dashboard - Welcome, " + customer.getName());
        this.customer = customer;
        this.orderService = new OrderService();
        this.trackingService = new TrackingService();
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND_WHITE);
        add(createHeaderPanel(), BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("Place New Request", createRequestPanel());
        tabbedPane.addTab("Track Existing Order", createTrackingPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.PRIMARY_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Customer Portal: " + customer.getName());
        titleLabel.setForeground(AppColors.BACKGROUND_WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(AppColors.WARNING_RED);
        logoutButton.setForeground(AppColors.BACKGROUND_WHITE);
        logoutButton.addActionListener(e -> {
            customer.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        return headerPanel;
    }
    
    private JPanel createRequestPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("New Logistics Request");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(AppColors.TEXT_BLACK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        panel.add(new JLabel("Request Type:"), gbc);
        requestTypeCombo = new JComboBox<>(new String[]{"Shipment", "Purchase", "Transport"});
        requestTypeCombo.setBackground(AppColors.BACKGROUND_WHITE);
        gbc.gridx = 1;
        panel.add(requestTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Pickup Address (Source):"), gbc);
        pickupField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(pickupField, gbc);

     
        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Delivery Address (Destination):"), gbc);
        deliveryField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(deliveryField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Product/Cargo Details:"), gbc);
        productDetailsField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(productDetailsField, gbc);

        JButton submitButton = new JButton("Submit Request");
        submitButton.setBackground(AppColors.ACCENT_GREEN);
        submitButton.setForeground(AppColors.BACKGROUND_WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(e -> handlePlaceRequest());
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.ipady = 8;
        panel.add(submitButton, gbc);
        
        requestMessageLabel = new JLabel(" ");
        requestMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++; gbc.ipady = 0;
        panel.add(requestMessageLabel, gbc);

        return panel;
    }

    private void handlePlaceRequest() {
        String type = (String) requestTypeCombo.getSelectedItem();
        String pickup = pickupField.getText().trim();
        String delivery = deliveryField.getText().trim();
        String details = productDetailsField.getText().trim();

        if (pickup.isEmpty() || delivery.isEmpty() || details.isEmpty()) {
            requestMessageLabel.setText("All fields are required to place a request.");
            requestMessageLabel.setForeground(AppColors.WARNING_RED);
            return;
        }
        
        Order newOrder = new Order(
            customer.getUserID(),
            null, 
            type,
            pickup,
            delivery
        );
        
        Order placedOrder = orderService.placeNewRequest(newOrder);

        if (placedOrder != null) {
            requestMessageLabel.setText("Request successful! Order ID: " + placedOrder.getOrderID() + ". Agent assignment in progress.");
            requestMessageLabel.setForeground(AppColors.PRIMARY_BLUE);
            pickupField.setText("");
            deliveryField.setText("");
            productDetailsField.setText("");
        } else {
            requestMessageLabel.setText("Error placing request. Check system logs.");
            requestMessageLabel.setForeground(AppColors.WARNING_RED);
        }
    }
    
    private JPanel createTrackingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.BACKGROUND_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setBackground(AppColors.SECONDARY_GRAY);
        searchPanel.setBorder(BorderFactory.createLineBorder(AppColors.SECONDARY_GRAY.darker()));

        searchPanel.add(new JLabel("Enter Order ID:"));
        trackingIdField = new JTextField(15);
        searchPanel.add(trackingIdField);

        JButton trackButton = new JButton("Track Shipment");
        trackButton.setBackground(AppColors.PRIMARY_BLUE);
        trackButton.setForeground(AppColors.BACKGROUND_WHITE);
        trackButton.addActionListener(e -> handleTrackOrder());
        searchPanel.add(trackButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        trackingResultArea = new JTextArea("Tracking results will appear here...\n\n(Example: 1 -> Pending, 2 -> Out for Delivery)");
        trackingResultArea.setEditable(false);
        trackingResultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(trackingResultArea);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void handleTrackOrder() {
        String idText = trackingIdField.getText().trim();
        if (idText.isEmpty()) {
            trackingResultArea.setText("Please enter a valid Order ID.");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(idText);
            String history = trackingService.getFormattedTrackingHistory(orderId);
            trackingResultArea.setText(history);
            
        } catch (NumberFormatException ex) {
            trackingResultArea.setText("Invalid input. Order ID must be a number.");
        }
    }
}
