package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.models.Order;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Retrieves all orders assigned to a specific delivery agent.
     * @param agentId The ID of the delivery agent.
     * @return A list of Order objects.
     */
    public List<Order> getOrdersForAgent(int agentId) {
        return orderDAO.findByAgentId(agentId);
    }

    /**
     * Updates the status of a specific order.
     * @param orderId The ID of the order to update.
     * @param newStatus The new status to set for the order.
     * @return true if the update was successful, false otherwise.
     */
     public List<Order> getOrdersForAgent(int agentId) {
        return orderDAO.findByAgentId(agentId);
    }

    public List<Order> getOrdersForCustomer(int userId) {
        return orderDAO.findByUserId(userId);
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        // Business logic can be added here, e.g., checking if the status transition is valid.
        return orderDAO.updateStatus(orderId, newStatus);
    }

    /**
     * Creates a new order in the system.
     * @param order The Order object to be created.
     * @return true if the order was created successfully, false otherwise.
     */
    public boolean createNewOrder(Order order) {
        // Business logic for creating an order can be added here.
        return orderDAO.save(order);
    }
}
