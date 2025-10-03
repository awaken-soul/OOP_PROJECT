package com.logistics.gui;

import com.logistics.database.*;
import com.logistics.models.User;
import com.logistics.services.*;

import javax.swing.*;

public class MainApplication {

    // Instantiate all DAOs and Services here.
    private static final UserDAO userDAO = new UserDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final ProductDAO productDAO = new ProductDAO();
    private static final VehicleDAO vehicleDAO = new VehicleDAO();
    private static final RetailerDAO retailerDAO = new RetailerDAO();
    private static final WarehouseDAO warehouseDAO = new WarehouseDAO();
    private static final TrackingDAO trackingDAO = new TrackingDAO();
    private static final ComplaintDAO complaintDAO = new ComplaintDAO();
    private static final PaymentDAO paymentDAO = new PaymentDAO(); // New DAO

    private static final UserService userService = new UserService(userDAO);
    private static final ProductService productService = new ProductService(productDAO);
    private static final VehicleService vehicleService = new VehicleService(vehicleDAO);
    private static final RetailerService retailerService = new RetailerService(retailerDAO);
    private static final WarehouseService warehouseService = new WarehouseService(warehouseDAO);
    private static final TrackingService trackingService = new TrackingService(trackingDAO);
    private static final ComplaintService complaintService = new ComplaintService(complaintDAO);
    private static final PaymentService paymentService = new PaymentService(paymentDAO); // New Service

    private static final OrderService orderService = new OrderService(orderDAO, productService, trackingService);

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
                dashboard = new AdminDashboardFrame(user, vehicleService, retailerService, warehouseService, complaintService);
                dashboard.setVisible(true);
                break;
            case AGENT:
                dashboard = new DeliveryAgentDashboardFrame(user, orderService, trackingService);
                dashboard.setVisible(true);
                break;
            case CUSTOMER:
                // Pass paymentService to the constructor
                dashboard = new CustomerDashboardFrame(user, orderService, productService, trackingService, complaintService, paymentService);
                dashboard.setVisible(true);
                break;
            case MANAGER:
                dashboard = new WarehouseManagerDashboardFrame(user, orderService, userService, vehicleService, productService, trackingService);
                dashboard.setVisible(true);
                break;
            default:
                throw new IllegalStateException("Unsupported user role: " + user.getRole());
        }
    }
}
