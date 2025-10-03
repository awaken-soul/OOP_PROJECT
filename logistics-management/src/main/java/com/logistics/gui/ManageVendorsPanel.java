package com.logistics.gui;

import com.logistics.models.Retailer;
import com.logistics.models.Warehouse;
import com.logistics.services.RetailerService;
import com.logistics.services.WarehouseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageVendorsPanel extends JPanel {

    private final RetailerService retailerService;
    private final WarehouseService warehouseService;

    private final JTable retailerTable;
    private final DefaultTableModel retailerTableModel;
    private final JTable warehouseTable;
    private final DefaultTableModel warehouseTableModel;

    public ManageVendorsPanel(RetailerService retailerService, WarehouseService warehouseService) {
        this.retailerService = retailerService;
        this.warehouseService = warehouseService;

        setLayout(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createRetailerPanel());
        add(createWarehousePanel());

        loadRetailers();
        loadWarehouses();
    }

    private JPanel createRetailerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Retailers"));

        String columnNames = {"ID", "Name", "Address", "Contact Number"};
        retailerTableModel = new DefaultTableModel(columnNames, 0);
        retailerTable = new JTable(retailerTableModel);
        retailerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(retailerTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Retailer");
        JButton editButton = new JButton("Edit Retailer");
        JButton deleteButton = new JButton("Delete Retailer");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addRetailer());
        editButton.addActionListener(e -> editRetailer());
        deleteButton.addActionListener(e -> deleteRetailer());

        return panel;
    }

    private JPanel createWarehousePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Warehouses"));

        String columnNames = {"ID", "Name", "Address", "Capacity", "Manager ID"};
        warehouseTableModel = new DefaultTableModel(columnNames, 0);
        warehouseTable = new JTable(warehouseTableModel);
        warehouseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(warehouseTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Warehouse");
        JButton editButton = new JButton("Edit Warehouse");
        JButton deleteButton = new JButton("Delete Warehouse");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addWarehouse());
        editButton.addActionListener(e -> editWarehouse());
        deleteButton.addActionListener(e -> deleteWarehouse());

        return panel;
    }

    private void loadRetailers() {
        retailerTableModel.setRowCount(0);
        List<Retailer> retailers = retailerService.getAllRetailers();
        for (Retailer retailer : retailers) {
            retailerTableModel.addRow(new Object{
                    retailer.getRetailerId(),
                    retailer.getName(),
                    retailer.getAddress(),
                    retailer.getContactNumber()
            });
        }
    }

    private void loadWarehouses() {
        warehouseTableModel.setRowCount(0);
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        for (Warehouse warehouse : warehouses) {
            warehouseTableModel.addRow(new Object{
                    warehouse.getWarehouseId(),
                    warehouse.getName(),
                    warehouse.getAddress(),
                    warehouse.getCapacity(),
                    warehouse.getManagerId()
            });
        }
    }

    // --- Retailer Actions ---
    private void addRetailer() {
        RetailerDialog dialog = new RetailerDialog(null);
        if (dialog.isConfirmed()) {
            if (retailerService.addRetailer(dialog.getRetailer())) {
                loadRetailers();
            }
        }
    }

    private void editRetailer() {
        int selectedRow = retailerTable.getSelectedRow();
        if (selectedRow == -1) return;
        int retailerId = (int) retailerTableModel.getValueAt(selectedRow, 0);
        Retailer retailer = retailerService.getAllRetailers().stream().filter(r -> r.getRetailerId() == retailerId).findFirst().orElse(null);
        if (retailer!= null) {
            RetailerDialog dialog = new RetailerDialog(retailer);
            if (dialog.isConfirmed()) {
                if (retailerService.updateRetailer(dialog.getRetailer())) {
                    loadRetailers();
                }
            }
        }
    }

    private void deleteRetailer() {
        int selectedRow = retailerTable.getSelectedRow();
        if (selectedRow == -1) return;
        int retailerId = (int) retailerTableModel.getValueAt(selectedRow, 0);
        Retailer retailer = retailerService.getAllRetailers().stream().filter(r -> r.getRetailerId() == retailerId).findFirst().orElse(null);
        if (retailer!= null) {
            if (JOptionPane.showConfirmDialog(this, "Delete " + retailer.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (retailerService.deleteRetailer(retailer)) {
                    loadRetailers();
                }
            }
        }
    }

    // --- Warehouse Actions ---
    private void addWarehouse() {
        WarehouseDialog dialog = new WarehouseDialog(null);
        if (dialog.isConfirmed()) {
            if (warehouseService.addWarehouse(dialog.getWarehouse())) {
                loadWarehouses();
            }
        }
    }

    private void editWarehouse() {
        int selectedRow = warehouseTable.getSelectedRow();
        if (selectedRow == -1) return;
        int warehouseId = (int) warehouseTableModel.getValueAt(selectedRow, 0);
        Warehouse warehouse = warehouseService.getAllWarehouses().stream().filter(w -> w.getWarehouseId() == warehouseId).findFirst().orElse(null);
        if (warehouse!= null) {
            WarehouseDialog dialog = new WarehouseDialog(warehouse);
            if (dialog.isConfirmed()) {
                if (warehouseService.updateWarehouse(dialog.getWarehouse())) {
                    loadWarehouses();
                }
            }
        }
    }

    private void deleteWarehouse() {
        int selectedRow = warehouseTable.getSelectedRow();
        if (selectedRow == -1) return;
        int warehouseId = (int) warehouseTableModel.getValueAt(selectedRow, 0);
        Warehouse warehouse = warehouseService.getAllWarehouses().stream().filter(w -> w.getWarehouseId() == warehouseId).findFirst().orElse(null);
        if (warehouse!= null) {
            if (JOptionPane.showConfirmDialog(this, "Delete " + warehouse.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (warehouseService.deleteWarehouse(warehouse)) {
                    loadWarehouses();
                }
            }
        }
    }

    // --- Inner Dialog Classes ---
    private static class RetailerDialog {
        private Retailer retailer;
        private boolean confirmed = false;
        public RetailerDialog(Retailer r) {
            this.retailer = r;
            JTextField nameField = new JTextField(r == null? "" : r.getName());
            JTextField addressField = new JTextField(r == null? "" : r.getAddress());
            JTextField contactField = new JTextField(r == null? "" : r.getContactNumber());
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Name:")); panel.add(nameField);
            panel.add(new JLabel("Address:")); panel.add(addressField);
            panel.add(new JLabel("Contact:")); panel.add(contactField);
            int result = JOptionPane.showConfirmDialog(null, panel, r == null? "Add Retailer" : "Edit Retailer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (r == null) this.retailer = new Retailer(nameField.getText(), addressField.getText(), contactField.getText());
                else {
                    r.setName(nameField.getText()); r.setAddress(addressField.getText()); r.setContactNumber(contactField.getText());
                }
                confirmed = true;
            }
        }
        public Retailer getRetailer() { return retailer; }
        public boolean isConfirmed() { return confirmed; }
    }

    private static class WarehouseDialog {
        private Warehouse warehouse;
        private boolean confirmed = false;
        public WarehouseDialog(Warehouse w) {
            this.warehouse = w;
            JTextField nameField = new JTextField(w == null? "" : w.getName());
            JTextField addressField = new JTextField(w == null? "" : w.getAddress());
            JTextField capacityField = new JTextField(w == null? "" : String.valueOf(w.getCapacity()));
            JTextField managerIdField = new JTextField(w == null? "" : String.valueOf(w.getManagerId()));
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Name:")); panel.add(nameField);
            panel.add(new JLabel("Address:")); panel.add(addressField);
            panel.add(new JLabel("Capacity:")); panel.add(capacityField);
            panel.add(new JLabel("Manager ID:")); panel.add(managerIdField);
            int result = JOptionPane.showConfirmDialog(null, panel, w == null? "Add Warehouse" : "Edit Warehouse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                int capacity = Integer.parseInt(capacityField.getText());
                int managerId = Integer.parseInt(managerIdField.getText());
                if (w == null) this.warehouse = new Warehouse(nameField.getText(), addressField.getText(), capacity, managerId);
                else {
                    w.setName(nameField.getText()); w.setAddress(addressField.getText()); w.setCapacity(capacity); w.setManagerId(managerId);
                }
                confirmed = true;
            }
        }
        public Warehouse getWarehouse() { return warehouse; }
        public boolean isConfirmed() { return confirmed; }
    }
}
