package dblayer.dao;

import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.User;
import util.CustomException;

import java.util.List;

public interface UserDAO {

	void insertUser(User obj) throws CustomException;
    
    List<User> getUser(List<String> columns, List<Criteria> conditions) throws CustomException;
    
    void updateUser(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException;
    
    void deleteUser(List<Criteria> conditions) throws CustomException;	
    
}