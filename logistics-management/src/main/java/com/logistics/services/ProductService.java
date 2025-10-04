package com.logistics.services;

import com.logistics.database.ProductDAO;
import com.logistics.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Retrieves all products from the database.
     */
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Retrieves products that are currently in stock.
     */
    public List<Product> getAvailableProducts() {
        return productDAO.findAll().stream()
                .filter(product -> product.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Updates the quantity of a product.
     */
    public boolean updateProductQuantity(int productId, int newQuantity) {
        if (newQuantity < 0) {
            return false; // Prevent negative stock
        }

        // Fetch product
        Optional<Product> productOpt = productDAO.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setQuantity(newQuantity);
            return productDAO.update(product);
        }
        return false;
    }

    /**
     * Retrieves a product by its ID.
     */
    public Optional<Product> getProductById(int productId) {
        return productDAO.findById(productId);
    }

    /**
     * Adds a new product to the database.
     */
    public boolean addProduct(Product product) {
        Optional<Integer> generatedId = productDAO.save(product);
        if (generatedId.isPresent()) {
            product.setProductId(generatedId.get());
            return true;
        }
        return false;
    }

    /**
     * Deletes a product by its ID.
     */
    public boolean deleteProduct(int productId) {
        Optional<Product> productOpt = productDAO.findById(productId);
        if (productOpt.isPresent()) {
            return productDAO.delete(productOpt.get());
        }
        return false;
    }
}
