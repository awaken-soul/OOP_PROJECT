package logistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Fetch all active orders of a user
    public List<Order> getActiveOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getString("order_type"),
                        rs.getString("source_address"),
                        rs.getString("destination_address"),
                        rs.getString("status"),
                        rs.getString("payment_status")
                );
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}
