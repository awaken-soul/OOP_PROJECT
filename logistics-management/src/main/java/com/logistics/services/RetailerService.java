package com.logistics.services;

import com.logistics.database.RetailerDAO;
import com.logistics.models.Retailer;

import java.util.List;
import java.util.Optional;

public class RetailerService {

    private final RetailerDAO retailerDAO;

    public RetailerService(RetailerDAO retailerDAO) {
        this.retailerDAO = retailerDAO;
    }

    public List<Retailer> getAllRetailers() {
        return retailerDAO.findAll();
    }

    public boolean addRetailer(Retailer retailer) {
        Optional<Integer> generatedId = retailerDAO.save(retailer);
        return generatedId.isPresent();
    }

    public boolean updateRetailer(Retailer retailer) {
        return retailerDAO.update(retailer);
    }

    public boolean deleteRetailer(Retailer retailer) {
        return retailerDAO.delete(retailer);
    }
}
