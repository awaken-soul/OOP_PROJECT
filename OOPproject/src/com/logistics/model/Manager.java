package com.logistics.model;

public class Manager {
    private int managerId;
    private String name;
    private String email;
    private String contactNumber;
    private String address;

    public Manager() {}

    public Manager(int managerId, String name, String email, String contactNumber, String address) {
        this.managerId = managerId;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    // --- Getters ---
    public int getManagerId() {
        return managerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    // --- Setters ---
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + " (" + managerId + ")";
    }
}
