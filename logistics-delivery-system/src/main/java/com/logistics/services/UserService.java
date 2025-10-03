package com.logistics.services;

import com.logistics.database.UserDAO;
import com.logistics.models.User;
// import com.logistics.utils.PasswordHasher; // REMOVED

/**
 * Handles all business logic related to User accounts, including registration and authentication.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Registers a new user. The plain password is now stored directly in the DB.
     */
    public User registerUser(User user, String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            System.err.println("Registration failed: Password cannot be empty.");
            return null;
        }
        // UserDAO now handles direct storage of plainPassword
        boolean success = userDAO.registerNewUser(user, plainPassword);

        if (success) {
            System.out.println("User " + user.getName() + " registered successfully as " + user.getRole() + ".");
            return user;
        } else {
            return null;
        }
    }

    /**
     * Authenticates a user using direct plain password comparison.
     */
    public User loginUser(String email, String plainPassword) {
        
        // 1. Retrieve the user record from the database by email
        User userFromDB = userDAO.getUserByEmail(email);

        if (userFromDB == null) {
            System.out.println("Login Failed: User not found with email: " + email);
            return null;
        }

        // 2. Verify the entered plain password against the stored plain password
        if (userFromDB.getPassword().equals(plainPassword)) { // Direct String Comparison (INSECURE but required)
            
            System.out.println("Login Successful for " + userFromDB.getRole() + ": " + userFromDB.getName());
            
            return userFromDB;
        } else {
            System.out.println("Login Failed: Incorrect password for user: " + email);
            return null;
        }
    }
    
    // ... (Other methods remain the same)
    public boolean updateContactInfo(int userID, String newContact, String newAddress) {
        System.out.println("User profile update initiated for ID: " + userID);
        return true; 
    }
}
