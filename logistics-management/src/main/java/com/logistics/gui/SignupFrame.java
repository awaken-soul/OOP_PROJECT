package com.logistics.gui;

import com.logistics.models.Role;
import com.logistics.models.User;
import com.logistics.services.UserService;

import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField contactField = new JTextField();
    private final JTextArea addressArea = new JTextArea(3, 20);
    private final JButton signupButton = new JButton("Create Account");
    private final UserService userService;

    public SignupFrame(UserService userService) {
        this.userService = userService;

        setTitle("Customer Signup");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(new JScrollPane(addressArea));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        buttonPanel.add(signupButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        signupButton.addActionListener(e -> performSignup());
    }

    private void performSignup() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String contact = contactField.getText();
        String address = addressArea.getText();

        if (name.isEmpty() |

| email.isEmpty() |
| password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Email, and Password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new User object for the customer
        User newUser = new User(name, email, password, Role.CUSTOMER, contact, address);

        boolean success = userService.registerUser(newUser);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Close the signup window
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. The email may already be in use.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
