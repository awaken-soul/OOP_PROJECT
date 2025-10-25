package com.logistics.ui;

import com.logistics.service.AuthService;
import com.logistics.dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Simple registration UI for creating new users.
 */
public class RegistrationPage extends JFrame {

    private final AuthService authService;

    public RegistrationPage(AuthService authService) {
        this.authService = authService;
        setTitle("User Registration");
        setSize(420, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Register New User", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // ✅ Replaced roleField with a dropdown
        String[] roles = {"Customer", "Admin", "Agent", "Manager"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        JTextField contactField = new JTextField();
        JTextField addressField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox); // ✅ Dropdown instead of text field
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton registerButton = new JButton("Register");
        formPanel.add(new JLabel());
        formPanel.add(registerButton);
        add(formPanel, BorderLayout.CENTER);

        registerButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem(); // ✅ Read from dropdown
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = authService.signUp(
                name,
                email,
                password,
                role,
                contact,
                address
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
                contactField.setText("");
                addressField.setText("");
                roleComboBox.setSelectedIndex(0); // Reset dropdown
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed! Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        AuthService authService = new AuthService(userDAO);
        new RegistrationPage(authService);
    }
}
