package dblayer.dao;

import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Customer;
import util.CustomException;

import java.util.List;

public interface CustomerDAO {
    
	<T> void insertCustomer(String table, T obj) throws CustomException;
    
    <T> List<T> getCustomer(String table, Class<T> clazz, List<String> columns, List<Criteria> conditions) throws CustomException;
    
    void updateCustomer(String table,List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException;
    
    void deleteCustomer(String table, List<Criteria> conditions) throws CustomException;
    
}