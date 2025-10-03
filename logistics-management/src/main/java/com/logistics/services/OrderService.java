package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.models.Order;
import com.logistics.models.Product;

import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderDAO orderDAO;
    private final ProductService productService; // New dependency

    // Updated constructor to accept ProductService
    public OrderService(OrderDAO orderDAO, ProductService productService) {
        this.orderDAO = orderDAO;
        this.productService = productService;
    }

    public List<Order> getOrdersForAgent(int agentId) {
        return orderDAO.findByAgentId(agentId);
    }

    public List<Order> getOrdersForCustomer(int userId) {
        return orderDAO.findByUserId(userId);
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        return orderDAO.updateStatus(orderId, newStatus);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.findByStatus(status);
    }

    public boolean assignOrderToAgent(int orderId, int agentId, int vehicleId) {
        String newStatus = "Out for Delivery";
        return orderDAO.assignAgentAndVehicle(orderId, agentId, vehicleId, newStatus);
    }

    /**
     * Creates a new order and automatically decrements the stock for the ordered product.
     * @param order The Order object to be created.
     * @return true if the order was created successfully, false otherwise.
     */
    public boolean createNewOrder(Order order) {
        // Step 1: Save the new order to the database.
        boolean orderSaved = orderDAO.save(order);

        // Step 2: If the order was saved, update the product stock.
        if (orderSaved) {
            Optional<Product> productOpt = productService.getProductById(order.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                int newQuantity = product.getQuantity() - 1;
                // This will fail gracefully if newQuantity is negative.
                productService.updateProductQuantity(product.getProductId(), newQuantity);
            }
        }
        return orderSaved;
    }
}
