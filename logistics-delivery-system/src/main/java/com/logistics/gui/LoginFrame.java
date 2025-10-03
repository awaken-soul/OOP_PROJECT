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
        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginTitle.setForeground(AppColors.TEXT_BLACK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(loginTitle, gbc);

        // --- Email ---
        gbc.gridy = 1; gbc.gridwidth = 2;
        rightPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridy = 2;
        rightPanel.add(emailField, gbc);

        // --- Password ---
        gbc.gridy = 3;
        rightPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridy = 4;
        rightPanel.add(passwordField, gbc);

        // --- Role Selector ---
        gbc.gridy = 5;
        rightPanel.add(new JLabel("Role Selection:"), gbc);
        String[] roles = {"Customer", "Agent", "Manager", "Admin"};
        roleSelector = new JComboBox<>(roles);
        roleSelector.setBackground(AppColors.BACKGROUND_WHITE);
        gbc.gridy = 6;
        rightPanel.add(roleSelector, gbc);

        // --- Login Button ---
        JButton loginButton = new JButton("Login");
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
            messageLabel.setForeground(AppColors.WARNING_RED);
            messageLabel.setText("Please enter email, password, and select a role.");
            return;
        }

        // 1. Attempt Authentication via the Service Layer
        User user = userService.loginUser(email, password);

        // 2. Process Result
        if (user != null) {
            String actualRole = user.getRole() == null ? "" : user.getRole().trim();
            if (actualRole.equalsIgnoreCase(selectedRole.trim())) {
                messageLabel.setForeground(AppColors.ACCENT_GREEN);
                messageLabel.setText("Login Successful! Redirecting...");

                // Debug log for troubleshooting role/type mismatches
                System.out.println("Authenticated user class: " + user.getClass().getName() + ", role: '" + actualRole + "'");

                // Redirect on the Event Dispatch Thread and close login frame afterwards
                SwingUtilities.invokeLater(() -> {
                    try {
                        redirectToDashboard(user);
                        // Dispose the login window only after launching dashboard
                        this.dispose();
                    } catch (Throwable ex) {
                        // Show user-facing error if anything unexpected occurs during dashboard launch
                        JOptionPane.showMessageDialog(this,
                                "Internal error launching dashboard. See console for details.",
                                "Launch Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                });
            } else {
                messageLabel.setForeground(AppColors.WARNING_RED);
                messageLabel.setText("Login Failed: You are a " + actualRole + ", not a " + selectedRole + ".");
            }
        } else {
            messageLabel.setForeground(AppColors.WARNING_RED);
            messageLabel.setText("Login Failed: Invalid email or password.");
        }
    }

    /**
     * Redirects to the correct dashboard based on the authenticated user's role.
     * This method defensively handles ClassCastException and reports actionable errors.
     */
    private void redirectToDashboard(User user) {
        String role = user.getRole() == null ? "" : user.getRole().trim();
        try {
            switch (role) {
                case "Customer":
                    // If UserService returns a generic User instance, dashboards should accept User.
                    if (user instanceof Customer) {
                        new CustomerDashboard((Customer) user).setVisible(true);
                    } else {
                        new CustomerDashboard(user).setVisible(true);
                    }
                    break;
                case "Admin":
                    if (user instanceof Admin) {
                        new AdminDashboard((Admin) user).setVisible(true);
                    } else {
                        new AdminDashboard(user).setVisible(true);
                    }
                    break;
                case "Agent":
                    if (user instanceof DeliveryAgent) {
                        new DeliveryAgentDashboard((DeliveryAgent) user).setVisible(true);
                    } else {
                        new DeliveryAgentDashboard(user).setVisible(true);
                    }
                    break;
                case "Manager":
                    if (user instanceof WarehouseManager) {
                        new WarehouseManagerDashboard((WarehouseManager) user).setVisible(true);
                    } else {
                        new WarehouseManagerDashboard(user).setVisible(true);
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Unknown role: " + role);
            }
        } catch (ClassCastException ex) {
            JOptionPane.showMessageDialog(this,
                    "Internal error launching dashboard. User type mismatch: " + ex.getMessage(),
                    "Launch Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}

