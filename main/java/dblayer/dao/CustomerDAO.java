package dblayer.dao;

import dblayer.model.Customer;
import dblayer.model.Nominee;
import util.CustomException;

import java.util.List;

public interface CustomerDAO {
	
    void insert(Customer customer) throws CustomException;
    
    Customer getCustomer(Long userID) throws CustomException;
    
    Customer getCustomerDetail(Long userID) throws CustomException;
    
    List<Nominee> getNominees(Long userID) throws CustomException;
    
    Customer getAllCustomerDetails(Long userID) throws CustomException;
    
    <T> void update(Long userID, Long performerID, String table, String column, T value) throws CustomException;
    
    void delete(Long userID) throws CustomException;
    
    List<Customer> findAll() throws CustomException;
    
}