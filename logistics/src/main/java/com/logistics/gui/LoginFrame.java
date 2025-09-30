package com.logistics.gui;

import com.logistics.models.*;
import com.logistics.services.UserService;
import com.logistics.utils.AppColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The main login frame for the application.
 * Allows users to enter credentials and select their role.
 */
public class LoginFrame extends JFrame {

    private final UserService userService;

    // GUI Components
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleSelector;
    private final JLabel messageLabel;

    public LoginFrame() {
        super("Logistics & Delivery Management System - Login");
        this.userService = new UserService(); // Initialize the service layer

        // --- Frame Setup ---
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Use a 2-column layout (Split Panel Wireframe style)
        setLayout(new GridLayout(1, 2));

        // --- 1. Left Panel (Logo/Illustration) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(AppColors.BACKGROUND_WHITE);
        leftPanel.setLayout(new GridBagLayout()); // Center contents
        
        JLabel logoLabel = new JLabel("Logistics & Delivery Management System");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(AppColors.PRIMARY_BLUE);
        [cite_start]// Placeholder for the logo/illustration [cite: 129]
        leftPanel.add(logoLabel); 
        
        add(leftPanel);

        // --- 2. Right Panel (Login Form) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(AppColors.SECONDARY_GRAY);
        rightPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title
        [cite_start]JLabel loginTitle = new JLabel("Login"); [cite: 128]
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginTitle.setForeground(AppColors.TEXT_BLACK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(loginTitle, gbc);

        // --- Email ---
        gbc.gridy = 1; gbc.gridwidth = 2;
        [cite_start]rightPanel.add(new JLabel("Email:"), gbc); [cite: 128]
        emailField = new JTextField(20);
        gbc.gridy = 2;
        rightPanel.add(emailField, gbc);

        // --- Password ---
        gbc.gridy = 3;
        [cite_start]rightPanel.add(new JLabel("Password:"), gbc); [cite: 130]
        passwordField = new JPasswordField(20);
        gbc.gridy = 4;
        rightPanel.add(passwordField, gbc);

        // --- Role Selector ---
        gbc.gridy = 5;
        [cite_start]rightPanel.add(new JLabel("Role Selection:"), gbc); [cite: 131]
        String[] roles = {"Customer", "Agent", "Manager", "Admin"};
        roleSelector = new JComboBox<>(roles);
        roleSelector.setBackground(AppColors.BACKGROUND_WHITE);
        gbc.gridy = 6;
        rightPanel.add(roleSelector, gbc);

        // --- Login Button ---
        [cite_start]JButton loginButton = new JButton("Login"); [cite: 141]
        loginButton.setBackground(AppColors.PRIMARY_BLUE);
        loginButton.setForeground(AppColors.BACKGROUND_WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(this::handleLogin);
        gbc.gridy = 7; gbc.ipady = 10;
        rightPanel.add(loginButton, gbc);
        
        // --- Message Label (for errors/feedback) ---
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(AppColors.WARNING_RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 8; gbc.ipady = 0;
        rightPanel.add(messageLabel, gbc);


        add(rightPanel);
        setVisible(true);
    }

    /**
     * Handles the login button click event.
     */
    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) roleSelector.getSelectedItem();
        
        // Input Validation
        if (email.isEmpty() || password.isEmpty() || selectedRole == null) {
            messageLabel.setText("Please enter email, password, and select a role.");
            return;
        }

        // 1. Attempt Authentication via the Service Layer
        User user = userService.loginUser(email, password);

        // 2. Process Result
        if (user != null) {
            // Check if the authenticated user's actual role matches the selected login role
            if (user.getRole().equals(selectedRole)) {
                messageLabel.setText("Login Successful! Redirecting...");
                messageLabel.setForeground(AppColors.ACCENT_GREEN);
                
                // Redirect to the appropriate dashboard
                redirectToDashboard(user);
                
                this.dispose(); // Close the login window
            } else {
                messageLabel.setText("Login Failed: You are a " + user.getRole() + ", not a " + selectedRole + ".");
                messageLabel.setForeground(AppColors.WARNING_RED);
            }
        } else {
            messageLabel.setText("Login Failed: Invalid email or password.");
            messageLabel.setForeground(AppColors.WARNING_RED);
        }
    }

    /**
     * Redirects to the correct dashboard based on the authenticated user's role.
     */
    private void redirectToDashboard(User user) {
        switch (user.getRole()) {
            case "Customer":
                // Launch the completed CustomerDashboard
                new CustomerDashboard((Customer) user).setVisible(true);
                break;
            case "Admin":
                // Launch the Admin Dashboard
                new AdminDashboard((Admin) user).setVisible(true); 
                break;
            case "Agent":
                // Launch the Agent Dashboard
                new DeliveryAgentDashboard((DeliveryAgent) user).setVisible(true); 
                break;
            case "Manager":
                // Placeholder: Launches the Warehouse Manager Dashboard
                // new WarehouseManagerDashboard((WarehouseManager) user).setVisible(true);
                JOptionPane.showMessageDialog(this, "Welcome Warehouse Manager: " + user.getName() + ". Dashboard not implemented yet.");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role! Cannot launch dashboard.");
        }
    }
}
