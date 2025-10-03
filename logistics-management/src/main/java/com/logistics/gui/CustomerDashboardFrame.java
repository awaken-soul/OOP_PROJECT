package com.logistics.gui;

import com.logistics.models.User;

import javax.swing.*;
import java.awt.*;

public class CustomerDashboardFrame extends JFrame {

    public CustomerDashboardFrame(User user) {
        setTitle("Customer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }
}
