package com.logistics.gui;

import com.logistics.database.OrderDAO;
import com.logistics.database.ProductDAO;
import com.logistics.database.UserDAO;
import com.logistics.database.VehicleDAO;
import com.logistics.models.User;
import com.logistics.services.OrderService;
import com.logistics.services.ProductService;
import com.logistics.services.UserService;
import com.logistics.services.VehicleService;

import javax.swing.*;

public class MainApplication {

    // Instantiate all DAOs and Services here.
    private static final UserDAO userDAO = new UserDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final ProductDAO productDAO = new ProductDAO();
    private static final VehicleDAO vehicleDAO = new VehicleDAO();

    private static final UserService userService = new UserService(userDAO);
    private static final OrderService orderService = new OrderService(orderDAO);
    private static final ProductService productService = new ProductService(productDAO);
    private static final VehicleService vehicleService = new VehicleService(vehicleDAO);

    public static void main(String args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(userService);
            loginFrame.setVisible(true);
        });
    }

    public static void showDashboardForUser(User user) {
        JFrame dashboard;
        switch (user.getRole()) {
            case ADMIN:
                dashboard = new AdminDashboardFrame(user, vehicleService); // Pass vehicleService
                dashboard.setVisible(true);
                break;
            case AGENT:
                dashboard = new DeliveryAgentDashboardFrame(user, orderService);
                dashboard.setVisible(true);
                break;
            case CUSTOMER:
                dashboard = new CustomerDashboardFrame(user, orderService, productService);
                dashboard.setVisible(true);
                break;
            case MANAGER:
                dashboard = new WarehouseManagerDashboardFrame(user, orderService, userService, vehicleService);
                dashboard.setVisible(true);
                break;
            default:
                throw new IllegalStateException("Unsupported user role: " + user.getRole());
        }
    }
}
