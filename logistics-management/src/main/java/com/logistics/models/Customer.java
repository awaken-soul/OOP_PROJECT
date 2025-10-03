package com.logistics.models;

import com.logistics.services.UserService;

public class Customer extends User {

    public Customer(String name, String email, String password, String contactNumber, String address, int is_validated) {
        super(name, email, password, "Customer", contactNumber, address, is_validated);
    }
    
    public Customer(int userID, String name, String email, String password, String contactNumber, String address, int is_validated) {
        super(userID, name, email, password, "Customer", contactNumber, address, is_validated);
    }

    public void placeOrder(String orderType, String details) {
        System.out.println("Customer " + getName() + " requested a service: " + orderType);
    }

    public String trackOrder(int orderID) {
        return "Customer is checking status for Order ID: " + orderID;
    }

    public void makePayment(int orderID, double amount, String method) {
        System.out.println(getName() + " initiated payment of $" + amount + " via " + method + " for Order " + orderID);
    }

    @Override
    public boolean login(String enteredEmail, String enteredPassword) {
        UserService service = new UserService();
        User authenticatedUser = service.loginUser(enteredEmail, enteredPassword);
        
        if (authenticatedUser != null && authenticatedUser.getRole().equals(this.getRole())) {
            System.out.println(this.getRole() + " login via service succeeded.");
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        System.out.println("Customer " + getName() + " logged out successfully.");
    }
}
