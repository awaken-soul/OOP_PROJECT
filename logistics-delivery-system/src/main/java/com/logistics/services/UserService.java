package com.logistics.services;

import com.logistics.database.UserDAO;
import com.logistics.models.User;
import com.logistics.utils.PasswordHasher;

/**
 * Handles all business logic related to User accounts, including registration, 
 * authentication (login), and profile management.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // --- Core Functional Requirement: User Registration ---

    /**
     * Registers a new user (Customer, Agent, Admin, etc.) into the system.
     * This method handles the hashing of the password before calling the DAO.
     * * @param user The new User Model object (must have role set).
     * @param plainPassword The plain text password entered by the user.
     * @return The fully registered User object (with the generated ID), or null if registration failed.
     */
    public User registerUser(User user, String plainPassword) {
        // 1. Basic validation (e.g., check if password is empty)
        if (plainPassword == null || plainPassword.isEmpty()) {
            System.err.println("Registration failed: Password cannot be empty.");
            return null;
        }

        // The UserDAO handles the hashing and database insertion.
        boolean success = userDAO.registerNewUser(user, plainPassword);

        if (success) {
            System.out.println("User " + user.getName() + " registered successfully as " + user.getRole() + ".");
            return user;
        } else {
            // Error handling is managed inside DAO (e.g., email unique constraint)
            return null;
        }
    }

    // --- Core Functional Requirement: User Login/Authentication ---

    /**
     * Authenticates a user based on email and password.
     * * @param email The email entered by the user.
     * @param plainPassword The password entered by the user.
     * @return The authenticated User object if successful, or null otherwise.
     */
    public User loginUser(String email, String plainPassword) {
        
        // 1. Retrieve the user record from the database by email
        User userFromDB = userDAO.getUserByEmail(email);

        if (userFromDB == null) {
            System.out.println("Login Failed: User not found with email: " + email);
            return null;
        }

        // 2. Verify the entered plain password against the stored hash
        if (PasswordHasher.verifyPassword(plainPassword, userFromDB.getPasswordHash())) {
            
            System.out.println("Login Successful for " + userFromDB.getRole() + ": " + userFromDB.getName());
            
            // 3. Return the fully loaded User object
            return userFromDB;
        } else {
            System.out.println("Login Failed: Incorrect password for user: " + email);
            return null;
        }
    }
    
    // --- Additional Profile Management Example ---

    /**
     * Updates a user's contact information in both the object and the database.
     */
    public boolean updateContactInfo(int userID, String newContact, String newAddress) {
        // In a real implementation, this would involve a specific DAO update method.
        System.out.println("User profile update initiated for ID: " + userID);
        // userDAO.updateContact(userID, newContact, newAddress);
        return true; 
    }
}
