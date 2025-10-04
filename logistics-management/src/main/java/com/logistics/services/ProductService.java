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

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productDAO.findAll().stream()
                .filter(product -> product.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    public boolean updateProductQuantity(int productId, int newQuantity) {
        if (newQuantity < 0) return false;
        return productDAO.updateQuantity(productId, newQuantity);
    }

    public Optional<Product> getProductById(int productId) {
        return productDAO.findById(productId);
    }
}
