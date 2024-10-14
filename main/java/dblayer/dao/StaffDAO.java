package dblayer.dao;

import java.util.List;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Staff;
import util.CustomException;

public interface StaffDAO {
	
	void insertStaff(Staff obj) throws CustomException;
    
    List<Staff> getStaff(List<String> columns, List<Criteria> conditions) throws CustomException;
    
    void updateStaff(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException;
    
    void deleteStaff(List<Criteria> conditions) throws CustomException;	

}
