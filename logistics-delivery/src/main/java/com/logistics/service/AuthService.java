package com.logistics.service;

import com.logistics.data.UserDao;
import com.logistics.model.User;

/**
 * Service layer for handling business logic related to authentication (login/signup).
 */
public class AuthService {

    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Handles the business logic for user login.
     * @param email The user's email.
     * @param password The user's password.
     * @return The User object if login is successful, otherwise null.
     */
    public User login(String email, String password) {
        // Basic validation
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return userDao.findByEmailAndPassword(email, password);
    }

    /**
     * Handles the business logic for user registration.
     * @param email The user's email.
     * @param password The user's password.
     * @param role The role of the new user (e.g., "CUSTOMER").
     * @return true if the user was created successfully, false otherwise (e.g., email already exists).
     */
    public boolean signUp(String email, String password, String role) {
        // Basic validation
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        // Delegate user creation to the DAO
        return userDao.createUser(email, password, role);
    }
}

