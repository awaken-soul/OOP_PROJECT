package com.logistics.models;

// Note: This class remains an abstract model. It does not contain direct JDBC or Swing code.

/**
 * The abstract base class for all user roles (Customer, Admin, Agent, Manager) 
 * in the Logistics and Delivery Management System.
 */
public abstract class User {
    
    // Core Attributes from Class and DB Diagrams
    protected int userID;
    protected String name;
    protected String email;
    protected String passwordHash; // Stores the secure hash of the password
    protected String role;
    protected String contactNumber;
    protected String address;

    // --- CONSTRUCTORS ---

    /**
     * 1. Full Constructor (Used when retrieving an existing user from the DB)
     */
    public User(int userID, String name, String email, String passwordHash, String role, String contactNumber, String address) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash; 
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
    }
    
    /**
     * 2. Registration Constructor (Used when creating a new user object before DB insertion)
     * The DB will generate the userID, so we don't include it here.
     * The role is passed in by the specific subclass constructor (e.g., Customer).
     */
    public User(String name, String email, String passwordHash, String role, String contactNumber, String address) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash; 
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
        this.userID = -1; // Indicate that the ID has not been assigned by the database yet
    }


    // --- ABSTRACT METHODS ---

    public abstract boolean login(String enteredEmail, String enteredPassword);
    public abstract void logout();


    // --- GETTERS ---
    
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }


    // --- SETTERS ---

    public void setUserID(int userID) { this.userID = userID; } // Needed after DB insertion
    public void setName(String name) { this.name = name; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setPasswordHash(String newPasswordHash) { this.passwordHash = newPasswordHash; }


    // --- UTILITY METHODS ---

    /**
     * Overrides the default toString() for easy logging and debugging.
     */
    @Override
    public String toString() {
        return "User {" +
               "ID=" + userID +
               ", Name='" + name + '\'' +
               ", Email='" + email + '\'' +
               ", Role='" + role + '\'' +
               ", Contact='" + (contactNumber != null ? contactNumber : "N/A") + '\'' +
               '}';
    }
}
