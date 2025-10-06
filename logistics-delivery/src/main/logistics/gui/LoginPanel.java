package com.logistics.ui;

import com.logistics.model.User;
import com.logistics.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JPanel for the login/signup screen.
 */
public class LoginPanel extends JPanel {

    private final MainFrame mainFrame; // To call back to the main frame to switch panels
    private final AuthService authService;

    // UI Components
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JButton signUpButton = new JButton("Sign Up as Customer");

    public LoginPanel(MainFrame mainFrame, AuthService authService) {
        this.mainFrame = mainFrame;
        this.authService = authService;
        initUI();
        attachListeners();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Login or Sign Up"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    private void attachListeners() {
        loginButton.addActionListener(this::handleLogin);
        signUpButton.addActionListener(this::handleSignUp);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authService.login(email, password);

        if (user != null) {
            // Tell the main frame to switch to the appropriate dashboard
            mainFrame.showDashboard(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignUp(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = authService.signUp(email, password, "CUSTOMER");

        if (success) {
            JOptionPane.showMessageDialog(this, "Sign up successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            passwordField.setText(""); // Clear password for login attempt
        } else {
            JOptionPane.showMessageDialog(this, "Sign up failed. Email might already be in use.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

