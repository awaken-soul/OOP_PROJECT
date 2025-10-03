package com.logistics.services;

import com.logistics.database.ProductDAO;
import com.logistics.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Retrieves all available products.
     * In a real application, this could filter out-of-stock items.
     * @return A list of all Product objects.
     */
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Retrieves all products that are currently in stock.
     * @return A list of in-stock Product objects.
     */
    public List<Product> getAvailableProducts() {
        return productDAO.findAll().stream()
               .filter(product -> product.getQuantity() > 0)
               .collect(Collectors.toList());
    }
    /**
     * Updates the stock quantity for a specific product.
     * @param productId The ID of the product to update.
     * @param newQuantity The new quantity to set.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateProductQuantity(int productId, int newQuantity) {
        // Business logic, like checking for negative quantities, could be added here.
        if (newQuantity < 0) {
            return false; // Prevent setting negative stock
        }
        return productDAO.updateQuantity(productId, newQuantity);
    }
}
