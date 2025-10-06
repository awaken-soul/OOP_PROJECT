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
        // --- FIX: Each button now calls its own specific handler ---
        toWarehouseButton.addActionListener(this::handleMoveToWarehouse);
        deliverButton.addActionListener(this::handleDeliver);
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

    // --- FIX: Created a specific handler for moving to a warehouse ---
    private void handleMoveToWarehouse(ActionEvent e) {
        Shipment selectedShipment = myShipmentsList.getSelectedValue();
        Warehouse selectedWarehouse = (Warehouse) warehouseComboBox.getSelectedItem();

        if (selectedShipment == null || selectedWarehouse == null) {
            JOptionPane.showMessageDialog(this, "Please select one of your jobs and a warehouse.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the correct 2-argument method
        boolean success = agentService.moveShipmentToWarehouse(selectedShipment.getId(), selectedWarehouse.getId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Shipment moved to warehouse!");
            refreshLists();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- FIX: Created a specific handler for delivering a shipment ---
    private void handleDeliver(ActionEvent e) {
        Shipment selectedShipment = myShipmentsList.getSelectedValue();
        if (selectedShipment == null) {
            JOptionPane.showMessageDialog(this, "Please select one of your jobs to mark as delivered.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the correct 1-argument method
        boolean success = agentService.markShipmentDelivered(selectedShipment.getId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Shipment marked as delivered!");
            refreshLists();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void refreshLists() {
        // Refresh available shipments
        availableShipmentsModel.clear();
        List<Shipment> available = agentService.getAvailableShipments();
        if(available != null) available.forEach(availableShipmentsModel::addElement);

        // Refresh agent's own shipments
        myShipmentsModel.clear();
        // --- FIX: Calling the correct method name ---
        List<Shipment> myJobs = agentService.getMyAcceptedShipments(currentAgent.getId());
        if(myJobs != null) myJobs.forEach(myShipmentsModel::addElement);

        // Refresh warehouse dropdown
        warehouseComboBox.removeAllItems();
        List<Warehouse> warehouses = agentService.getAllWarehouses();
        if(warehouses != null) warehouses.forEach(warehouseComboBox::addItem);
    }
}

