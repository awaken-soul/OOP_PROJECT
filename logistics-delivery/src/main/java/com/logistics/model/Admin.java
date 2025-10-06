package com.logistics.model;

/**
 * Represents an Admin user who can manage agents and warehouses.
 */
public class Admin extends User {
    public Admin(int id, String email, String password) {
        super(id, email, password, "ADMIN");
    }
}

