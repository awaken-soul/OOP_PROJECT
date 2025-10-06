package com.logistics.model;

/**
 * Represents a generic user in the system.
 * This class is the base for more specific user roles like Customer, Agent, and Admin.
 */
public abstract class User {
    protected int id;
    protected String email;
    protected String password;
    protected String role;

    public User(int id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
