package logistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginPage() {
        setTitle("Logistics & Delivery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        // Left side panel for logo/illustration
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setPreferredSize(new Dimension(500, 0));
        JLabel logoLabel = new JLabel("<html><center>Logistics & Delivery<br/>Management System<br/>Illustration or Logo</center></html>");
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

        // Forgot/Signup links
        JPanel linksPanel = new JPanel(new BorderLayout());
        JLabel forgot = new JLabel("<html><u>Forgot password?</u></html>");
        JLabel signup = new JLabel("<html><u>Sign up</u></html>");
        forgot.setFont(new Font("Arial", Font.PLAIN, 11));
        signup.setFont(new Font("Arial", Font.PLAIN, 11));
        signup.setHorizontalAlignment(SwingConstants.RIGHT);
        linksPanel.add(forgot, BorderLayout.WEST);
        linksPanel.add(signup, BorderLayout.EAST);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        rightPanel.add(linksPanel, gbc);
        gbc.gridwidth = 1;

        // Role selection
        gbc.gridx = 0; gbc.gridy = 4;
        rightPanel.add(new JLabel("Role Selection"), gbc);
        roleBox = new JComboBox<>(new String[]{"Admin", "Customer", "Delivery Agent", "Warehouse Manager"});
        gbc.gridx = 1;
        rightPanel.add(roleBox, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(30, 144, 255));
        loginBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        rightPanel.add(loginBtn, gbc);

        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Action Listeners
        loginBtn.addActionListener(this::loginAction);
        signup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RegistrationPage();
                dispose();
            }
        });

        setVisible(true);
    }

    private void loginAction(ActionEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleBox.getSelectedItem().toString();

        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(email, password);

        if (user != null && user.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");

            switch (role) {
                case "Customer":
                    new UserDashboard(user);
                    break;
                case "Admin":
           //         new AdminDashboard(user);
                    break;
                case "Delivery Agent":
         //           new AgentDashboard(user);
                    break;
                case "Warehouse Manager":
          //          new WarehouseDashboard(user);
                    break;
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials or role!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
