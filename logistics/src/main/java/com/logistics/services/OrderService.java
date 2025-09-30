package com.logistics.services;

import com.logistics.database.OrderDAO;
import com.logistics.database.UserDAO; // Needed to find available agents
import com.logistics.database.VehicleDAO; // Needed to find available vehicles
import com.logistics.models.DeliveryAgent;
import com.logistics.models.Order;
import com.logistics.models.User;
import com.logistics.models.Vehicle;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Handles all business logic related to Order transactions, agent assignment, 
 * and delivery status updates.
 */
public class OrderService {

    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final VehicleDAO vehicleDAO; // Assuming this DAO is created later

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.userDAO = new UserDAO();
        // NOTE: VehicleDAO is needed here but is a placeholder until created.
        this.vehicleDAO = new VehicleDAO(); 
    }

    // --- Core Functional Requirement: Place Order/Shipment/Transport ---

    /**
     * Creates a new order, saves it to the DB, and attempts to assign a delivery agent.
     * @param order The new Order model object.
     * @return The saved Order object with its generated ID, or null on failure.
     */
    public Order placeNewRequest(Order order) {
        // 1. Save the initial order to the database
        if (!orderDAO.saveNewOrder(order)) {
            System.err.println("OrderService: Failed to save order to database.");
            return null;
        }

        System.out.println("New request placed, Order ID: " + order.getOrderID());

        // 2. Immediately attempt to assign an agent/vehicle (business logic)
        attemptToAssignAgent(order.getOrderID());
        
        return order;
    }
    
    // --- Business Logic: Agent Assignment ---

    /**
     * Finds the most suitable Delivery Agent and Vehicle and assigns them to the order.
     * This logic directly addresses the 'proximity' and 'cargo specifications' requirements.
     * NOTE: This is a simulation using simple string matching/sorting for location.
     */
    public boolean attemptToAssignAgent(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null || !order.getStatus().equals("Pending")) return false;
        
        // 1. Get all available agents and vehicles (requires UserDAO and VehicleDAO methods)
        // For a full system, we would filter by 'cargo specifications' (vehicle type vs. order size)
        List<DeliveryAgent> availableAgents = userDAO.getAvailableAgents(); // Placeholder method
        List<Vehicle> availableVehicles = vehicleDAO.getAvailableVehicles(); // Placeholder method

        if (availableAgents.isEmpty() || availableVehicles.isEmpty()) {
            System.out.println("Assignment skipped for Order " + orderId + ": No agents/vehicles currently available.");
            return false;
        }
        
        String pickupLocation = order.getSourceAddress(); // E.g., "12.9716, 77.5946" (coordinates)

        // 2. Simulate finding the closest agent/vehicle
        Optional<DeliveryAgent> closestAgent = availableAgents.stream()
            .min(Comparator.comparingDouble(agent -> 
                simulateDistance(agent.getAddress(), pickupLocation))); // Assume agent address is their location

        Optional<Vehicle> nearestVehicle = availableVehicles.stream().findFirst(); // Simplistic assignment

        if (closestAgent.isPresent() && nearestVehicle.isPresent()) {
            // 3. Update the order in the database
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
                // Update vehicle/agent status (requires respective DAO methods)
                System.out.println("Successfully assigned Agent " + agent.getUserID() + " to Order " + orderId + " with Vehicle " + vehicle.getVehicleID());
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Simulates distance calculation between two addresses (simplified for academic project).
     * In a real system, this would use a proper geographic library (e.g., Haversine formula).
     */
    private double simulateDistance(String locationA, String locationB) {
        // Simple placeholder logic: treat address strings as random numerical values
        return Math.random() * 100; 
    }
    
    // --- Functional Requirement: Shipment Tracking ---

    /**
     * Retrieves the latest status of an order using the tracking records.
     */
    public String getTrackingStatus(int orderId) {
        // In a full implementation, this calls OrderDAO's tracking methods
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            return "Error: Order not found.";
        }
        return "Current Status: " + order.getStatus() + " (Last updated: " + order.getUpdatedAt() + ")";
    }
    
    // --- Agent Actions (Called from DeliveryAgent model) ---

    /**
     * Updates an order's status when the agent accepts the delivery.
     */
    public boolean agentAcceptsDelivery(int orderId, int agentId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null || order.getAssignedAgentID() == null || order.getAssignedAgentID() != agentId) {
            return false;
        }
        return orderDAO.updateOrderStatus(orderId, "Agent Accepted - En Route to Pickup", agentId, order.getVehicleID());
    }
}
