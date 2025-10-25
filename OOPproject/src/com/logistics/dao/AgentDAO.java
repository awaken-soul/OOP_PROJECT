package com.logistics.dao;

import com.logistics.model.Agent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Agents table.
 */
public class AgentDAO {

    public boolean createAgent(Agent agent) {
        String sql = "INSERT INTO Agents (name, email, contact_number) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, agent.getName());
            stmt.setString(2, agent.getEmail());
            stmt.setString(3, agent.getContactNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating agent: " + e.getMessage());
            return false;
        }
    }

    public List<Agent> getAllAgents() {
        List<Agent> agents = new ArrayList<>();
        String sql = "SELECT * FROM Agents";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Agent agent = new Agent();
                agent.setAgentId(rs.getInt("agent_id"));
                agent.setName(rs.getString("name"));
                agent.setEmail(rs.getString("email"));
                agent.setContactNumber(rs.getString("contact_number"));
                agents.add(agent);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching agents: " + e.getMessage());
        }
        return agents;
    }
}
