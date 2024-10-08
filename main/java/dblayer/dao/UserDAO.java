package dblayer.dao;

import dblayer.model.User;
import util.CustomException;

import java.util.List;

public interface UserDAO {
	
    void insert(User user) throws CustomException;
    
    User get(Long id) throws CustomException;
    
    <T> void update(Long id, String column, T value) throws CustomException;
    
    void delete(Long id) throws CustomException;
    
    List<User> findAll() throws CustomException;
    
}