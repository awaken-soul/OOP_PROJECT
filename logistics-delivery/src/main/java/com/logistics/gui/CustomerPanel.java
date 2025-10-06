package com.logistics.ui;

import com.logistics.model.Customer;
import com.logistics.model.Shipment;
import com.logistics.service.CustomerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * JPanel for the Customer dashboard.
 * Provides UI for placing new orders and viewing past orders.
 */
public class CustomerPanel extends JPanel {

    private final CustomerService customerService;
    private final Customer currentUser;

    // UI Components for placing an order
    private final JTextField productNameField = new JTextField(20);
    private final JTextField descriptionField = new JTextField(20);
    private final JTextField sourceField = new JTextField(20);
    private final JTextField destinationField = new JTextField(20);
    private final JButton placeOrderButton = new JButton("Place Shipment Request");

    // UI Components for viewing past orders
    private final DefaultListModel<Shipment> shipmentsModel = new DefaultListModel<>();
    private final JList<Shipment> shipmentsList = new JList<>(shipmentsModel);

    public CustomerPanel(CustomerService customerService, Customer currentUser) {
        this.customerService = customerService;
        this.currentUser = currentUser;
        initUI();
        attachListeners();
        loadShipments();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Customer Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);

        // Split pane to separate the form from the list
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                createOrderPanel(),
                createShipmentsListPanel());
        splitPane.setResizeWeight(0.3); // Give less space to the form initially

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Place a New Order"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add labels and fields
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        panel.add(productNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Source Address:"), gbc);
        gbc.gridx = 1;
        panel.add(sourceField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Destination Address:"), gbc);
        gbc.gridx = 1;
        panel.add(destinationField, gbc);

        // Add button
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(placeOrderButton, gbc);

        return panel;
    }

    private JPanel createShipmentsListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("My Shipments"));
        shipmentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(shipmentsList), BorderLayout.CENTER);
        return panel;
    }

    private void attachListeners() {
        placeOrderButton.addActionListener(this::handlePlaceOrder);
    }

    private void handlePlaceOrder(ActionEvent e) {
        String productName = productNameField.getText();
        String description = descriptionField.getText();
        String source = sourceField.getText();
        String destination = destinationField.getText();

        if (productName.isEmpty() || source.isEmpty() || destination.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product Name, Source, and Destination cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = customerService.placeShipmentRequest(productName, description, source, destination, currentUser.getId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Shipment request placed successfully!");
            // Clear fields
            productNameField.setText("");
            descriptionField.setText("");
            sourceField.setText("");
            destinationField.setText("");
            // Refresh the list to show the new shipment
            loadShipments();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to place shipment request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadShipments() {
        shipmentsModel.clear();
        List<Shipment> myShipments = customerService.getMyShipments(currentUser.getId());
        if (myShipments != null) {
            for (Shipment s : myShipments) {
                shipmentsModel.addElement(s);
            }
        }
    }
}

