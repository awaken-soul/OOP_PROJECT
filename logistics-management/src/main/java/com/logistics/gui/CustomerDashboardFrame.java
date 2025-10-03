package com.logistics.gui;

import com.logistics.models.*;
import com.logistics.services.ComplaintService;
import com.logistics.services.OrderService;
import com.logistics.services.ProductService;
import com.logistics.services.TrackingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerDashboardFrame extends JFrame {

    private final JTable ordersTable;
    private final DefaultTableModel tableModel;
    private final User customerUser;
    private final OrderService orderService;
    private final ProductService productService;
    private final TrackingService trackingService;
    private final ComplaintService complaintService; // New service

    public CustomerDashboardFrame(User user, OrderService orderService, ProductService productService, TrackingService trackingService, ComplaintService complaintService) {
        this.customerUser = user;
        this.orderService = orderService;
        this.productService = productService;
        this.trackingService = trackingService;
        this.complaintService = complaintService; // Store the service

        setTitle("Customer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("My Orders"));

        String columnNames = {"Order ID", "Destination", "Status", "Last Updated"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = new JTable(tableModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton placeOrderButton = new JButton("Place New Order");
        JButton trackOrderButton = new JButton("Track Selected Order");
        JButton fileComplaintButton = new JButton("File a Complaint"); // New button
        bottomPanel.add(placeOrderButton);
        bottomPanel.add(trackOrderButton);
        bottomPanel.add(fileComplaintButton);

        add(welcomeLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        placeOrderButton.addActionListener(e -> showPlaceOrderDialog());
        trackOrderButton.addActionListener(e -> showTrackingHistory());
        fileComplaintButton.addActionListener(e -> showComplaintDialog()); // Add action listener

        loadCustomerOrders();
    }

    private void showComplaintDialog() {
        JTextField subjectField = new JTextField();
        JTextArea descriptionArea = new JTextArea(5, 30);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.add(new JLabel("Subject:"));
        formPanel.add(subjectField);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JLabel("Description:"), BorderLayout.CENTER);
        panel.add(new JScrollPane(descriptionArea), BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, panel, "File a Complaint", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String subject = subjectField.getText();
            String description = descriptionArea.getText();

            if (subject.trim().isEmpty() |

| description.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Subject and Description cannot be empty.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // For simplicity, we are not linking to a specific order, but this could be added.
            Complaint newComplaint = new Complaint(customerUser.getUserId(), null, subject, description);
            if (complaintService.submitComplaint(newComplaint)) {
                JOptionPane.showMessageDialog(this, "Complaint submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit complaint.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadCustomerOrders() {
        //... (existing method, no changes needed)
        tableModel.setRowCount(0);
        List<Order> orders = orderService.getOrdersForCustomer(customerUser.getUserId());
        for (Order order : orders) {
            tableModel.addRow(new Object{order.getOrderId(), order.getDestinationAddress(), order.getStatus(), order.getUpdatedAt()});
        }
    }

    private void showTrackingHistory() {
        //... (existing method, no changes needed)
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to track.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer orderId = (Integer) ordersTable.getValueAt(selectedRow, 0);
        List<Tracking> history = trackingService.getTrackingHistoryForOrder(orderId);
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tracking history found for this order yet.", "No History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TrackingHistoryDialog dialog = new TrackingHistoryDialog(this, orderId, history);
        dialog.setVisible(true);
    }

    private void showPlaceOrderDialog() {
        //... (existing method, no changes needed)
        List<Product> availableProducts = productService.getAvailableProducts();
        if (availableProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sorry, no products are available to order at this time.", "No Products", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JComboBox<Product> productComboBox = new JComboBox<>(availableProducts.toArray(new Product));
        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) setText(((Product) value).getName());
                return this;
            }
        });
        JTextField sourceField = new JTextField(customerUser.getAddress());
        JTextField destinationField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Select Product:"));
        panel.add(productComboBox);
        panel.add(new JLabel("Source Address:"));
        panel.add(sourceField);
        panel.add(new JLabel("Destination Address:"));
        panel.add(destinationField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Place New Order", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            String source = sourceField.getText();
            String destination = destinationField.getText();
            if (selectedProduct == null |

| destination.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a product and enter a destination address.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Order newOrder = new Order(customerUser.getUserId(), selectedProduct.getProductId(), "Purchase", source, destination);
            if (orderService.createNewOrder(newOrder)) {
                JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCustomerOrders();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place the order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
