package com.logistics.model;

/**
 * Represents a single, physical item that is being shipped.
 */
public class Product {
    private int id;
    private String name;
    private String description;

    public Product(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Provides a user-friendly string representation for UI components.
     * @return The name of the product.
     */
    @Override
    public String toString() {
        return name;
    }
}

