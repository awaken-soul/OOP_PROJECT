package com.logistics.models;
import com.logistics.services.UserService;
// Note: This class remains an OOP model. 
// The 'login' method will be implemented to call the UserService for DB interaction.

/**
 * Represents a Customer (general user) in the system. 
 * Inherits common attributes from User and implements customer-specific functions.
 */
public class Customer extends User {

    // --- CONSTRUCTORS ---

    /**
     * Registration Constructor (New User): Used when a customer is registering.
     * @param name The customer's full name.
     * @param email The login email.
     * @param passwordHash The pre-calculated HASH of the customer's password.
     */
    public Customer(String name, String email, String passwordHash, String contactNumber, String address) {
        // Calls the User registration constructor, setting the role to "Customer"
        super(name, email, passwordHash, "Customer", contactNumber, address);
    }
    
    /**
     * Full Constructor (DB Retrieval): Used when fetching an existing customer from the database.
     */
    public Customer(int userID, String name, String email, String passwordHash, String contactNumber, String address) {
        // Calls the full User constructor, setting the role to "Customer"
        super(userID, name, email, passwordHash, "Customer", contactNumber, address);
    }

    // --- CUSTOMER-SPECIFIC FUNCTIONALITY (Delegates to Service Layer) ---

    /**
     * Allows the customer to place a new product order, shipment request, or transportation service request.
     * Corresponds to 'placeOrder()' in the Class Diagram and 'Request Service' use case[cite: 164, 29, 223].
     */
    public void placeOrder(String orderType, String details) {
        // In the final system, this calls OrderService.placeNewOrder(...)
        System.out.println("Customer " + getName() + " requested a service:");
        System.out.println("  Type: " + orderType + ", Details: " + details);
    }

    /**
     * Allows the customer to track the status of an existing delivery[cite: 164, 223].
     * Corresponds to 'trackOrder()' in the Class Diagram.
     */
    public String trackOrder(int orderID) {
        // In the final system, this calls OrderService.getTrackingStatus(orderID)
        return "Customer is checking status for Order ID: " + orderID;
    }

    /**
     * Allows the customer to record a payment for an order[cite: 165, 31, 223].
     * Corresponds to 'makePayment()' in the Class Diagram.
     */
    public void makePayment(int orderID, double amount, String method) {
        // In the final system, this calls PaymentService.recordPayment(orderID, amount, method)
        System.out.println(getName() + " initiated payment of $" + amount + " via " + method + " for Order " + orderID);
    }


    // --- ABSTRACT METHOD IMPLEMENTATIONS ---

    /**
     * Handles the login process by calling the central UserService for authentication.
     */
    @Override
    public boolean login(String enteredEmail, String enteredPassword) {
        UserService service = new UserService();
        User authenticatedUser = service.loginUser(enteredEmail, enteredPassword);
        
        // Check if authentication succeeded AND the retrieved role matches the expected role
        if (authenticatedUser != null && authenticatedUser.getRole().equals(this.getRole())) {
            // If successful, update the current object's state (essential for the session)
            // Note: For simplicity, the GUI will handle the session object directly from the service return.
            System.out.println(this.getRole() + " login via service succeeded.");
            return true;
        }
        return false;
    }
    /**
     * Handles the customer logout process.
     */
    @Override
    public void logout() {
        System.out.println("Customer " + getName() + " logged out successfully.");
    }
}
