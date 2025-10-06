package com.logistics.ui;

import com.logistics.model.Agent;
import com.logistics.model.Shipment;
import com.logistics.model.Warehouse;
import com.logistics.service.AgentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * JPanel for the Agent dashboard.
 * Provides UI for viewing and managing shipments.
 */
public class AgentPanel extends JPanel {

    private final AgentService agentService;
    private final Agent currentAgent;

    // UI Components
    private final DefaultListModel<Shipment> availableShipmentsModel = new DefaultListModel<>();
    private final JList<Shipment> availableShipmentsList = new JList<>(availableShipmentsModel);
    private final DefaultListModel<Shipment> myShipmentsModel = new DefaultListModel<>();
    private final JList<Shipment> myShipmentsList = new JList<>(myShipmentsModel);

    private final JButton acceptButton = new JButton("Accept Selected Job");
    private final JButton toWarehouseButton = new JButton("Move to Warehouse");
    private final JButton deliverButton = new JButton("Mark as Delivered");

    private final JComboBox<Warehouse> warehouseComboBox = new JComboBox<>();

    public AgentPanel(AgentService agentService, Agent currentAgent) {
        this.agentService = agentService;
        this.currentAgent = currentAgent;
        initUI();
        attachListeners();
        refreshLists();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JLabel("Agent Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);

        // Main content area
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createListPanel("Available Jobs", availableShipmentsList, acceptButton),
                createListPanel("My Accepted Jobs", myShipmentsList, createMyJobsActionsPanel()));
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createListPanel(String title, JList<Shipment> list, JComponent actionComponent) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(actionComponent, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createMyJobsActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(new JLabel("Warehouse:"));
        panel.add(warehouseComboBox);
        panel.add(toWarehouseButton);
        panel.add(deliverButton);
        return panel;
    }

    private void attachListeners() {
        acceptButton.addActionListener(this::handleAcceptShipment);
        toWarehouseButton.addActionListener(e -> handleUpdateStatus("IN_WAREHOUSE"));
        deliverButton.addActionListener(e -> handleUpdateStatus("DELIVERED"));
    }

    private void handleAcceptShipment(ActionEvent e) {
        Shipment selected = availableShipmentsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a job to accept.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = agentService.acceptShipment(selected.getId(), currentAgent.getId());
        if (success) {
            JOptionPane.showMessageDialog(this, "Job accepted!");
            refreshLists();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to accept the job.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateStatus(String newStatus) {
        Shipment selected = myShipmentsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select one of your jobs to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer warehouseId = null;
        if (newStatus.equals("IN_WAREHOUSE")) {
            Warehouse selectedWarehouse = (Warehouse) warehouseComboBox.getSelectedItem();
            if (selectedWarehouse == null) {
                JOptionPane.showMessageDialog(this, "Please select a warehouse.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            warehouseId = selectedWarehouse.getId();
        }

        boolean success = agentService.updateShipmentStatus(selected.getId(), newStatus, warehouseId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Shipment status updated!");
            refreshLists();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void refreshLists() {
        // Refresh available shipments
        availableShipmentsModel.clear();
        List<Shipment> available = agentService.getAvailableShipments();
        available.forEach(availableShipmentsModel::addElement);

        // Refresh agent's own shipments
        myShipmentsModel.clear();
        List<Shipment> myJobs = agentService.getShipmentsByAgent(currentAgent.getId());
        myJobs.forEach(myShipmentsModel::addElement);

        // Refresh warehouse dropdown
        warehouseComboBox.removeAllItems();
        List<Warehouse> warehouses = agentService.getAllWarehouses();
        warehouses.forEach(warehouseComboBox::addItem);
    }
}

