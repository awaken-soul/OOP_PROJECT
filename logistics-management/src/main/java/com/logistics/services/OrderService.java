package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.models.Order;
import com.logistics.models.Product;

import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderDAO orderDAO;
    private final ProductService productService;
    private final TrackingService trackingService; // New dependency

    // Updated constructor
    public OrderService(OrderDAO orderDAO, ProductService productService, TrackingService trackingService) {
        this.orderDAO = orderDAO;
        this.productService = productService;
        this.trackingService = trackingService;
    }

    public List<Order> getOrdersForAgent(int agentId) {
        return orderDAO.findByAgentId(agentId);
    }

    public List<Order> getOrdersForCustomer(int userId) {
        return orderDAO.findByUserId(userId);
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        boolean success = orderDAO.updateStatus(orderId, newStatus);
        if (success) {
            // Log this status change to the tracking history
            trackingService.addTrackingUpdate(orderId, 0, newStatus, "Status updated by agent.");
        }
        return success;
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.findByStatus(status);
    }

    public boolean assignOrderToAgent(int orderId, int agentId, int vehicleId) {
        String newStatus = "Out for Delivery";
        boolean success = orderDAO.assignAgentAndVehicle(orderId, agentId, vehicleId, newStatus);
        if (success) {
            // Log this assignment to the tracking history
            trackingService.addTrackingUpdate(orderId, agentId, newStatus, "Assigned by Warehouse Manager.");
        }
        return success;
    }

    public boolean createNewOrder(Order order) {
        // Step 1: Save the new order and get its generated ID.
        Optional<Integer> newOrderIdOpt = orderDAO.save(order);

        if (newOrderIdOpt.isPresent()) {
            int newOrderId = newOrderIdOpt.get();

            // Step 2: Log the initial "Pending" status to the tracking history.
            trackingService.addTrackingUpdate(newOrderId, 0, "Pending", "Order placed by customer.");

            // Step 3: Update the product stock.
            Optional<Product> productOpt = productService.getProductById(order.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                int newQuantity = product.getQuantity() - 1;
                productService.updateProductQuantity(product.getProductId(), newQuantity);
            }
            return true;
        }
        return false;
    }
}
