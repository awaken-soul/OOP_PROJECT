package com.logistics.service;

import com.logistics.data.ProductDao;
import com.logistics.data.ShipmentDao;
import com.logistics.model.Shipment;

import java.util.List;

/**
 * Service layer for handling business logic related to Customer operations.
 */
public class CustomerService {

    private final ProductDao productDao;
    private final ShipmentDao shipmentDao;

    public CustomerService(ProductDao productDao, ShipmentDao shipmentDao) {
        this.productDao = productDao;
        this.shipmentDao = shipmentDao;
    }

    /**
     * Handles the business logic for placing a new shipment request.
     * It first creates a product, then creates a shipment linked to that product.
     * @param productName The name of the product.
     * @param productDescription The description of the product.
     * @param source The source address.
     * '
     * @param destination The destination address.
     * @param customerId The ID of the customer placing the order.
     * @return true if the shipment was placed successfully.
     */
    public boolean placeShipmentRequest(String productName, String productDescription, String source, String destination, int customerId) {
        // First, create the product and get its ID.
        int productId = productDao.createProductAndGetId(productName, productDescription);

        // If product creation was successful (ID is not -1), create the shipment.
        if (productId != -1) {
            return shipmentDao.createShipment(customerId, productId, source, destination);
        }

        // If product creation failed, the whole operation fails.
        return false;
    }

    /**
     * Retrieves all shipments belonging to a specific customer.
     * @param customerId The ID of the customer.
     * @return A list of the customer's shipments.
     */
    public List<Shipment> getMyShipments(int customerId) {
        return shipmentDao.findByCustomerId(customerId);
    }
}

