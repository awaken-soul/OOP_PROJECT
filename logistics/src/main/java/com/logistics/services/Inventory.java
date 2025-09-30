package com.logistics.services;

import com.logistics.database.InventoryDAO;
import com.logistics.models.Inventory;

import java.util.List;

/**
 * Handles all business logic related to warehouse inventory, stock levels, and product assignment.
 */
public class InventoryService {

    private final InventoryDAO inventoryDAO;

    public InventoryService() {
        this.inventoryDAO = new InventoryDAO();
    }

    /**
     * Retrieves the current stock and product list for a given warehouse.
     * Corresponds to the Warehouse Manager's 'monitorStock()' function.
     * @param warehouseId The ID of the warehouse.
     * @return A list of Inventory items.
     */
    public List<Inventory> getWarehouseStock(int warehouseId) {
        System.out.println("InventoryService: Retrieving stock for Warehouse ID " + warehouseId);
        return inventoryDAO.getInventoryByWarehouse(warehouseId);
    }

    /**
     * Updates the stock level of a product after a sale or stock receipt.
     * Corresponds to the Warehouse Manager's 'assignProducts()' logic.
     * @param productId The ID of the product.
     * @param quantityChange The amount to change the stock by (positive for receipt, negative for dispatch).
     * @return true if the update was successful.
     */
    public boolean updateProductStock(int productId, int quantityChange) {
        // 1. Get current stock
        // NOTE: In a full implementation, you would first retrieve the current Inventory item
        // using a DAO method: Inventory current = inventoryDAO.getProductById(productId);
        
        // 2. Simulate current stock retrieval for demo:
        int currentStock = 100; // Placeholder
        
        int newStock = currentStock + quantityChange;
        if (newStock < 0) {
            System.err.println("InventoryService: Stock update failed. Insufficient stock (Current: " + currentStock + ").");
            return false;
        }

        System.out.println("InventoryService: Updating stock for Product ID " + productId + " to " + newStock);
        return inventoryDAO.updateStockLevel(productId, newStock);
    }
    
    /**
     * Simulates the assignment of a product to an Order for dispatch.
     * This is called after an order is placed and an agent is assigned.
     */
    public boolean assignProductToOrder(int orderId, int productId, int quantity) {
        // 1. Update the stock level (reducing inventory)
        boolean stockReduced = updateProductStock(productId, -quantity);
        
        if (stockReduced) {
            // 2. Logic to update Order to 'Ready for Pickup' status if necessary
            // orderDAO.updateOrderStatus(orderId, "Ready for Pickup", null, null);
            System.out.println("InventoryService: Product " + productId + " assigned and stock reduced for Order " + orderId);
            return true;
        }
        return false;
    }
}
