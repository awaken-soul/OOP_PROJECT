package com.logistics.models;

public class Retailer {
    private int retailerId;
    private String name;
    private String address;
    private String contactNumber;

    public Retailer(String name, String address, String contactNumber) {
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }
        
    public Retailer(int retailerId, String name, String address, String contactNumber) {
        this.retailerId = retailerId;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    // Getters and Setters
    public int getRetailerId() { return retailerId; }
    public void setRetailerId(int retailerId) { this.retailerId = retailerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
