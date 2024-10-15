package dblayer.implementation;

import java.util.List;

import dblayer.dao.CrudDAO;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.CustomException;

public class CrudDAOImp implements CrudDAO {

	@Override
	public <T> void insert(T obj) throws CustomException {
		
	}

	@Override
	public <T> List<T> getBranch(List<String> columns, List<Criteria> conditions) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBranch(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions)
			throws CustomException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBranch(List<Criteria> conditions) throws CustomException {
		// TODO Auto-generated method stub
		
	}

}
