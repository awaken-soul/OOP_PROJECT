package com.logistics.services;

import com.logistics.database.InventoryDAO;
import com.logistics.models.Inventory;
import java.util.List;

public class InventoryService {

    private final InventoryDAO inventoryDAO;

    public InventoryService() {
        this.inventoryDAO = new InventoryDAO();
    }
    
    public List<Inventory> getWarehouseStock(int warehouseId) {
        System.out.println("InventoryService: Retrieving stock for Warehouse ID " + warehouseId);
        return inventoryDAO.getInventoryByWarehouse(warehouseId);
    }

    public boolean updateProductStock(int productId, int quantityChange) {
        int currentStock = 100;
        int newStock = currentStock + quantityChange;
        if (newStock < 0) {
            System.err.println("InventoryService: Stock update failed. Insufficient stock (Current: " + currentStock + ").");
            return false;
        }
        System.out.println("InventoryService: Updating stock for Product ID " + productId + " to " + newStock);
        return inventoryDAO.updateStockLevel(productId, newStock);
    }
    
    public boolean assignProductToOrder(int orderId, int productId, int quantity) {
        boolean stockReduced = updateProductStock(productId, -quantity);
        if (stockReduced) {
            System.out.println("InventoryService: Product " + productId + " assigned and stock reduced for Order " + orderId);
            return true;
        }
        return false;
    }
}
