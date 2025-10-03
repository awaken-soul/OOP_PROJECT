package com.logistics.gui;

import com.logistics.models.*;
import com.logistics.services.UserService;
import com.logistics.utils.AppColors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private final UserService userService;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleSelector;
    private final JLabel messageLabel;

    public LoginFrame() {
        super("Logistics & Delivery Management System - Login");
        this.userService = new UserService(); 
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(AppColors.BACKGROUND_WHITE);
        leftPanel.setLayout(new GridBagLayout()); 

        JLabel logoLabel = new JLabel("Logistics & Delivery Management System");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(AppColors.PRIMARY_BLUE);
        leftPanel.add(logoLabel);

        add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(AppColors.SECONDARY_GRAY);
        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;


        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginTitle.setForeground(AppColors.TEXT_BLACK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(loginTitle, gbc);

        gbc.gridy = 1; gbc.gridwidth = 2;
        rightPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridy = 2;
        rightPanel.add(emailField, gbc);
        
        gbc.gridy = 3;
        rightPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridy = 4;
        rightPanel.add(passwordField, gbc);

        gbc.gridy = 5;
        rightPanel.add(new JLabel("Role Selection:"), gbc);
        String[] roles = {"Customer", "Agent", "Manager", "Admin"};
        roleSelector = new JComboBox<>(roles);
        roleSelector.setBackground(AppColors.BACKGROUND_WHITE);
        gbc.gridy = 6;
        rightPanel.add(roleSelector, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(AppColors.PRIMARY_BLUE);
        loginButton.setForeground(AppColors.BACKGROUND_WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(this::handleLogin);
        gbc.gridy = 7; gbc.ipady = 10;
        rightPanel.add(loginButton, gbc);

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(AppColors.WARNING_RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 8; gbc.ipady = 0;
        rightPanel.add(messageLabel, gbc);

        add(rightPanel);
        setVisible(true);
    }
    
    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) roleSelector.getSelectedItem();

        if (email.isEmpty() || password.isEmpty() || selectedRole == null) {
            messageLabel.setForeground(AppColors.WARNING_RED);
            messageLabel.setText("Please enter email, password, and select a role.");
            return;
        }

        User user = userService.loginUser(email, password);

        if (user != null) {
            String actualRole = user.getRole() == null ? "" : user.getRole().trim();
            if (actualRole.equalsIgnoreCase(selectedRole.trim())) {
                messageLabel.setForeground(AppColors.ACCENT_GREEN);
                messageLabel.setText("Login Successful! Redirecting...");

                System.out.println("Authenticated user class: " + user.getClass().getName() + ", role: '" + actualRole + "'");

                SwingUtilities.invokeLater(() -> {
                    try {
                        redirectToDashboard(user);
                        this.dispose();
                    } catch (Throwable ex) {
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

  private void redirectToDashboard(User user) {
    switch (user.getRole()) {
        case "Customer":
            new CustomerDashboard((Customer) user).setVisible(true);
            break;
        case "Admin":
            
            new AdminDashboard((Admin) user).setVisible(true); 
            break;
        case "Agent":
        
            new DeliveryAgentDashboard((DeliveryAgent) user).setVisible(true); 
            break;
        case "Manager":
            new WarehouseManagerDashboard((WarehouseManager) user).setVisible(true);
            break;
        default:
            JOptionPane.showMessageDialog(this, "Unknown role! Cannot launch dashboard.");
    }
}
}

