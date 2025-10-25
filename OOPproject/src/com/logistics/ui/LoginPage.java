package com.logistics.ui;

import com.logistics.dao.*;
import com.logistics.model.*;
import com.logistics.service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Login screen for all user roles.
 */
public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    private final AuthService authService;

    public LoginPage(AuthService authService) {
        this.authService = authService;

        setTitle("Logistics & Delivery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        // Left side panel (logo/illustration)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(200, 255, 200));
        leftPanel.setPreferredSize(new Dimension(500, 0));
        JLabel logoLabel = new JLabel("<html><center>Logistics & Delivery<br/>Management System<br/></html>");
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(logoLabel);
        mainPanel.add(leftPanel, BorderLayout.CENTER);

        // Right side login form
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(400, 0));
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(title, gbc);
        gbc.gridwidth = 1;

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        rightPanel.add(new JLabel("Email"), gbc);
        emailField = new JTextField();
        gbc.gridx = 1;
        rightPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        rightPanel.add(new JLabel("Password"), gbc);
        passwordField = new JPasswordField();
        gbc.gridx = 1;
        rightPanel.add(passwordField, gbc);

        // Role selection
        gbc.gridx = 0; gbc.gridy = 3;
        rightPanel.add(new JLabel("Role"), gbc);
        roleBox = new JComboBox<>(new String[]{"Admin", "Customer", "Agent", "Manager"});
        gbc.gridx = 1;
        rightPanel.add(roleBox, gbc);

        // Forgot/Signup links
        JPanel linksPanel = new JPanel(new BorderLayout());
        JLabel forgot = new JLabel("<html><u>Forgot password?</u></html>");
        JLabel signup = new JLabel("<html><u>Sign up</u></html>");
        forgot.setFont(new Font("Arial", Font.PLAIN, 11));
        signup.setFont(new Font("Arial", Font.PLAIN, 11));
        signup.setHorizontalAlignment(SwingConstants.RIGHT);
        linksPanel.add(forgot, BorderLayout.WEST);
        linksPanel.add(signup, BorderLayout.EAST);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        rightPanel.add(linksPanel, gbc);
        gbc.gridwidth = 1;

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(30, 144, 255));
        loginBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        rightPanel.add(loginBtn, gbc);

        mainPanel.add(rightPanel, BorderLayout.EAST);

        // --- Action Listeners ---
        loginBtn.addActionListener(this::loginAction);
        signup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new RegistrationPage(authService); // ✅ FIXED: pass AuthService
            }
        });

        setVisible(true);
    }

    private void loginAction(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        User user = authService.login(email, password);

        if (user != null && user.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");

            switch (role) {
                case "Customer":
                    new CustomerDashboard(user);
                    break;
                case "Admin":
                	UserDAO userDAO = new UserDAO();
                	WarehouseDao warehouseDao = new WarehouseDao();
                	AdminService adminService = new AdminService(userDAO, warehouseDao);
       
                    new AdminDashboard(adminService);

                    break;
                    
                case "Agent":
                    AgentService agentService = new AgentService();
                    new AgentDashboard(agentService, user).setVisible(true); // 👈 no cast
                    dispose();
                    break;

                case "Manager":
                	if (user.getRole().equalsIgnoreCase("Manager")) {
                	    new WarehouseManagerDashboard(user); // pass full User object
                	}
                	break;
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials or role!");
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        AuthService authService = new AuthService(userDAO);
        SwingUtilities.invokeLater(() -> new LoginPage(authService));
    }
}
