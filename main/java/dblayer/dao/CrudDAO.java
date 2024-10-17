package dblayer.dao;

import java.util.List;

import dblayer.model.Branch;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.CustomException;

public interface CrudDAO {
	
	<T> void insert(T obj) throws CustomException;
    
	<T> List<T> get(Criteria<T> conditions) throws CustomException;
    
	<T> void update(List<ColumnCriteria> columnCriteriaList, Criteria<T> conditions) throws CustomException;
    
	<T> void delete(Criteria<T> conditions) throws CustomException;
    
}
