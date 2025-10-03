package com.logistics.gui;

import com.logistics.models.Order;
import com.logistics.models.User;
import com.logistics.services.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerDashboardFrame extends JFrame {

    private final JTable ordersTable;
    private final DefaultTableModel tableModel;
    private final User customerUser;
    private final OrderService orderService;

    public CustomerDashboardFrame(User user, OrderService orderService) {
        this.customerUser = user;
        this.orderService = orderService;

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

        add(welcomeLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

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
}
