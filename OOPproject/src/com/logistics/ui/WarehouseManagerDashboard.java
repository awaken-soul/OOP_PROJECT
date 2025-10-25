package com.logistics.ui;

import com.logistics.model.*;
import com.logistics.service.ManagerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WarehouseManagerDashboard extends JFrame {

    private final int managerId;
    private final ManagerService managerService = new ManagerService();

    private JComboBox<Warehouse> warehouseComboBox;
    private JTable ordersTable;
    private JTable inventoryTable;

    public WarehouseManagerDashboard(int managerId) {
        this.managerId = managerId;

        setTitle("Warehouse Manager Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JLabel header = new JLabel("Warehouse Manager Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // ===== MAIN SPLIT PANEL =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        splitPane.setTopComponent(topPanel);

        JPanel warehousePanel = new JPanel();
        warehousePanel.add(new JLabel("Select Warehouse:"));
        warehouseComboBox = new JComboBox<>();
        warehousePanel.add(warehouseComboBox);
        JButton viewOrdersBtn = new JButton("View Orders");
        JButton displayStockBtn = new JButton("Display Current Stock");
        warehousePanel.add(viewOrdersBtn);
        warehousePanel.add(displayStockBtn);
        topPanel.add(warehousePanel, BorderLayout.NORTH);

        ordersTable = new JTable(new DefaultTableModel(
                new String[]{"Order ID", "Customer", "Product", "Status", "Source", "Destination"}, 0
        ));
        topPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        JPanel assignPanel = new JPanel();
        JButton assignAgentBtn = new JButton("Assign Agent");
        assignPanel.add(assignAgentBtn);
        topPanel.add(assignPanel, BorderLayout.SOUTH);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        splitPane.setBottomComponent(bottomPanel);

        JLabel invLabel = new JLabel("Warehouse Inventory", SwingConstants.CENTER);
        invLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomPanel.add(invLabel, BorderLayout.NORTH);

        inventoryTable = new JTable(new DefaultTableModel(
                new String[]{"Product ID", "Name", "Description", "Price", "Quantity"}, 0
        ));
        bottomPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JPanel stockPanel = new JPanel();
        JButton updateStockBtn = new JButton("Update Stock");
        stockPanel.add(updateStockBtn);
        bottomPanel.add(stockPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====
        viewOrdersBtn.addActionListener(e -> loadOrders());
        displayStockBtn.addActionListener(e -> loadInventoryForSelectedWarehouse());
        assignAgentBtn.addActionListener(e -> assignAgent());
        updateStockBtn.addActionListener(e -> updateStock());

        loadWarehouses();
        setVisible(true);
    }

    // Overloaded constructor — used by LoginPage after login
    public WarehouseManagerDashboard(User user) {
        this(user.getUserId());
    }

    /** Load warehouses for this manager **/
    private void loadWarehouses() {
        List<Warehouse> warehouses = managerService.getWarehousesByManager(managerId);
        warehouseComboBox.removeAllItems();

        for (Warehouse w : warehouses) {
            System.out.println("Manager ID: " + managerId + " | Warehouse manager_id: " + w.getManagerId());
            if (w.getManagerId() == managerId) {
                warehouseComboBox.addItem(w);
            }
        }

        if (warehouseComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No warehouses found for this manager!");
        }
    }

    /** Load orders for selected warehouse **/
    private void loadOrders() {
        Warehouse selectedWarehouse = (Warehouse) warehouseComboBox.getSelectedItem();
        if (selectedWarehouse == null) {
            JOptionPane.showMessageDialog(this, "Select a warehouse first!");
            return;
        }

        List<Order> orders = managerService.getOrdersForManager(managerId);
        DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
        model.setRowCount(0);

        for (Order o : orders) {
            if (o.getWarehouseName() != null &&
                    o.getWarehouseName().equalsIgnoreCase(selectedWarehouse.getName())) {
                model.addRow(new Object[]{
                        o.getOrderId(),
                        o.getCustomerName(),
                        o.getProductName(),
                        o.getStatus(),
                        o.getSourceAddress(),
                        o.getDestinationAddress()
                });
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No orders found for this warehouse.");
        }
    }

    /** Display stock for selected warehouse **/
    private void loadInventoryForSelectedWarehouse() {
        Warehouse selectedWarehouse = (Warehouse) warehouseComboBox.getSelectedItem();
        if (selectedWarehouse == null) {
            JOptionPane.showMessageDialog(this, "Select a warehouse first!");
            return;
        }
        loadInventory(selectedWarehouse.getWarehouseId());
    }

    /** Load stock for warehouse **/
    private void loadInventory(int warehouseId) {
        List<Product> products = managerService.getInventoryByWarehouse(warehouseId);
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getQuantity()
            });
        }
    }

    /** Assign agent **/
    private void assignAgent() {
        int row = ordersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order first!");
            return;
        }

        int orderId = (int) ordersTable.getValueAt(row, 0);
        String agentIdStr = JOptionPane.showInputDialog(this, "Enter Agent ID:");
        if (agentIdStr == null || agentIdStr.trim().isEmpty()) return;

        try {
            int agentId = Integer.parseInt(agentIdStr);
            boolean success = managerService.assignAgent(orderId, agentId);
            JOptionPane.showMessageDialog(this, success ? "Agent assigned!" : "Failed to assign agent.");
            loadOrders();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid agent ID!");
        }
    }

    /** Update stock **/
    private void updateStock() {
        int row = inventoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first!");
            return;
        }

        int productId = (int) inventoryTable.getValueAt(row, 0);
        String qtyStr = JOptionPane.showInputDialog(this, "Enter new stock quantity:");
        if (qtyStr == null || qtyStr.trim().isEmpty()) return;

        try {
            int newQty = Integer.parseInt(qtyStr);
            boolean success = managerService.updateStock(productId, newQty);
            JOptionPane.showMessageDialog(this, success ? "Stock updated!" : "Update failed.");

            Warehouse selectedWarehouse = (Warehouse) warehouseComboBox.getSelectedItem();
            if (selectedWarehouse != null)
                loadInventory(selectedWarehouse.getWarehouseId());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WarehouseManagerDashboard(4)); // Example: Diana
    }
}
