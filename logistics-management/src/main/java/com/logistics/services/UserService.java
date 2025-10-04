package com.logistics.services;

import com.logistics.database.UserDAO;
import com.logistics.models.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user.
     * @param user User object to register.
     * @return true if registration is successful, false if email already exists.
     */
    public boolean registerUser(User user) {
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            System.err.println("Registration failed: Email already in use.");
            return false;
        }
        Optional<Integer> generatedId = userDAO.save(user);
        return generatedId.isPresent();
    }

    /**
     * Authenticates a user by email and password.
     * @param email Email of the user.
     * @param plainPassword Plain-text password.
     * @return Optional containing the User if authentication is successful.
     */
    public Optional<User> authenticate(String email, String plainPassword) {
        Optional<User> userOptional = userDAO.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (plainPassword.equals(user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves all users with the role of AGENT.
     * @return List of available agent users.
     */
    public List<User> getAvailableAgents() {
        return userDAO.findAvailableAgents();
    }

    /**
     * Updates user information.
     * @param user User object with updated details.
     * @return true if update is successful.
     */
    public boolean updateUser(User user) {
        return userDAO.update(user);
    }

    /**
     * Deletes a user.
     * @param user User to delete.
     * @return true if deletion is successful.
     */
    public boolean deleteUser(User user) {
        return userDAO.delete(user);
    }
}
