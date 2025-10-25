package com.logistics.service;

import com.logistics.dao.UserDAO;
import com.logistics.model.User;

/**
 * Handles user authentication and registration.
 * Works with User table in your logistics.db schema.
 */
public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /** ---------------------- LOGIN ---------------------- **/
    public User login(String email, String password) {
        // Fetch user from DB using email + password
        return userDAO.findByEmailAndPassword(email, password);
    }

    /** ---------------------- REGISTER / SIGNUP ---------------------- **/
    public boolean signUp(String name, String email, String password, String role,
                          String contact, String address) {
        // Insert new user into User table
        return userDAO.createUser(name, email, password, role, contact, address);
    }
}


/*package com.logistics.service;

import com.logistics.dao.UserDAO;
import com.logistics.model.User;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String email, String password) {
        return userDAO.findByEmailAndPassword(email, password);
    }

    public boolean signUp(String name, String email, String password, String contactNumber, String address, String role) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setContactNumber(contactNumber);
        newUser.setAddress(address);
        newUser.setRole(role);
        return userDAO.addUser(newUser);
    }

    // Overload for admin-created users (no address)
    public boolean signUp(String email, String password, String role) {
        return signUp("N/A", email, password, "N/A", "N/A", role);
    }
}
*/