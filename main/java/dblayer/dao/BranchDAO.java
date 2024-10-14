package dblayer.dao;

import util.CustomException;
import java.util.List;
import dblayer.model.Branch;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;

public interface BranchDAO {

	void insertBranch(Branch obj) throws CustomException;
    
    List<Branch> getBranch(List<String> columns, List<Criteria> conditions) throws CustomException;
    
    void updateBranch(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException;
    
    void deleteBranch(List<Criteria> conditions) throws CustomException;
    
}