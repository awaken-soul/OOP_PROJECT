package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.database.UserDAO; 
import com.logistics.database.VehicleDAO; 
import com.logistics.models.DeliveryAgent;
import com.logistics.models.Order;
import com.logistics.models.Vehicle;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final VehicleDAO vehicleDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.userDAO = new UserDAO();
        this.vehicleDAO = new VehicleDAO(); 
    }

    // --- Core Functional Requirement: Place Order/Shipment/Transport ---

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
        
        // 1. Get first available agent and vehicle
        List<DeliveryAgent> availableAgents = userDAO.getAvailableAgents(); 
        List<Vehicle> availableVehicles = vehicleDAO.getAvailableVehicles(); 
    
        if (availableAgents.isEmpty() || availableVehicles.isEmpty()) {
            System.out.println("Assignment skipped for Order " + orderId + ": No agents/vehicles currently available.");
            return false;
        }
        
        // 2. ASSIGNMENT LOGIC: Assign the first agent and vehicle found (The "Next Available" logic)
        DeliveryAgent agent = availableAgents.get(0);
        Vehicle vehicle = availableVehicles.get(0);
        
        String newStatus = "Assigned - Waiting Pickup";
        boolean success = orderDAO.updateOrderStatus(
            orderId, 
            newStatus, 
            agent.getUserID(), 
            vehicle.getVehicleID()
        );
    
        if (success) {
            // Update the vehicle status to 'On Delivery' (takes it out of the available list for next order)
            vehicleDAO.updateVehicleStatus(vehicle.getVehicleID(), "On Delivery", vehicle.getCurrentLocation());
            System.out.println("Successfully assigned Agent " + agent.getUserID() + " to Order " + orderId + " with Vehicle " + vehicle.getVehicleID());
            return true;
        }
        
        return false;
    }

    
    // --- Functional Requirement: Shipment Tracking ---

    public String getTrackingStatus(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            return "Error: Order not found.";
        }
        return "Current Status: " + order.getStatus() + " (Last updated: " + order.getUpdatedAt() + ")";
    }
    
    // --- Agent Actions ---

    public boolean agentAcceptsDelivery(int orderId, int agentId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null || order.getAssignedAgentID() == null || !order.getAssignedAgentID().equals(agentId)) {
            return false;
        }
        // Assuming the agent is accepting their assigned order (status update)
        return orderDAO.updateOrderStatus(orderId, "Agent Accepted - En Route to Pickup", agentId, order.getVehicleID());
    }
}
