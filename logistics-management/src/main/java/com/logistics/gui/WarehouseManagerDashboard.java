package com.logistics.gui;

import com.logistics.models.Order;
import com.logistics.models.User;
import com.logistics.models.Vehicle;
import com.logistics.services.OrderService;
import com.logistics.services.UserService;
import com.logistics.services.VehicleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WarehouseManagerDashboardFrame extends JFrame {

    private final JTable ordersTable;
    private final DefaultTableModel tableModel;
    private final OrderService orderService;
    private final UserService userService;
    private final VehicleService vehicleService;

    public WarehouseManagerDashboardFrame(User managerUser, OrderService orderService, UserService userService, VehicleService vehicleService) {
        this.orderService = orderService;
        this.userService = userService;
        this.vehicleService = vehicleService;

        setTitle("Warehouse Manager Dashboard");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel profileLabel = new JLabel("WAREHOUSE PROFILE | Welcome, " + managerUser.getName());
        profileLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(profileLabel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Pending Orders for Assignment"));

        String columnNames = {"Order ID", "Customer ID", "Product ID", "Destination"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = new JTable(tableModel);
        ordersTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionPanel.setPreferredSize(new Dimension(200, 0));

        JButton assignOrderButton = new JButton("Assign Selected Order");
        assignOrderButton.setMaximumSize(new Dimension(180, 40));
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionPanel.add(assignOrderButton);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        assignOrderButton.addActionListener(e -> showAssignOrderDialog());

        loadPendingOrders();
    }

    private void loadPendingOrders() {
        tableModel.setRowCount(0);
        List<Order> pendingOrders = orderService.getOrdersByStatus("Pending");
        for (Order order : pendingOrders) {
            Object rowData = {
                    order.getOrderId(),
                    order.getUserId(),
                    order.getProductId(),
                    order.getDestinationAddress()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAssignOrderDialog() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to assign.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer orderId = (Integer) ordersTable.getValueAt(selectedRow, 0);

        // Fetch available agents and vehicles
        List<User> availableAgents = userService.getAvailableAgents();
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();

        if (availableAgents.isEmpty() |

| availableVehicles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available agents or vehicles to assign.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create components for the dialog
        JComboBox<User> agentComboBox = new JComboBox<>(availableAgents.toArray(new User));
        agentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    setText(((User) value).getName());
                }
                return this;
            }
        });

        JComboBox<Vehicle> vehicleComboBox = new JComboBox<>(availableVehicles.toArray(new Vehicle));
        vehicleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vehicle) {
                    setText(((Vehicle) value).getLicensePlate());
                }
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

            boolean success = orderService.assignOrderToAgent(orderId, selectedAgent.getUserId(), selectedVehicle.getVehicleId());

            if (success) {
                JOptionPane.showMessageDialog(this, "Order assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadPendingOrders(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign the order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
