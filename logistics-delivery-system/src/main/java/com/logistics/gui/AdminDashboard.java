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

    // GUI Components (Instance variables)
    private final JPanel contentPanel; // Panel using CardLayout
    private final JPanel navPanel;     // Panel holding navigation buttons (The fix)

    // Components for Vehicle Management
    private JTextField vehicleTypeField;
    private JTextField licensePlateField;
    private JTextField driverIdField;
    private JLabel vehicleMessageLabel;

    public AdminDashboard(Admin admin) {
        super("Admin Dashboard - Welcome, " + admin.getName());
        this.admin = admin;
        this.adminService = new AdminService();
        
        // Initialize core panels early
        this.contentPanel = createContentPanel();
        this.navPanel = createNavigationPanel();

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
                this.navPanel, 
                this.contentPanel); // <-- Use instance variables here
        mainSplit.setDividerLocation(180);
        mainSplit.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplit, BorderLayout.CENTER);

        // FINAL STEP: Link navigation buttons to the CardLayout panel safely
        setupNavigationListeners();

        setVisible(true);
    }
    
    // ==========================================================
    // LISTENER SETUP (THE CRITICAL FIX)
    // ==========================================================
    private void setupNavigationListeners() {
        // Safely iterate through components in the stored navPanel reference
        CardLayout cl = (CardLayout) (this.contentPanel.getLayout());
        
        for (Component comp : navPanel.getComponents()) {
            if (comp instanceof JButton) {
                // Attach a listener that tells the CardLayout to switch panels
                ((JButton) comp).addActionListener(e -> {
                    cl.show(contentPanel, e.getActionCommand());
                });
            }
        }
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 0, 10));
        panel.setBackground(AppColors.SECONDARY_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        // Navigation Links
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

    // ==========================================================
    // CONTENT PANEL (Card Layout to switch views)
    // ==========================================================
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new CardLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add all sub-panels here
        panel.add(createOverviewPanel(), "Overview");
        panel.add(createVendorManagementPanel(), "Vendors");
        panel.add(createVehicleManagementPanel(), "Vehicles");
        panel.add(createComplaintsPanel(), "Complaints");
        
        // NOTE: Listeners are added in setupNavigationListeners(), not here.
        return panel;
    }
    
    // --- 3.1 Overview/Summary Panel ---
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(AppColors.BACKGROUND_WHITE);
        
        // Vendor List (Placeholder) 
        JTextArea vendorList = new JTextArea("VENTOR LIST (Placeholder)\n- Retailer A (Pending)\n- Agent B (Approved)");
        vendorList.setBorder(BorderFactory.createTitledBorder("Active Vendors & Agents"));
        vendorList.setEditable(false);
        panel.add(new JScrollPane(vendorList));

        // Warehouse Summary Card (Placeholder) 
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

    // --- 3.3 Vehicle Management Panel ---
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
        addButton.setForeground(AppColors.
