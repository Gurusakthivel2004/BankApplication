package dblayer.dao;

import java.util.List;

import dblayer.model.Branch;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.CustomException;

public interface CrudDAO {
	
	<T> void insert(T obj) throws CustomException;
    
	<T>  List<T> getBranch(List<String> columns, List<Criteria> conditions) throws CustomException;
    
    void updateBranch(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException;
    
    void deleteBranch(List<Criteria> conditions) throws CustomException;
    
}
