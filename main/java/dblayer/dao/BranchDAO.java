package dblayer.dao;

import util.CustomException;
import java.util.List;
import dblayer.model.Branch;

public interface BranchDAO {
	
    void insert(Branch branch) throws CustomException;
    
    Branch get(String[] selectColumns, String[] conditionalColumns, Object[] values) throws CustomException;
    
    void update(String[] columns, Object[] values) throws CustomException;
    
    void delete(String[] columns, Object[] values) throws CustomException;
    
    List<Branch> findAll() throws CustomException;
    
}