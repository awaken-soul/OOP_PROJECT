package com.logistics.gui;

import com.logistics.models.Tracking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TrackingHistoryDialog extends JDialog {

    private final JTable trackingTable;
    private final DefaultTableModel tableModel;

    public TrackingHistoryDialog(Frame owner, int orderId, List<Tracking> trackingHistory) {
        super(owner, "Tracking History for Order #" + orderId, true); // true for modal
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Table to display the history
        String columnNames = {"Status", "Location / Note", "Timestamp"};
        tableModel = new DefaultTableModel(columnNames, 0);
        trackingTable = new JTable(tableModel);
        trackingTable.setFillsViewportHeight(true);

        // Populate the table with the provided history
        for (Tracking entry : trackingHistory) {
            tableModel.addRow(new Object{
                    entry.getCurrentStatus(),
                    entry.getLocation(),
                    entry.getUpdatedAt()
            });
        }

        JScrollPane scrollPane = new JScrollPane(trackingTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(closeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
