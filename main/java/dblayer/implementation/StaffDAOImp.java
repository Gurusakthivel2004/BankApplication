package dblayer.implementation;

import java.util.List;

import dblayer.dao.StaffDAO;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Staff;
import util.CustomException;
import util.SQLHelper;

public class StaffDAOImp implements StaffDAO {

	@Override
	public void insertStaff(Staff obj) throws CustomException {
		SQLHelper.insert("staff", obj);
	}

	@Override
	public List<Staff> getStaff(List<String> columns, List<Criteria> conditions)
			throws CustomException {
		return SQLHelper.get("staff", Staff.class, columns, conditions);
	}

	@Override
	public void updateStaff(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException {
		SQLHelper.update("staff", columnCriteriaList, conditions);
	}

	@Override
	public void deleteStaff(List<Criteria> conditions) throws CustomException {
		SQLHelper.delete("staff", conditions);
	}

}
