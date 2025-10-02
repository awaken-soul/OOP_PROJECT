//create all tables

package logistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateAllTables {

    private static final String DB_URL = "jdbc:sqlite:logistics.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // 1. User Table
            String userTable = "CREATE TABLE IF NOT EXISTS User (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT CHECK(role IN ('Customer','Admin','Agent','Manager')) NOT NULL," +
                    "contact_number TEXT," +
                    "address TEXT" +
                    ");";
            stmt.execute(userTable);

            // 2. Retailer Table
            String retailerTable = "CREATE TABLE IF NOT EXISTS Retailer (" +
                    "retailer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "contact_number TEXT" +
                    ");";
            stmt.execute(retailerTable);

            // 3. Warehouse Table
            String warehouseTable = "CREATE TABLE IF NOT EXISTS Warehouse (" +
                    "warehouse_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "capacity INTEGER," +
                    "manager_id INTEGER," +
                    "FOREIGN KEY(manager_id) REFERENCES User(user_id)" +
                    ");";
            stmt.execute(warehouseTable);

            // 4. Product Table
            String productTable = "CREATE TABLE IF NOT EXISTS Product (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT," +
                    "price REAL NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "warehouse_id INTEGER," +
                    "retailer_id INTEGER," +
                    "FOREIGN KEY(warehouse_id) REFERENCES Warehouse(warehouse_id)," +
                    "FOREIGN KEY(retailer_id) REFERENCES Retailer(retailer_id)" +
                    ");";
            stmt.execute(productTable);

            // 5. Vehicle Table
            String vehicleTable = "CREATE TABLE IF NOT EXISTS Vehicle (" +
                    "vehicle_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "vehicle_type TEXT CHECK(vehicle_type IN ('Truck','Van','Bike')) NOT NULL," +
                    "license_plate TEXT UNIQUE NOT NULL," +
                    "status TEXT CHECK(status IN ('Available','On Delivery','Maintenance')) NOT NULL," +
                    "driver_id INTEGER," +
                    "current_location TEXT," +
                    "FOREIGN KEY(driver_id) REFERENCES User(user_id)" +
                    ");";
            stmt.execute(vehicleTable);

            // 6. Orders Table
            String ordersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "product_id INTEGER," +
                    "order_type TEXT CHECK(order_type IN ('Purchase','Shipment','Transport')) NOT NULL," +
                    "source_address TEXT NOT NULL," +
                    "destination_address TEXT NOT NULL," +
                    "status TEXT CHECK(status IN ('Pending','In Warehouse','Out for Delivery','Delivered')) NOT NULL," +
                    "assigned_agent_id INTEGER," +
                    "vehicle_id INTEGER," +
                    "payment_status TEXT CHECK(payment_status IN ('Pending','Paid','COD')) NOT NULL," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(user_id) REFERENCES User(user_id)," +
                    "FOREIGN KEY(product_id) REFERENCES Product(product_id)," +
                    "FOREIGN KEY(assigned_agent_id) REFERENCES User(user_id)," +
                    "FOREIGN KEY(vehicle_id) REFERENCES Vehicle(vehicle_id)" +
                    ");";
            stmt.execute(ordersTable);

            // 7. Payment Table
            String paymentTable = "CREATE TABLE IF NOT EXISTS Payment (" +
                    "payment_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER NOT NULL," +
                    "amount REAL NOT NULL," +
                    "method TEXT CHECK(method IN ('Cash','Card','UPI')) NOT NULL," +
                    "status TEXT CHECK(status IN ('Pending','Completed')) NOT NULL," +
                    "payment_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(order_id) REFERENCES Orders(order_id)" +
                    ");";
            stmt.execute(paymentTable);

            // 8. Tracking Table
            String trackingTable = "CREATE TABLE IF NOT EXISTS Tracking (" +
                    "tracking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER NOT NULL," +
                    "agent_id INTEGER," +
                    "current_status TEXT CHECK(current_status IN ('Picked Up','In Warehouse','Out for Delivery','Delivered')) NOT NULL," +
                    "location TEXT," +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(order_id) REFERENCES Orders(order_id)," +
                    "FOREIGN KEY(agent_id) REFERENCES User(user_id)" +
                    ");";
            stmt.execute(trackingTable);

            System.out.println("All tables created successfully!");
            
            
         // --- Insert sample data ---

         // 1. Insert Users
         stmt.executeUpdate("INSERT INTO User (name, email, password, role, contact_number, address) VALUES " +
                 "('Alice', 'alice@example.com', 'pass123', 'Customer', '9876543210', '123 Main Street')," +
                 "('Bob', 'bob@example.com', 'pass123', 'Admin', '8765432109', 'Admin Office')," +
                 "('Charlie', 'charlie@example.com', 'pass123', 'Agent', '7654321098', 'Agent Hub')," +
                 "('Diana', 'diana@example.com', 'pass123', 'Manager', '6543210987', 'Warehouse HQ');");

         // 2. Insert Retailers
         stmt.executeUpdate("INSERT INTO Retailer (name, address, contact_number) VALUES " +
                 "('TechRetail', '45 Retail Park', '9123456780')," +
                 "('BookWorld', '78 Market Street', '9234567890');");

         // 3. Insert Warehouses
         stmt.executeUpdate("INSERT INTO Warehouse (name, address, capacity, manager_id) VALUES " +
                 "('Central Warehouse', 'Warehouse Road 1', 1000, 4)," +   // managed by Diana (user_id=4)
                 "('North Warehouse', 'Warehouse Road 2', 500, 4);");

         // 4. Insert Products
         stmt.executeUpdate("INSERT INTO Product (name, description, price, quantity, warehouse_id, retailer_id) VALUES " +
                 "('Laptop', 'HP ProBook 450', 55000, 20, 1, 1)," +
                 "('Smartphone', 'iPhone 16', 120000, 15, 1, 1)," +
                 "('Book', 'Harry Potter Collection', 800, 50, 2, 2);");

         // 5. Insert Vehicles
         stmt.executeUpdate("INSERT INTO Vehicle (vehicle_type, license_plate, status, driver_id, current_location) VALUES " +
                 "('Truck', 'KL01AB1234', 'Available', 3, 'Central Warehouse')," +  // assigned to Charlie (agent)
                 "('Van', 'KL02CD5678', 'Maintenance', NULL, 'Garage')," +
                 "('Bike', 'KL03EF9012', 'On Delivery', 3, 'City Center');");

         // 6. Insert Orders
         stmt.executeUpdate("INSERT INTO Orders (user_id, product_id, order_type, source_address, destination_address, status, assigned_agent_id, vehicle_id, payment_status) VALUES " +
                 "(1, 1, 'Purchase', 'Central Warehouse', '123 Main Street', 'Pending', 3, 1, 'Pending')," +  // Alice buying Laptop
                 "(1, 3, 'Purchase', 'North Warehouse', '123 Main Street', 'In Warehouse', 3, 3, 'Pending')," +
                 "(2, NULL, 'Transport', '45 Retail Park', '78 Market Street', 'Out for Delivery', 3, 1, 'Pending');");

         // 7. Insert Payments
         stmt.executeUpdate("INSERT INTO Payment (order_id, amount, method, status) VALUES " +
                 "(1, 55000, 'UPI', 'Pending')," +
                 "(2, 800, 'Card', 'Completed');");

         // 8. Insert Tracking
         stmt.executeUpdate("INSERT INTO Tracking (order_id, agent_id, current_status, location) VALUES " +
                 "(1, 3, 'Picked Up', 'Central Warehouse')," +
                 "(2, 3, 'In Warehouse', 'North Warehouse')," +
                 "(3, 3, 'Out for Delivery', 'City Center');");

         System.out.println("Sample data inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
