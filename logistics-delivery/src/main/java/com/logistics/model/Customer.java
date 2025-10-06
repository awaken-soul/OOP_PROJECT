package com.logistics.model;

/**
 * Represents a Customer user who can place shipment requests.
 */
public class Customer extends User {
    public Customer(int id, String email, String password) {
        super(id, email, password, "CUSTOMER");
    }
}
