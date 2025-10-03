package com.logistics.services;

import com.logistics.database.UserDAO;
import com.logistics.models.User;
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public User registerUser(User user, String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            System.err.println("Registration failed: Password cannot be empty.");
            return null;
        }
        boolean success = userDAO.registerNewUser(user, plainPassword);

        if (success) {
            System.out.println("User " + user.getName() + " registered successfully as " + user.getRole() + ".");
            return user;
        } else {
            return null;
        }
    }

    public User loginUser(String email, String plainPassword) {
        
      
        User userFromDB = userDAO.getUserByEmail(email);

        if (userFromDB == null) {
            System.out.println("Login Failed: User not found with email: " + email);
            return null;
        }

        if (userFromDB.getPassword().equals(plainPassword)) { 
            System.out.println("Login Successful for " + userFromDB.getRole() + ": " + userFromDB.getName());
            return userFromDB;
        } else {
            System.out.println("Login Failed: Incorrect password for user: " + email);
            return null;
        }
    }
    
    public boolean updateContactInfo(int userID, String newContact, String newAddress) {
        System.out.println("User profile update initiated for ID: " + userID);
        return true; 
    }
}
