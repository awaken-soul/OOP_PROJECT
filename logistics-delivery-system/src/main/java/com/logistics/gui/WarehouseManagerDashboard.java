package com.logistics.gui;

import com.logistics.models.Inventory;
import com.logistics.models.WarehouseManager;
import com.logistics.services.InventoryService;
import com.logistics.services.TrackingService;
import com.logistics.utils.AppColors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WarehouseManagerDashboard extends JFrame {

    private final WarehouseManager manager;
    private final InventoryService inventoryService;
    private final TrackingService trackingService;
    private static final int WAREHOUSE_ID = 10;
    private JTable stockTable;
    private DefaultTableModel stockTableModel;
    private JTextField assignOrderIdField;
    private JTextField assignProductIdField;
    private JTextField assignQuantityField;
    private JLabel assignMessageLabel;

    public WarehouseManagerDashboard(WarehouseManager manager) {
        super("Warehouse Manager Dashboard - Profile: " + manager.getName());
        this.manager = manager;
        this.inventoryService = new InventoryService();
        this.trackingService = new TrackingService();

        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BACKGROUND_WHITE);

        add(createHeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("Stock Levels", createStockLevelsPanel());
        tabbedPane.addTab("Assign Products & Dispatch", createAssignmentPanel());
        tabbedPane.addTab("Track Outbound Shipments", createTrackingPanel());

        add(tabbedPane, BorderLayout.CENTER);

        loadStockData();

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.PRIMARY_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("WAREHOUSE MANAGER: " + manager.getName() + " (WH-ID: " + WAREHOUSE_ID + ")");
        titleLabel.setForeground(AppColors.BACKGROUND_WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(AppColors.WARNING_RED);
        logoutButton.setForeground(AppColors.BACKGROUND_WHITE);
        logoutButton.addActionListener(e -> {
            manager.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createStockLevelsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columnNames = {"Product ID", "Name", "Stock Level", "Price", "Retailer ID", "Status"};
        stockTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        stockTable = new JTable(stockTableModel);
        stockTable.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Current Stock Levels"));

        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Stock Data");
        refreshButton.setBackground(AppColors.PRIMARY_BLUE);
        refreshButton.setForeground(AppColors.BACKGROUND_WHITE);
        refreshButton.addActionListener(e -> loadStockData());

        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadStockData() {

        stockTableModel.setRowCount(0);

        List<Inventory> inventoryList = inventoryService.getWarehouseStock(WAREHOUSE_ID);

        if (inventoryList.isEmpty()) {

            Inventory simProduct1 = new Inventory("Laptop", "Business laptop", 1200.00, 50, WAREHOUSE_ID, 201);
            Inventory simProduct2 = new Inventory("Shipping Box", "Standard cardboard box", 2.50, 500, WAREHOUSE_ID, 202);
            stockTableModel.addRow(new Object[]{1001, simProduct1.getName(), 35, "$1200.00", 201, "Low Stock"});
            stockTableModel.addRow(new Object[]{1002, simProduct2.getName(), 450, "$2.50", 202, "Good"});
        } else {
            for (Inventory item : inventoryList) {
                String status = item.getStockLevel() < 50 ? "Low" : "Good";
                stockTableModel.addRow(new Object[]{
                    item.getProductID(),
                    item.getName(),
                    item.getStockLevel(),
                    "$" + String.format("%.2f", item.getPrice()),
                    item.getRetailerID(),
                    status
                });
            }
        }

        manager.monitorStock();
    } // <--- CORRECT CLOSING BRACE ADDED HERE

    private JPanel createAssignmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Dispatch Product for Delivery"));

        form.add(new JLabel("Order ID to Dispatch:"));
        assignOrderIdField = new JTextField();
        form.add(assignOrderIdField);

        form.add(new JLabel("Product ID (from Stock):"));
        assignProductIdField = new JTextField();
        form.add(assignProductIdField);

        form.add(new JLabel("Quantity to Assign:"));
        assignQuantityField = new JTextField();
        form.add(assignQuantityField);

        JButton dispatchButton = new JButton("Confirm Dispatch & Reduce Stock");
        dispatchButton.setBackground(AppColors.ACCENT_GREEN);
        dispatchButton.setForeground(AppColors.BACKGROUND_WHITE);
        dispatchButton.addActionListener(e -> handleProductAssignment());
        form.add(dispatchButton);

        assignMessageLabel = new JLabel(" ");
        assignMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        form.add(assignMessageLabel);

        panel.add(form, BorderLayout.NORTH);

        JTextArea statusArea = new JTextArea("Inbound Shipments (Needs Storage):\n- Shipment 801 (Truck 123) - Waiting Unload\n\nOutbound Queue (Ready to Ship):\n- Order 505 (Agent 105) - Ready for Pickup");
        statusArea.setBorder(BorderFactory.createTitledBorder("Warehouse Queue Status"));
        statusArea.setEditable(false);
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);

        return panel;
    }

    private void handleProductAssignment() {
        try {
            int orderId = Integer.parseInt(assignOrderIdField.getText().trim());
            int productId = Integer.parseInt(assignProductIdField.getText().trim());
            int quantity = Integer.parseInt(assignQuantityField.getText().trim());

            if (quantity <= 0) throw new NumberFormatException();

            boolean success = inventoryService.assignProductToOrder(orderId, productId, quantity);

            if (success) {
                manager.assignProducts(WAREHOUSE_ID, productId);

                assignMessageLabel.setText("Product ID " + productId + " assigned to Order " + orderId + ". Stock reduced.");
                assignMessageLabel.setForeground(AppColors.ACCENT_GREEN);

                loadStockData(); // Refresh stock view

                assignOrderIdField.setText("");
                assignProductIdField.setText("");
                assignQuantityField.setText("");
            } else {
                assignMessageLabel.setText("Assignment failed! Check stock/order status.");
                assignMessageLabel.setForeground(AppColors.WARNING_RED);
            }

        } catch (NumberFormatException e) {
            assignMessageLabel.setText("Invalid input: All fields must be positive numbers.");
            assignMessageLabel.setForeground(AppColors.WARNING_RED);
        }
    }

    private JPanel createTrackingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.BACKGROUND_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchPanel.setBackground(AppColors.SECONDARY_GRAY);
        searchPanel.setBorder(BorderFactory.createLineBorder(AppColors.SECONDARY_GRAY.darker()));

        searchPanel.add(new JLabel("Order ID to Track (Warehouse Focus):"));
        JTextField trackIdField = new JTextField(15);
        searchPanel.add(trackIdField);

        JButton trackButton = new JButton("Track Shipment");
        trackButton.setBackground(AppColors.PRIMARY_BLUE);
        trackButton.setForeground(AppColors.BACKGROUND_WHITE);

        JTextArea trackResultArea = new JTextArea("Tracking results will appear here...");
        trackResultArea.setEditable(false);
        trackResultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        trackButton.addActionListener(e -> {
            String idText = trackIdField.getText().trim();
            try {
                int orderId = Integer.parseInt(idText);
                String history = trackingService.getFormattedTrackingHistory(orderId);
                trackResultArea.setText(history);
            } catch (NumberFormatException ex) {
                trackResultArea.setText("Invalid input. Order ID must be a number.");
            }
        });

        searchPanel.add(trackButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(trackResultArea), BorderLayout.CENTER);
        return panel;
    }
}
