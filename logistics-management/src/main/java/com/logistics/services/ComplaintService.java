package com.logistics.services;

import com.logistics.database.ComplaintDAO;
import com.logistics.models.Complaint;

import java.util.List;

public class ComplaintService {

    private final ComplaintDAO complaintDAO;

    public ComplaintService(ComplaintDAO complaintDAO) {
        this.complaintDAO = complaintDAO;
    }

    /**
     * Allows a user to submit a new complaint.
     * @param complaint The Complaint object to be saved.
     * @return true if the complaint was submitted successfully, false otherwise.
     */
    public boolean submitComplaint(Complaint complaint) {
        return complaintDAO.save(complaint);
    }

    /**
     * Retrieves all complaints from the system, typically for an admin to view.
     * @return A list of all Complaint objects.
     */
    public List<Complaint> getAllComplaints() {
        return complaintDAO.findAll();
    }

    /**
     * Allows an admin to resolve a complaint by updating its status.
     * @param complaintId The ID of the complaint to resolve.
     * @return true if the status was updated successfully, false otherwise.
     */
    public boolean resolveComplaint(int complaintId) {
        return complaintDAO.updateStatus(complaintId, "Resolved");
    }
}
