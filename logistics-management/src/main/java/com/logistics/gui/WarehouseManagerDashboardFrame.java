package com.logistics.gui;

import com.logistics.models.Order;
import com.logistics.models.Product;
import com.logistics.models.Tracking;
import com.logistics.models.User;
import com.logistics.models.Vehicle;
import com.logistics.services.OrderService;
import com.logistics.services.ProductService;
import com.logistics.services.TrackingService;
import com.logistics.services.UserService;
import com.logistics.services.VehicleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WarehouseManagerDashboardFrame extends JFrame {

    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private JTable stockTable;
    private DefaultTableModel stockTableModel;

    private OrderService orderService;
    private UserService userService;
    private VehicleService vehicleService;
    private ProductService productService;
    private TrackingService trackingService;

    public WarehouseManagerDashboardFrame(User managerUser, OrderService orderService, UserService userService,
                                          VehicleService vehicleService, ProductService productService,
                                          TrackingService trackingService) {
        this.orderService = orderService;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.productService = productService;
        this.trackingService = trackingService;

        setTitle("Warehouse Manager Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        JPanel pendingOrdersPanel = createPendingOrdersPanel();
        splitPane.setTopComponent(pendingOrdersPanel);

        JPanel stockLevelsPanel = createStockLevelsPanel();
        splitPane.setBottomComponent(stockLevelsPanel);

        add(splitPane, BorderLayout.CENTER);

        loadPendingOrders();
        loadStockLevels();
    }

    private JPanel createPendingOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Pending Orders for Assignment"));

        String[] columnNames = {"Order ID", "Customer ID", "Product ID", "Destination"};
        ordersTableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton assignOrderButton = new JButton("Assign Selected Order");
        JButton trackOrderButton = new JButton("Track Selected Order");
        buttonPanel.add(assignOrderButton);
        buttonPanel.add(trackOrderButton);

        assignOrderButton.addActionListener(e -> showAssignOrderDialog());
        trackOrderButton.addActionListener(e -> showTrackingHistory());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStockLevelsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Stock Levels"));

        String[] columnNames = {"Product ID", "Product Name", "Quantity", "Status"};
        stockTableModel = new DefaultTableModel(columnNames, 0);
        stockTable = new JTable(stockTableModel);
        stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(stockTable);

        JButton updateStockButton = new JButton("Update Selected Stock");
        updateStockButton.addActionListener(e -> updateSelectedStock());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(updateStockButton, BorderLayout.SOUTH);
        return panel;
    }

    private void showTrackingHistory() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to track.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer orderId = (Integer) ordersTable.getValueAt(selectedRow, 0);
        List<Tracking> history = trackingService.getTrackingHistoryForOrder(orderId);

        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tracking history found for this order.", "No History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        TrackingHistoryDialog dialog = new TrackingHistoryDialog(this, orderId, history);
        dialog.setVisible(true);
    }

    private void loadPendingOrders() {
        ordersTableModel.setRowCount(0);
        List<Order> pendingOrders = orderService.getOrdersByStatus("Pending");
        for (Order order : pendingOrders) {
            ordersTableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getUserId(),
                order.getProductId(),
                order.getDestinationAddress()
            });
        }
    }

    private void loadStockLevels() {
        stockTableModel.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            String status = product.getQuantity() > 10 ? "In Stock" :
                            (product.getQuantity() > 0 ? "Low Stock" : "Out of Stock");
            stockTableModel.addRow(new Object[]{
                product.getProductId(),
                product.getName(),
                product.getQuantity(),
                status
            });
        }
    }

    private void updateSelectedStock() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer productId = (Integer) stockTable.getValueAt(selectedRow, 0);
        String productName = (String) stockTable.getValueAt(selectedRow, 1);
        String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new quantity for " + productName + ":", "Update Stock", JOptionPane.PLAIN_MESSAGE);

        if (newQuantityStr != null && !newQuantityStr.trim().isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(newQuantityStr.trim());
                if (productService.updateProductQuantity(productId, newQuantity)) {
                    JOptionPane.showMessageDialog(this, "Stock updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadStockLevels();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update stock (quantity cannot be negative).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAssignOrderDialog() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to assign.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer orderId = (Integer) ordersTable.getValueAt(selectedRow, 0);
        List<User> availableAgents = userService.getAvailableAgents();
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();

        if (availableAgents.isEmpty() || availableVehicles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available agents or vehicles to assign.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<User> agentComboBox = new JComboBox<>(availableAgents.toArray(new User[0]));
        agentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) setText(((User) value).getName());
                return this;
            }
        });

        JComboBox<Vehicle> vehicleComboBox = new JComboBox<>(availableVehicles.toArray(new Vehicle[0]));
        vehicleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vehicle) setText(((Vehicle) value).getLicensePlate());
                return this;
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Select Agent:"));
        panel.add(agentComboBox);
        panel.add(new JLabel("Select Vehicle:"));
        panel.add(vehicleComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Assign Order ID: " + orderId,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            User selectedAgent = (User) agentComboBox.getSelectedItem();
            Vehicle selectedVehicle = (Vehicle) vehicleComboBox.getSelectedItem();

            if (selectedAgent != null && selectedVehicle != null) {
                if (orderService.assignOrderToAgent(orderId, selectedAgent.getUserId(), selectedVehicle.getVehicleId())) {
                    JOptionPane.showMessageDialog(this, "Order assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadPendingOrders();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to assign the order.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
