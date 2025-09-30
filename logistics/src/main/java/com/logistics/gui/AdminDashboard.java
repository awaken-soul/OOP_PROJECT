package com.logistics.gui;

import com.logistics.models.Admin;
import com.logistics.services.AdminService;
import com.logistics.utils.AppColors;

import javax.swing.*;
import java.awt.*;

/**
 * The main dashboard for the Admin role.
 * Includes panels for managing vendors, vehicles, and complaints.
 */
public class AdminDashboard extends JFrame {

    private final Admin admin;
    private final AdminService adminService;

    // Components for Vehicle Management
    private JTextField vehicleTypeField;
    private JTextField licensePlateField;
    private JTextField driverIdField;
    private JLabel vehicleMessageLabel;

    public AdminDashboard(Admin admin) {
        super("Admin Dashboard - Welcome, " + admin.getName());
        this.admin = admin;
        this.adminService = new AdminService();

        // --- Frame Setup ---
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AppColors.BACKGROUND_WHITE);

        // --- 1. Header ---
        add(createHeaderPanel(), BorderLayout.NORTH);

        // --- 2. Main Content Area (Layout matching wireframe: West Nav, Center Content) ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createNavigationPanel(), 
                createContentPanel());
        mainSplit.setDividerLocation(180);
        mainSplit.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplit, BorderLayout.CENTER);

        setVisible(true);
    }
    
    // ==========================================================
    // HEADER AND NAVIGATION PANELS
    // ==========================================================
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.PRIMARY_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("ADMIN CONSOLE: " + admin.getName());
        titleLabel.setForeground(AppColors.BACKGROUND_WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(AppColors.WARNING_RED);
        logoutButton.setForeground(AppColors.BACKGROUND_WHITE);
        logoutButton.addActionListener(e -> {
            admin.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(6, 1, 0, 10)); // Grid for nav links
        navPanel.setBackground(AppColors.SECONDARY_GRAY);
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        // Navigation Links (Matching wireframe categories) [cite: 130-133, 138]
        navPanel.add(createNavLink("Dashboard Overview", "Overview"));
        navPanel.add(createNavLink("Manage Vendors", "Vendors"));
        navPanel.add(createNavLink("Manage Vehicles", "Vehicles"));
        navPanel.add(createNavLink("Resolve Complaints", "Complaints"));
        navPanel.add(createNavLink("Warehouse Info", "Warehouse"));

        return navPanel;
    }

    private JButton createNavLink(String text, String command) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(AppColors.BACKGROUND_WHITE);
        button.setForeground(AppColors.TEXT_BLACK);
        button.setFocusPainted(false);
        button.setActionCommand(command);
        // Action listener to switch panels (handled in createContentPanel)
        return button;
    }

    // ==========================================================
    // CONTENT PANEL (Card Layout to switch views)
    // ==========================================================
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add all sub-panels here
        contentPanel.add(createOverviewPanel(), "Overview");
        contentPanel.add(createVendorManagementPanel(), "Vendors");
        contentPanel.add(createVehicleManagementPanel(), "Vehicles");
        contentPanel.add(createComplaintsPanel(), "Complaints");
        
        // Set action listeners on navigation buttons to switch cards
        for (Component comp : ((JPanel) ((JSplitPane) this.getContentPane().getComponent(1)).getLeftComponent()).getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).addActionListener(e -> {
                    CardLayout cl = (CardLayout) (contentPanel.getLayout());
                    cl.show(contentPanel, e.getActionCommand());
                });
            }
        }
        return contentPanel;
    }
    
    // --- 3.1 Overview/Summary Panel ---
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(AppColors.BACKGROUND_WHITE);
        
        // Vendor List (Placeholder) [cite: 136]
        JTextArea vendorList = new JTextArea("VENTOR LIST (Placeholder)\n- Retailer A (Pending)\n- Agent B (Approved)");
        vendorList.setBorder(BorderFactory.createTitledBorder("Active Vendors & Agents"));
        vendorList.setEditable(false);
        panel.add(new JScrollPane(vendorList));

        // Warehouse Summary Card (Placeholder) [cite: 147]
        JTextArea warehouseSummary = new JTextArea("WAREHOUSE SUMMARY CARD\nTotal Stock: 5,400 units\nOrders in Queue: 12\nManager: Alice");
        warehouseSummary.setBorder(BorderFactory.createTitledBorder("Warehouse Overview"));
        warehouseSummary.setEditable(false);
        panel.add(new JScrollPane(warehouseSummary));

        return panel;
    }

    // --- 3.2 Vendor Management Panel ---
    private JPanel createVendorManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppColors.PRIMARY_BLUE), "Vendor Management & Validation"));
        
        // Placeholder for Validation form
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField vendorIdField = new JTextField();
        JComboBox<String> vendorRoleCombo = new JComboBox<>(new String[]{"Agent", "Retailer", "Manager"});
        JButton validateButton = new JButton("Validate/Approve Vendor");
        validateButton.setBackground(AppColors.ACCENT_GREEN);
        
        form.add(new JLabel("Vendor ID:"));
        form.add(vendorIdField);
        form.add(new JLabel("Vendor Type:"));
        form.add(vendorRoleCombo);
        form.add(new JLabel(""));
        form.add(validateButton);
        
        validateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(vendorIdField.getText().trim());
                String role = (String) vendorRoleCombo.getSelectedItem();
                boolean success = adminService.validateVendor(id, role, true); // Simulate approval
                JOptionPane.showMessageDialog(this, success ? 
                    "Vendor ID " + id + " (" + role + ") approved successfully." : 
                    "Validation failed. Check ID/DB.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Vendor ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable()), BorderLayout.CENTER); // Table of Pending Vendors

        return panel;
    }

    // --- 3.3 Vehicle Management Panel ---
    private JPanel createVehicleManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppColors.PRIMARY_BLUE), "Vehicle Management"));
        
        // Add Vehicle Form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Vehicle Type (Truck/Bike):"));
        vehicleTypeField = new JTextField();
        formPanel.add(vehicleTypeField);
        
        formPanel.add(new JLabel("License Plate:"));
        licensePlateField = new JTextField();
        formPanel.add(licensePlateField);
        
        formPanel.add(new JLabel("Initial Driver ID (Agent):"));
        driverIdField = new JTextField();
        formPanel.add(driverIdField);
        
        JButton addButton = new JButton("Add New Vehicle");
        addButton.setBackground(AppColors.PRIMARY_BLUE);
        addButton.setForeground(AppColors.BACKGROUND_WHITE);
        addButton.addActionListener(e -> handleAddVehicle());
        formPanel.add(addButton);
        
        vehicleMessageLabel = new JLabel(" ");
        vehicleMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(vehicleMessageLabel);
        
        panel.add(formPanel, BorderLayout.NORTH);

        // Vehicle Status Table (Placeholder) [cite: 137]
        JTextArea statusArea = new JTextArea("VEHICLE STATUS (Table Placeholder)\n- Plate XYZ: Available (Driver 101)\n- Plate ABC: On Delivery (Driver 102)");
        statusArea.setBorder(BorderFactory.createTitledBorder("Current Fleet Status"));
        statusArea.setEditable(false);
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void handleAddVehicle() {
        try {
            String type = vehicleTypeField.getText().trim();
            String plate = licensePlateField.getText().trim();
            String driverIdStr = driverIdField.getText().trim();
            Integer driverId = driverIdStr.isEmpty() ? null : Integer.parseInt(driverIdStr);
            String initialLocation = "Warehouse 1"; // Default location

            if (type.isEmpty() || plate.isEmpty()) {
                vehicleMessageLabel.setText("Type and Plate are required.");
                vehicleMessageLabel.setForeground(AppColors.WARNING_RED);
                return;
            }
            
            boolean success = adminService.addNewVehicle(type, plate, driverId, initialLocation);
            
            if (success) {
                vehicleMessageLabel.setText("Vehicle added successfully!");
                vehicleMessageLabel.setForeground(AppColors.ACCENT_GREEN);
                vehicleTypeField.setText("");
                licensePlateField.setText("");
                driverIdField.setText("");
            } else {
                vehicleMessageLabel.setText("Failed to add vehicle. Plate may exist.");
                vehicleMessageLabel.setForeground(AppColors.WARNING_RED);
            }
        } catch (NumberFormatException e) {
            vehicleMessageLabel.setText("Driver ID must be a number or left blank.");
            vehicleMessageLabel.setForeground(AppColors.WARNING_RED);
        }
    }

    // --- 3.4 Complaints Panel ---
    private JPanel createComplaintsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppColors.PRIMARY_BLUE), "Complaint Section"));
        
        JTextArea complaintArea = new JTextArea("COMPLAINT SECTION (Log Placeholder)\n- Issue 001: Agent late for pickup (Pending)\n- Issue 002: Item damaged (Closed)");
        complaintArea.setEditable(false);
        panel.add(new JScrollPane(complaintArea), BorderLayout.CENTER);
        
        JPanel resolutionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField complaintIdField = new JTextField(10);
        JButton resolveButton = new JButton("Resolve Issue");
        resolveButton.setBackground(AppColors.ACCENT_GREEN);
        
        resolutionPanel.add(new JLabel("Issue ID:"));
        resolutionPanel.add(complaintIdField);
        resolutionPanel.add(resolveButton);

        resolveButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(complaintIdField.getText().trim());
                adminService.closeComplaint(id, "Resolved via mediation.");
                JOptionPane.showMessageDialog(this, "Issue ID " + id + " marked as resolved.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Issue ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(resolutionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
}
