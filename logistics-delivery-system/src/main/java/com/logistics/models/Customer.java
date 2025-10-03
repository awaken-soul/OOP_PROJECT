package com.logistics.models;
import com.logistics.services.UserService;

public class Customer extends User {
    
    public Customer(String name, String email, String passwordHash, String contactNumber, String address) {
        // Calls the User registration constructor, setting the role to "Customer"
        super(name, email, passwordHash, "Customer", contactNumber, address);
    }
    
    public Customer(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        // Calls the full User constructor, setting the role to "Customer"
        super(userID, name, email, passwordHash, "Customer", contactNumber, address);
    }

    public void placeOrder(String orderType, String details) {
        // In the final system, this calls OrderService.placeNewOrder(...)
        System.out.println("Customer " + getName() + " requested a service:");
        System.out.println("  Type: " + orderType + ", Details: " + details);
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
