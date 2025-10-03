package com.logistics.database;

import com.logistics.models.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    private final Connection connection;

    public ComplaintDAO() {
        this.connection = DBConnector.getInstance().getConnection();
    }

    public boolean save(Complaint complaint) {
        String sql = "INSERT INTO complaints(user_id, order_id, subject, description, status) VALUES(?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, complaint.getUserId());
            if (complaint.getOrderId()!= null) {
                pstmt.setInt(2, complaint.getOrderId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, complaint.getSubject());
            pstmt.setString(4, complaint.getDescription());
            pstmt.setString(5, complaint.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving complaint.", e);
        }
    }

    public List<Complaint> findAll() {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM complaints ORDER BY created_at DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                complaints.add(mapRowToComplaint(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all complaints.", e);
        }
        return complaints;
    }

    public boolean updateStatus(int complaintId, String newStatus) {
        String sql = "UPDATE complaints SET status =? WHERE complaint_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, complaintId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating complaint status.", e);
        }
    }

    private Complaint mapRowToComplaint(ResultSet rs) throws SQLException {
        // Handle nullable order_id
        int orderIdInt = rs.getInt("order_id");
        Integer orderId = rs.wasNull()? null : orderIdInt;

        return new Complaint(
                rs.getInt("complaint_id"),
                rs.getInt("user_id"),
                orderId,
                rs.getString("subject"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
