package logistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistrationPage extends JFrame {
    private JTextField nameField, emailField, contactField, addressField;
    private JPasswordField passwordField;

    public RegistrationPage() {
        setTitle("Customer Registration");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6,2));
        panel.add(new JLabel("Name:")); nameField = new JTextField(); panel.add(nameField);
        panel.add(new JLabel("Email:")); emailField = new JTextField(); panel.add(emailField);
        panel.add(new JLabel("Password:")); passwordField = new JPasswordField(); panel.add(passwordField);
        panel.add(new JLabel("Contact:")); contactField = new JTextField(); panel.add(contactField);
        panel.add(new JLabel("Address:")); addressField = new JTextField(); panel.add(addressField);

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(this::registerAction);
        panel.add(registerBtn);

        add(panel);
        setVisible(true);
    }

    private void registerAction(ActionEvent e) {
        User user = new User(
                nameField.getText(),
                emailField.getText(),
                new String(passwordField.getPassword()),
                "Customer"
        );
        UserDAO dao = new UserDAO();
        if(dao.registerUser(user)){
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            new LoginPage();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed!");
        }
    }
}
