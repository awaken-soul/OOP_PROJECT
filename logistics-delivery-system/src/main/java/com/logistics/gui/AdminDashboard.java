package com.logistics.gui;

import com.logistics.models.Admin;
import com.logistics.services.AdminService;
import com.logistics.utils.AppColors;
import javax.swing.*;
import java.awt.*;


public class AdminDashboard extends JFrame {

    private final Admin admin;
    private final AdminService adminService;
    private final JPanel contentPanel;
    private final JPanel navPanel;   
    private JTextField vehicleTypeField;
    private JTextField licensePlateField;
    private JTextField driverIdField;
    private JLabel vehicleMessageLabel;

    public AdminDashboard(Admin admin) {
        super("Admin Dashboard - Welcome, " + admin.getName());
        this.admin = admin;
        this.adminService = new AdminService();
        this.contentPanel = createContentPanel();
        this.navPanel = createNavigationPanel();
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AppColors.BACKGROUND_WHITE);
        add(createHeaderPanel(), BorderLayout.NORTH);
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                this.navPanel,
                this.contentPanel); 
        mainSplit.setDividerLocation(180);
        mainSplit.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplit, BorderLayout.CENTER);
        setupNavigationListeners();
        setVisible(true);
    }
    private void setupNavigationListeners() {
        final CardLayout cl = (CardLayout) (this.contentPanel.getLayout());  
        for (Component comp : navPanel.getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).addActionListener(e -> {
                    cl.show(contentPanel, e.getActionCommand());
                });
            }
        }
    }
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 0, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panel.add(createNavLink("Dashboard Overview", "Overview"));
        panel.add(createNavLink("Manage Vendors", "Vendors"));
        panel.add(createNavLink("Manage Vehicles", "Vehicles"));
        panel.add(createNavLink("Resolve Complaints", "Complaints"));
        panel.add(createNavLink("Warehouse Info", "Warehouse"));
        return panel;
    }

    private JButton createNavLink(String text, String command) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(AppColors.BACKGROUND_WHITE);
        button.setForeground(AppColors.TEXT_BLACK);
        button.setFocusPainted(false);
        button.setActionCommand(command);
        return button;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new CardLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createOverviewPanel(), "Overview");
        panel.add(createVendorManagementPanel(), "Vendors");
        panel.add(createVehicleManagementPanel(), "Vehicles");
        panel.add(createComplaintsPanel(), "Complaints");
        panel.add(createWarehouseInfoPanel(), "Warehouse"); // <-- NEW WAREHOUSE INFO PANEL ADDED
        
        return panel;
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(AppColors.BACKGROUND_WHITE);
        
        JTextArea vendorList = new JTextArea("VENTOR LIST (Placeholder)\n- Retailer A (Pending)\n- Agent B (Approved)");
        vendorList.setBorder(BorderFactory.createTitledBorder("Active Vendors & Agents"));
        vendorList.setEditable(false);
        panel.add(new JScrollPane(vendorList));

        JTextArea warehouseSummary = new JTextArea("WAREHOUSE SUMMARY CARD\nTotal Stock: 5,400 units\nOrders in Queue: 12\nManager: Alice");
        warehouseSummary.setBorder(BorderFactory.createTitledBorder("Warehouse Overview"));
        warehouseSummary.setEditable(false);
        panel.add(new JScrollPane(warehouseSummary));

        return panel;
    }

    private JPanel createVendorManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppColors.PRIMARY_BLUE), "Vendor Management & Validation"));
        
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
                boolean success = adminService.validateVendor(id, role, true);
                JOptionPane.showMessageDialog(this, success ? 
                    "Vendor ID " + id + " (" + role + ") approved successfully." : 
                    "Validation failed. Check ID/DB.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Vendor ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable()), BorderLayout.CENTER); 

        return panel;
    }

    private JPanel createVehicleManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppColors.PRIMARY_BLUE), "Vehicle Management"));
        
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
            String initialLocation = "Warehouse 1"; 

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
    
    private JPanel createWarehouseInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(AppColors.BACKGROUND_WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppColors.PRIMARY_BLUE), "Warehouse Management & Status"
        ));
        
        JTextArea infoArea = new JTextArea(
            "WAREHOUSE SYSTEM STATUS\n\n" +
            "Warehouse 1 (ID: 10):\n" +
            "- Manager: Charlie Manager\n" +
            "- Capacity Used: 80%\n" +
            "- Inbound Shipments Today: 3"
        );
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JButton manageButton = new JButton("Go to Manager Portal (Charlie)");
        manageButton.setBackground(AppColors.ACCENT_GREEN.darker());
        manageButton.setForeground(AppColors.BACKGROUND_WHITE);
        
        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        panel.add(manageButton, BorderLayout.SOUTH);
        
        return panel;
    }
}
