package com.logistics.models;

public abstract class User {
    
    protected int userID;
    protected String name;
    protected String email;
    protected String password;
    protected String role;
    protected String contactNumber;
    protected String address;
    
    public User(int userID, String name, String email, String password, String role, String contactNumber, String address) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
    }
    
    public User(String name, String email, String password, String role, String contactNumber, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactNumber = contactNumber;
        this.address = address;
        this.userID = -1;
    }


    public abstract boolean login(String enteredEmail, String enteredPassword);
    public abstract void logout();

    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }      
    
    public void setUserID(int userID) { this.userID = userID; }
    public void setPassword(String password) { this.password = password; }
}
