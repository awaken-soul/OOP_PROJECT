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

    private JTable retailerTable;
    private DefaultTableModel retailerTableModel;
    private JTable warehouseTable;
    private DefaultTableModel warehouseTableModel;

    public ManageVendorsPanel(RetailerService retailerService, WarehouseService warehouseService) {
        this.retailerService = retailerService;
        this.warehouseService = warehouseService;

        setLayout(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create and add the retailer management panel
        add(createRetailerPanel());
        // Create and add the warehouse management panel
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

        // TODO: Add ActionListeners for retailer buttons

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

        // TODO: Add ActionListeners for warehouse buttons

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
}
