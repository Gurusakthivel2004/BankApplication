package dblayer.dao;

import dblayer.model.Customer;
import util.CustomException;

import java.util.List;

public interface CrudDAO {
	
	<T> void insert(String table, T Obj) throws CustomException;
    
    <T> List<T> get(String table, Class<T> clazz, String[] selectColumns, String[] conditionalColumns, Object[] values) throws CustomException;
    
    void update(String table, String[] columns, Object[] values) throws CustomException;
    
    void deleteCustomer(String table, String[] columns, Object[] values) throws CustomException;
    
    <T> List<T> findAll(String table, Class<T> clazz) throws CustomException;
    
}