package dblayer.dao;

import util.CustomException;
import java.util.List;
import dblayer.model.Branch;

public interface BranchDAO {
	
    void insert(Branch branch) throws CustomException;
    
    Branch get(Long id) throws CustomException;
    
    <T> void update(Long id, Long performerID, String column, T value) throws CustomException;
    
    void delete(Long id) throws CustomException;
    
    List<Branch> findAll() throws CustomException;
    
}