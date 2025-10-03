package com.logistics.models;

public abstract class User {
    
    protected int userID;
    protected String name;
    protected String email;
    protected String password; // Changed from passwordHash to store plain text
    protected String role;
    protected String contactNumber;
    protected String address;

    // --- CONSTRUCTORS ---

    // 1. Full Constructor (Used when retrieving an existing user from the DB)
    public User(int userID, String name, String email, String password, String role, String contactNumber, String address) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password; // Now stores plain password
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
    }
    
    // 2. Registration Constructor (Used when creating a new user object before DB insertion)
    public User(String name, String email, String password, String role, String contactNumber, String address) {
        this.name = name;
        this.email = email;
        this.password = password; // Now stores plain password
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
        this.userID = -1;
    }

    // --- ABSTRACT METHODS ---
    public abstract boolean login(String enteredEmail, String enteredPassword);
    public abstract void logout();

    // --- GETTERS & SETTERS ---
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; } // Getter for plain password
    public String getRole() { return role; }
    public String getContactNumber() { return contactNumber; } // <-- FIX 1
    public String getAddress() { return address; }         // <-- FIX 1
    // ... (All other getters/setters remain the same)
    
    public void setUserID(int userID) { this.userID = userID; }
    public void setPassword(String password) { this.password = password; }
}
