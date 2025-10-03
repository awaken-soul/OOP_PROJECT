package com.logistics.models;

public abstract class User {
    
    protected int userID;
    protected String name;
    protected String email;
    protected String password;
    protected String role;
    protected String contactNumber;
    protected String address;
    protected int is_validated; // 1 for true, 0 for false

    // 1. Full Constructor
    public User(int userID, String name, String email, String password, String role, String contactNumber, String address, int is_validated) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
        this.is_validated = is_validated;
    }
    
    // 2. Registration Constructor
    public User(String name, String email, String password, String role, String contactNumber, String address, int is_validated) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
        this.is_validated = is_validated;
        this.userID = -1;
    }

    public abstract boolean login(String enteredEmail, String enteredPassword);
    public abstract void logout();

    // Getters
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }
    public boolean isValidated() { return is_validated == 1; }
    
    // Setters
    public void setUserID(int userID) { this.userID = userID; }
}
