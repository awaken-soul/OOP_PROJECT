package com.logistics.model;

/**
 * Represents an Agent record from the Agents table.
 */
public class Agent {

    private int agentId;
    private String name;
    private String email;
    private String contactNumber;

    // --- Constructors ---
    public Agent() {}

    public Agent(int agentId, String name, String email, String contactNumber) {
        this.agentId = agentId;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    // --- Getters and Setters ---
    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return String.format("Agent[id=%d, name=%s, email=%s, contact=%s]",
                agentId, name, email, contactNumber);
    }
}
