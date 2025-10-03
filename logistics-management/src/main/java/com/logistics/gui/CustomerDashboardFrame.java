package com.logistics.gui;

import com.logistics.models.Order;
import com.logistics.models.Product;
import com.logistics.models.User;
import com.logistics.services.OrderService;
import com.logistics.services.ProductService;

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

    public CustomerDashboardFrame(User user, OrderService orderService, ProductService productService) {
        this.customerUser = user;
        this.orderService = orderService;
        this.productService = productService;

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
        ordersTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton placeOrderButton = new JButton("Place New Order");
        bottomPanel.add(placeOrderButton);

        add(welcomeLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        placeOrderButton.addActionListener(e -> showPlaceOrderDialog());

        loadCustomerOrders();
    }

    private void loadCustomerOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderService.getOrdersForCustomer(customerUser.getUserId());
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

    private void showPlaceOrderDialog() {
        List<Product> availableProducts = productService.getAvailableProducts();
        if (availableProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sorry, no products are available to order at this time.", "No Products", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create components for the dialog
        JComboBox<Product> productComboBox = new JComboBox<>(availableProducts.toArray(new Product));
        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) {
                    setText(((Product) value).getName());
                }
                return this;
            }
        });

        JTextField sourceField = new JTextField(customerUser.getAddress());
        JTextField destinationField = new JTextField();

        // Layout the components in a panel
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Select Product:"));
        panel.add(productComboBox);
        panel.add(new JLabel("Source Address:"));
        panel.add(sourceField);
        panel.add(new JLabel("Destination Address:"));
        panel.add(destinationField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Place New Order",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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
            boolean success = orderService.createNewOrder(newOrder);

            if (success) {
                JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCustomerOrders(); // Refresh the table to show the new order
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place the order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
