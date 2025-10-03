package com.logistics.gui;

import com.logistics.models.Vehicle;
import com.logistics.services.VehicleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageVehiclesPanel extends JPanel {

    private final JTable vehiclesTable;
    private final DefaultTableModel tableModel;
    private final VehicleService vehicleService;

    public ManageVehiclesPanel(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table Panel
        String columnNames = {"ID", "Type", "License Plate", "Status", "Current Location"};
        tableModel = new DefaultTableModel(columnNames, 0);
        vehiclesTable = new JTable(tableModel);
        vehiclesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(vehiclesTable);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Add Vehicle");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addVehicle());
        editButton.addActionListener(e -> editVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());

        loadVehicles();
    }

    private void loadVehicles() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        for (Vehicle vehicle : vehicles) {
            Object rowData = {
                    vehicle.getVehicleId(),
                    vehicle.getVehicleType(),
                    vehicle.getLicensePlate(),
                    vehicle.getStatus(),
                    vehicle.getCurrentLocation()
            };
            tableModel.addRow(rowData);
        }
    }

    private void addVehicle() {
        VehicleDialog dialog = new VehicleDialog(null);
        if (dialog.isConfirmed()) {
            Vehicle newVehicle = dialog.getVehicle();
            if (vehicleService.addVehicle(newVehicle)) {
                JOptionPane.showMessageDialog(this, "Vehicle added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadVehicles();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editVehicle() {
        int selectedRow = vehiclesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int vehicleId = (int) tableModel.getValueAt(selectedRow, 0);
        Vehicle vehicleToEdit = vehicleService.getAllVehicles().stream()
               .filter(v -> v.getVehicleId() == vehicleId)
               .findFirst().orElse(null);

        if (vehicleToEdit!= null) {
            VehicleDialog dialog = new VehicleDialog(vehicleToEdit);
            if (dialog.isConfirmed()) {
                Vehicle updatedVehicle = dialog.getVehicle();
                if (vehicleService.updateVehicle(updatedVehicle)) {
                    JOptionPane.showMessageDialog(this, "Vehicle updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadVehicles();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteVehicle() {
        int selectedRow = vehiclesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int vehicleId = (int) tableModel.getValueAt(selectedRow, 0);
        Vehicle vehicleToDelete = vehicleService.getAllVehicles().stream()
               .filter(v -> v.getVehicleId() == vehicleId)
               .findFirst().orElse(null);

        if (vehicleToDelete!= null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete vehicle: " + vehicleToDelete.getLicensePlate() + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (vehicleService.deleteVehicle(vehicleToDelete)) {
                    JOptionPane.showMessageDialog(this, "Vehicle deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadVehicles();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Inner class for the Add/Edit dialog
    private static class VehicleDialog {
        private final JTextField typeField = new JTextField();
        private final JTextField licensePlateField = new JTextField();
        private final JComboBox<String> statusComboBox = new JComboBox<>(new String{"Available", "On Delivery", "Maintenance"});
        private final JTextField locationField = new JTextField();
        private Vehicle vehicle;
        private boolean confirmed = false;

        public VehicleDialog(Vehicle existingVehicle) {
            this.vehicle = existingVehicle;
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Type:"));
            panel.add(typeField);
            panel.add(new JLabel("License Plate:"));
            panel.add(licensePlateField);
            panel.add(new JLabel("Status:"));
            panel.add(statusComboBox);
            panel.add(new JLabel("Current Location:"));
            panel.add(locationField);

            if (vehicle!= null) {
                typeField.setText(vehicle.getVehicleType());
                licensePlateField.setText(vehicle.getLicensePlate());
                statusComboBox.setSelectedItem(vehicle.getStatus());
                locationField.setText(vehicle.getCurrentLocation());
            }

            int result = JOptionPane.showConfirmDialog(null, panel,
                    vehicle == null? "Add New Vehicle" : "Edit Vehicle",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                if (vehicle == null) { // Creating a new one
                    this.vehicle = new Vehicle(typeField.getText(), licensePlateField.getText(), (String) statusComboBox.getSelectedItem(), locationField.getText());
                } else { // Updating existing one
                    this.vehicle.setVehicleType(typeField.getText());
                    this.vehicle.setLicensePlate(licensePlateField.getText());
                    this.vehicle.setStatus((String) statusComboBox.getSelectedItem());
                    this.vehicle.setCurrentLocation(locationField.getText());
                }
                confirmed = true;
            }
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public boolean isConfirmed() {
            return confirmed;
        }
    }
}
