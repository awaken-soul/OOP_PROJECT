package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.database.UserDAO;
import com.logistics.database.VehicleDAO; 
import com.logistics.models.DeliveryAgent;
import com.logistics.models.Order;
import com.logistics.models.User;
import com.logistics.models.Vehicle;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final VehicleDAO vehicleDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.userDAO = new UserDAO();
        this.vehicleDAO = new VehicleDAO(); 
    }
    
    public Order placeNewRequest(Order order) {
        if (!orderDAO.saveNewOrder(order)) {
            System.err.println("OrderService: Failed to save order to database.");
            return null;
        }
        System.out.println("New request placed, Order ID: " + order.getOrderID());
        attemptToAssignAgent(order.getOrderID());
        return order;
    }

    public boolean attemptToAssignAgent(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null || !order.getStatus().equals("Pending")) return false;
        List<DeliveryAgent> availableAgents = userDAO.getAvailableAgents();
        List<Vehicle> availableVehicles = vehicleDAO.getAvailableVehicles(); 
        if (availableAgents.isEmpty() || availableVehicles.isEmpty()) {
            System.out.println("Assignment skipped for Order " + orderId + ": No agents/vehicles currently available.");
            return false;
        }
        
        String pickupLocation = order.getSourceAddress(); 
        Optional<DeliveryAgent> closestAgent = availableAgents.stream()
            .min(Comparator.comparingDouble(agent -> 
                simulateDistance(agent.getAddress(), pickupLocation)));

        Optional<Vehicle> nearestVehicle = availableVehicles.stream().findFirst(); 

        if (closestAgent.isPresent() && nearestVehicle.isPresent()) {
            DeliveryAgent agent = closestAgent.get();
            Vehicle vehicle = nearestVehicle.get();
            
            String newStatus = "Assigned - Waiting Pickup";
            boolean success = orderDAO.updateOrderStatus(
                orderId, 
                newStatus, 
                agent.getUserID(), 
                vehicle.getVehicleID()
            );

            if (success) {
                System.out.println("Successfully assigned Agent " + agent.getUserID() + " to Order " + orderId + " with Vehicle " + vehicle.getVehicleID());
                return true;
            }
        }
        
        return false;
    }
    
   
    private double simulateDistance(String locationA, String locationB) {
        return Math.random() * 100; 
    }
    
    public String getTrackingStatus(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            return "Error: Order not found.";
        }
        return "Current Status: " + order.getStatus() + " (Last updated: " + order.getUpdatedAt() + ")";
    }
    
    public boolean agentAcceptsDelivery(int orderId, int agentId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null || order.getAssignedAgentID() == null || order.getAssignedAgentID() != agentId) {
            return false;
        }
        return orderDAO.updateOrderStatus(orderId, "Agent Accepted - En Route to Pickup", agentId, order.getVehicleID());
    }
}
