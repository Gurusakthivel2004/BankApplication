package dblayer.implementation;

import java.util.List;

import dblayer.dao.CrudDAO;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.CustomException;
import util.SQLHelper;

public class CrudDAOImp implements CrudDAO {

	@Override
	public <T> void insert(T obj) throws CustomException {
		SQLHelper.insert(obj);
	}

	@Override
	public <T> List<T> get(Criteria conditions) throws CustomException {
		return SQLHelper.get(conditions);
	}

	@Override
	public void update(ColumnCriteria columnCriteriaList, Criteria criterias) throws CustomException {
		SQLHelper.update(columnCriteriaList, criterias);
	}

	@Override
	public <T> void delete(Criteria conditions) throws CustomException {
		SQLHelper.delete(conditions);
	}

}