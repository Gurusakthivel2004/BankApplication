package dblayer.implementation;

import dblayer.dao.CustomerDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dblayer.connect.DBConnection;
import dblayer.model.Customer;
import dblayer.model.Nominee;
import util.CustomException;
import util.Helper;

public class CustomerDAOImp implements CustomerDAO {
	
    @Override
    public void insert(Customer customer) throws CustomException {
    	Helper.checkNullValues(customer);
    	String INSERT_CUSTOMER_SQL = "INSERT INTO customer (user_id, pan_number, aadhar_number, created_at, performed_by) VALUES (?, ?, ?, ?, ?)";
    	String INSERT_CUSTOMER_DETAIL_SQL = "INSERT INTO customerDetail (user_id, dob, father_name, mother_name, address, marital_status) VALUES (?, ?, ?, ?, ?, ?)";
        String INSERT_NOMINEE_SQL = "INSERT INTO nominee (user_id, name, relationship) VALUES (?, ?, ?)";
        
    	try (Connection connection = DBConnection.getConnection();
        PreparedStatement preparedCustomerStatement = connection.prepareStatement(INSERT_CUSTOMER_SQL);
    	PreparedStatement preparedCustomerDetailStatement = connection.prepareStatement(INSERT_CUSTOMER_DETAIL_SQL);
    	PreparedStatement preparedNomineeStatement = connection.prepareStatement(INSERT_NOMINEE_SQL)) {
    		// customer
    		preparedCustomerStatement.setLong(1, customer.getUserID());
    		preparedCustomerStatement.setString(2, customer.getPanNumber());
    		preparedCustomerStatement.setLong(3, customer.getAadharNumber());
    		preparedCustomerStatement.setLong(4, customer.getCreatedAt());
    		preparedCustomerStatement.setLong(5, customer.getPerformedBy());
    		preparedCustomerStatement.executeUpdate();
    		// customerDetail
    		preparedCustomerDetailStatement.setLong(1, customer.getUserID());
    		preparedCustomerDetailStatement.setString(2, customer.getDob());
    		preparedCustomerDetailStatement.setString(3, customer.getFatherName());
    		preparedCustomerDetailStatement.setString(4, customer.getMotherName());
    		preparedCustomerDetailStatement.setString(5, customer.getAddress());
    		preparedCustomerDetailStatement.setString(6, customer.getMaritalStatus());
    		preparedCustomerDetailStatement.executeUpdate();
    		// nominee
    		for(Nominee nominee : customer.getNominees()) {
    			preparedNomineeStatement.setLong(1, nominee.getUserId());
        		preparedNomineeStatement.setString(2, nominee.getName());
        		preparedNomineeStatement.setString(3, nominee.getRelationship());
        		preparedNomineeStatement.executeUpdate();
    		}
    		
        } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
        }
    }

    @Override
    public Customer getCustomer(Long userID) throws CustomException {
        Helper.checkNullValues(userID);
        Customer customer = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customer WHERE user_id = ?")) {
            preparedStatement.setLong(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
            	customer = new Customer();
                customer.setUserID(rs.getLong("user_id"));
                customer.setPanNumber(rs.getString("pan_number"));
                customer.setAadharNumber(rs.getLong("aadhar_number"));
                customer.setCreatedAt(rs.getLong("created_at"));
                customer.setModifiedAt(rs.getLong("modified_at"));
                customer.setPerformedBy(rs.getLong("performed_by"));
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
        return customer;
    }
    
    @Override
    public Customer getCustomerDetail(Long userID) throws CustomException {
        Helper.checkNullValues(userID);
        Customer customer = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customerDetail WHERE user_id = ?")) {
            preparedStatement.setLong(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setUserID(rs.getLong("user_id"));
                customer.setDob(rs.getString("dob"));
                customer.setFatherName(rs.getString("father_name"));
                customer.setMotherName(rs.getString("mother_name"));
                customer.setAddress(rs.getString("address"));
                customer.setMaritalStatus(rs.getString("marital_status"));
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
        return customer;
    }
    
    @Override
    public List<Nominee> getNominees(Long userID) throws CustomException {
        Helper.checkNullValues(userID);
        List<Nominee> nominees = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM nominee WHERE user_id = ?")) {
            preparedStatement.setLong(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Nominee nominee = new Nominee();
                nominee.setUserID(rs.getLong("user_id"));
                nominee.setName(rs.getString("name"));
                nominee.setRelationship(rs.getString("relationship"));
                nominees.add(nominee);
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
        return nominees;
    }
    
    @Override
    public Customer getAllCustomerDetails(Long userID) throws CustomException {
        Helper.checkNullValues(userID);
        Customer customer = getCustomer(userID); 
        if (customer == null) {
            throw new CustomException("Customer not found with userID: " + userID);
        }
        Customer customerDetails = getCustomerDetail(userID);
        if (customerDetails != null) {
            customer.setDob(customerDetails.getDob());
            customer.setFatherName(customerDetails.getFatherName());
            customer.setMotherName(customerDetails.getMotherName());
            customer.setAddress(customerDetails.getAddress());
            customer.setMaritalStatus(customerDetails.getMaritalStatus());
        }
        List<Nominee> nominees = getNominees(userID);
        customer.setNominees(nominees);

        return customer;
    }

    @Override
    public <T> void update(Long userID, Long performerID, String table, String column, T value) throws CustomException {
        Helper.checkNullValues(table);
        Helper.checkNullValues(userID);
        Helper.checkNullValues(column);
        Helper.checkNullValues(value);
        
        String updateSql = "UPDATE " + table + " SET " + column + " = ? WHERE user_id = ?";
        String updateSql2 = "UPDATE customer SET modified_at = ?, performed_by = ? WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
             PreparedStatement preparedStatement2 = connection.prepareStatement(updateSql2)) {
            
            if (value instanceof String) {
                preparedStatement.setString(1, (String) value);
            } else if (value instanceof Long) {
                preparedStatement.setLong(1, (Long) value);
            } else if (value instanceof Integer) {
                preparedStatement.setInt(1, (Integer) value);
            } else if (value instanceof Double) {
                preparedStatement.setDouble(1, (Double) value);
            } else {
                throw new CustomException("Unsupported value type: " + value.getClass());
            }
            preparedStatement.setLong(2, userID);
            preparedStatement.executeUpdate();
            preparedStatement2.setLong(1, System.currentTimeMillis());
            preparedStatement2.setLong(2, performerID);
            preparedStatement2.setLong(3, userID);
            preparedStatement2.executeUpdate(); 
        } catch (SQLException e) {
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            throw new CustomException(e.getMessage());
        }
    }



    @Override
    public void delete(Long id) throws CustomException {
        Helper.checkNullValues(id);
        String deleteCustomerSQL = "DELETE FROM customer WHERE user_id = ?";
        String deleteCustomerDetailSQL = "DELETE FROM customerDetail WHERE user_id = ?";
        String deleteNomineeSQL = "DELETE FROM nominee WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedNomineeStatement = connection.prepareStatement(deleteNomineeSQL);
             PreparedStatement preparedCustomerDetailStatement = connection.prepareStatement(deleteCustomerDetailSQL);
             PreparedStatement preparedCustomerStatement = connection.prepareStatement(deleteCustomerSQL)) {
            preparedNomineeStatement.setLong(1, id);
            preparedNomineeStatement.executeUpdate();
            preparedCustomerDetailStatement.setLong(1, id);
            preparedCustomerDetailStatement.executeUpdate();
            preparedCustomerStatement.setLong(1, id);
            preparedCustomerStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
    }


    @Override
    public List<Customer> findAll() throws CustomException {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT userID FROM customer")) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Long userID = rs.getLong("userID");
                Customer customer = getAllCustomerDetails(userID); 
                if (customer != null) {
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
        return customers;
    }

}
