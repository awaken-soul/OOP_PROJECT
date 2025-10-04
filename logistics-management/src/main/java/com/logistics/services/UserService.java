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

    public boolean registerUser(User user) {
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            System.err.println("Registration failed: Email already in use.");
            return false;
        }
        return userDAO.save(user).isPresent();
    }

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

    public List<User> getAvailableAgents() {
        return userDAO.findAvailableAgents();
    }
}
