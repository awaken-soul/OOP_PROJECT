package com.logistics.model;

/**
 * Represents an Agent user who can view and manage shipments.
 */
public class Agent extends User {
    public Agent(int id, String email, String password) {
        super(id, email, password, "AGENT");
    }
}

