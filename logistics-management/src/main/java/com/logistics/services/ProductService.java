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
}
