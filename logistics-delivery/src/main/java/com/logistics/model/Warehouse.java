package com.logistics.model;

/**
 * Represents a physical warehouse location.
 */
public class Warehouse {
    private int id;
    private String name;
    private String location;

    public Warehouse(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Provides a descriptive string representation for UI components.
     * @return A formatted string with the warehouse name and location.
     */
    @Override
    public String toString() {
        return String.format("%s - %s", name, location);
    }
}

