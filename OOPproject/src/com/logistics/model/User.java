package com.logistics.model;

/**
 * Represents a User entity mapped to the 'User' table in the database.
 */
public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String contactNumber;
    private String address;

    // --- Constructors ---
    public User() {
        // Default constructor
    }

    public User(String name, String email, String password, String role, String contactNumber, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public User(int userId, String name, String email, String password, String role, String contactNumber, String address) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // --- Getters and Setters ---
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // --- Utility ---
    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s', role='%s', contact='%s', address='%s'}",
                userId, name, email, role, contactNumber, address);
    }
}
