package com.logistics.gui;

import com.logistics.models.Complaint;
import com.logistics.services.ComplaintService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageComplaintsPanel extends JPanel {

    private final JTable complaintsTable;
    private final DefaultTableModel tableModel;
    private final ComplaintService complaintService;

    public ManageComplaintsPanel(ComplaintService complaintService) {
        this.complaintService = complaintService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String columnNames = {"ID", "User ID", "Order ID", "Subject", "Status", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        complaintsTable = new JTable(tableModel);
        complaintsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(complaintsTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton viewDetailsButton = new JButton("View Details");
        JButton resolveButton = new JButton("Mark as Resolved");
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(resolveButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        viewDetailsButton.addActionListener(e -> viewComplaintDetails());
        resolveButton.addActionListener(e -> resolveComplaint());

        loadComplaints();
    }

    private void loadComplaints() {
        tableModel.setRowCount(0);
        List<Complaint> complaints = complaintService.getAllComplaints();
        for (Complaint complaint : complaints) {
            tableModel.addRow(new Object{
                    complaint.getComplaintId(),
                    complaint.getUserId(),
                    complaint.getOrderId() == null? "N/A" : complaint.getOrderId(),
                    complaint.getSubject(),
                    complaint.getStatus(),
                    complaint.getCreatedAt()
            });
        }
    }

    private void viewComplaintDetails() {
        int selectedRow = complaintsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int complaintId = (int) tableModel.getValueAt(selectedRow, 0);
        Complaint complaint = getSelectedComplaint(complaintId);
        if (complaint!= null) {
            JTextArea textArea = new JTextArea(10, 40);
            textArea.setText(complaint.getDescription());
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Complaint Details: #" + complaintId, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void resolveComplaint() {
        int selectedRow = complaintsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint to resolve.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int complaintId = (int) tableModel.getValueAt(selectedRow, 0);
        Complaint complaint = getSelectedComplaint(complaintId);
        if (complaint!= null && complaint.getStatus().equals("Open")) {
            int choice = JOptionPane.showConfirmDialog(this, "Mark complaint #" + complaintId + " as Resolved?", "Confirm Resolution", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                if (complaintService.resolveComplaint(complaintId)) {
                    JOptionPane.showMessageDialog(this, "Complaint resolved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadComplaints();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to resolve complaint.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (complaint!= null) {
            JOptionPane.showMessageDialog(this, "This complaint has already been resolved.", "Already Resolved", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Complaint getSelectedComplaint(int complaintId) {
        return complaintService.getAllComplaints().stream()
               .filter(c -> c.getComplaintId() == complaintId)
               .findFirst().orElse(null);
    }
}
