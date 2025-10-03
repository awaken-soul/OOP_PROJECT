package com.logistics.services;

import com.logistics.database.UserDAO;
import com.logistics.models.User;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user after checking if the email is already in use.
     * @param user The user object to be registered.
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerUser(User user) {
        // Business Rule: Check if a user with this email already exists.
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            System.err.println("Registration failed: Email already in use.");
            return false;
        }
        /**
     * Retrieves a list of all users who are delivery agents.
     * @return A list of User objects with the AGENT role.
     */
    public List<User> getAvailableAgents() {
        return userDAO.findAvailableAgents();
    }
        // Proceed with saving the new user.
        return userDAO.save(user);
    }

    /**
     * Authenticates a user by checking their email and password.
     * @param email The user's email.
     * @param plainPassword The user's plain text password.
     * @return An Optional containing the User object if authentication is successful,
     *         otherwise an empty Optional.
     */
    public Optional<User> authenticate(String email, String plainPassword) {
        Optional<User> userOptional = userDAO.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Verify the provided password against the stored plain text password.
            if (plainPassword.equals(user.getPassword())) {
                return Optional.of(user); // Authentication successful
            }
        }

        return Optional.empty(); // Authentication failed
    }
}
