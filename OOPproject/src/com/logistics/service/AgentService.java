package com.logistics.service;

import com.logistics.dao.OrderDAO;
import com.logistics.dao.WarehouseDao;
import com.logistics.model.Order;
import com.logistics.model.Warehouse;
import java.util.List;

public class AgentService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final WarehouseDao warehouseDAO = new WarehouseDao();

    // ✅ Get all available orders (Pending + Paid)
    public List<Order> getAvailableOrders() {
        return orderDAO.getAvailableOrders();  // <-- renamed to match your DAO
    }

    // ✅ Get all orders accepted by a specific agent
    public List<Order> getOrdersByAgent(int agentId) {
        return orderDAO.getOrdersByAgent(agentId);  // <-- renamed to match your DAO
    }

    // ✅ Agent accepts an order
    public boolean acceptOrder(int orderId, int agentId) {
        return orderDAO.assignAgent(orderId, agentId);
    }

    // ✅ Move order to warehouse
    public boolean moveOrderToWarehouse(int orderId, int warehouseId) {
        return orderDAO.moveOrderToWarehouse(orderId, warehouseId);
    }

    // ✅ Update order status
    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    // ✅ Get all warehouses
    public List<Warehouse> getAllWarehouses() {
        return warehouseDAO.getAllWarehouses();
    }
}
