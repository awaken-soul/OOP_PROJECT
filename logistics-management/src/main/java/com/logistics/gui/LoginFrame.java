package com.logistics.gui;

import com.logistics.models.User;
import com.logistics.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton signupButton;
    private final UserService userService;

    public LoginFrame(UserService userService) {
        this.userService = userService;

        setTitle("Login - Logistics Management System");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout(10, 10));

        // Panel for input fields
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        fieldsPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        fieldsPanel.add(emailField);
        fieldsPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        fieldsPanel.add(passwordField);

        // Panel for buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        buttonsPanel.add(loginButton);
        buttonsPanel.add(signupButton);

        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> performLogin());
        signupButton.addActionListener(e -> openSignup());
    }

    private void performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() |

| password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the service layer to authenticate
        Optional<User> authenticatedUser = userService.authenticate(email, password);

        if (authenticatedUser.isPresent()) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + authenticatedUser.get().getName());
            this.dispose(); // Close the login window
            MainApplication.showDashboardForUser(authenticatedUser.get());
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSignup() {
        // Open the signup frame
        SignupFrame signupFrame = new SignupFrame(userService);
        signupFrame.setVisible(true);
    }
}
