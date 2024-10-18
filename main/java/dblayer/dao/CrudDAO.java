package dblayer.dao;

import java.util.List;

import dblayer.model.Branch;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.CustomerDetail;
import util.CustomException;

public interface CrudDAO {
	
	<T> void insert(T obj) throws CustomException;
    
	<T> List<T> get(Criteria conditions) throws CustomException;
    
	<T> void update(ColumnCriteria columnCriteria, Criteria criterias) throws CustomException;
    
	<T> void delete(Criteria conditions) throws CustomException;
    
}
