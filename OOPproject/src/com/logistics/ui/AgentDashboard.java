package com.logistics.ui;

import com.logistics.model.User;
import com.logistics.model.Order;
import com.logistics.model.Warehouse;
import com.logistics.service.AgentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AgentDashboard extends JFrame {

    private final User currentUser;
    private final AgentService agentService;

    private final DefaultTableModel availableModel;
    private final DefaultTableModel myJobsModel;

    private final JTable availableTable;
    private final JTable myJobsTable;

    private final JComboBox<Warehouse> warehouseBox = new JComboBox<>();
    private final JComboBox<String> statusBox = new JComboBox<>(new String[]{
            "In Warehouse", "Out for Delivery", "Delivered"
    });

    private final JButton acceptBtn = new JButton("Accept Order");
    private final JButton moveBtn = new JButton("Move to Warehouse");
    private final JButton updateStatusBtn = new JButton("Update Status");

    public AgentDashboard(AgentService agentService, User user) {
        this.agentService = agentService;
        this.currentUser = user;

        setTitle("Agent Dashboard - " + user.getName());
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        availableModel = new DefaultTableModel(
                new String[]{"Order ID", "Product", "Type", "Source", "Destination", "Status"}, 0
        );
        myJobsModel = new DefaultTableModel(
                new String[]{"Order ID", "Product", "Type", "Source", "Destination", "Status", }, 0
        );

        availableTable = new JTable(availableModel);
        myJobsTable = new JTable(myJobsModel);

        initUI();
        attachListeners();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Welcome, Agent " + currentUser.getName(), SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createAvailablePanel(),
                createMyJobsPanel()
        );
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);
    }

    private JPanel createAvailablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Available Orders "));

        JScrollPane scrollPane = new JScrollPane(availableTable);
        availableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel bottom = new JPanel();
        bottom.add(acceptBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createMyJobsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("My Shipments"));

        JScrollPane scrollPane = new JScrollPane(myJobsTable);
        myJobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(new JLabel("Warehouse:"));
        bottom.add(warehouseBox);
        bottom.add(moveBtn);
        bottom.add(new JLabel("Status:"));
        bottom.add(statusBox);
        bottom.add(updateStatusBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void attachListeners() {
        acceptBtn.addActionListener(this::acceptOrder);
        moveBtn.addActionListener(this::moveToWarehouse);
        updateStatusBtn.addActionListener(this::updateOrderStatus);
    }

    private void acceptOrder(ActionEvent e) {
        int row = availableTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to accept.");
            return;
        }

        int orderId = (int) availableModel.getValueAt(row, 0);
        boolean success = agentService.acceptOrder(orderId, currentUser.getUserId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Order accepted successfully!");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to accept order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moveToWarehouse(ActionEvent e) {
        int row = myJobsTable.getSelectedRow();
        Warehouse warehouse = (Warehouse) warehouseBox.getSelectedItem();

        if (row == -1 || warehouse == null) {
            JOptionPane.showMessageDialog(this, "Select an order and a warehouse.");
            return;
        }

        int orderId = (int) myJobsModel.getValueAt(row, 0);
        boolean success = agentService.moveOrderToWarehouse(orderId, warehouse.getWarehouseId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Order moved to warehouse: " + warehouse.getName());
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to move order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderStatus(ActionEvent e) {
        int row = myJobsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to update.");
            return;
        }

        int orderId = (int) myJobsModel.getValueAt(row, 0);
        String newStatus = (String) statusBox.getSelectedItem();
        boolean success = agentService.updateOrderStatus(orderId, newStatus);

        if (success) {
            JOptionPane.showMessageDialog(this, "Order status updated to: " + newStatus);
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update order status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        availableModel.setRowCount(0);
        myJobsModel.setRowCount(0);
        warehouseBox.removeAllItems();

        // ✅ Load available orders (Pending + Paid)
        List<Order> availableOrders = agentService.getAvailableOrders();
        if (availableOrders != null && !availableOrders.isEmpty()) {
            for (Order o : availableOrders) {
                availableModel.addRow(new Object[]{
                        o.getOrderId(),
                        o.getProductName() != null ? o.getProductName() : "Product #" + o.getProductId(),
                        safe(o.getOrderType()),
                        safe(o.getSourceAddress()),
                        safe(o.getDestinationAddress()),
                        safe(o.getStatus()),
   //                     safe(o.getPaymentStatus())
                });
            }
        }

        // ✅ Load accepted orders for this agent
        List<Order> myOrders = agentService.getOrdersByAgent(currentUser.getUserId());
        if (myOrders != null && !myOrders.isEmpty()) {
            for (Order o : myOrders) {
                myJobsModel.addRow(new Object[]{
                        o.getOrderId(),
                        o.getProductName() != null ? o.getProductName() : "Product #" + o.getProductId(),
                        safe(o.getOrderType()),
                        safe(o.getSourceAddress()),
                        safe(o.getDestinationAddress()),
                        safe(o.getStatus()),
                        o.getWarehouseName() != null ? o.getWarehouseName() : "-"
                });
            }
        }

        // ✅ Load warehouses
        List<Warehouse> warehouses = agentService.getAllWarehouses();
        if (warehouses != null && !warehouses.isEmpty()) {
            for (Warehouse w : warehouses) {
                warehouseBox.addItem(w);
            }
        }

        // ✅ Ensure warehouse dropdown shows only names
        warehouseBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Warehouse) {
                    setText(((Warehouse) value).getName());
                }
                return this;
            }
        });
    }

    private String safe(String s) {
        return (s != null && !s.isBlank()) ? s : "-";
    }
}
