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
	public <T> List<T> get(Criteria<T> conditions) throws CustomException {
		return SQLHelper.get(conditions);
	}

	@Override
	public <T> void update(List<ColumnCriteria> columnCriteriaList, Criteria<T> criterias) throws CustomException {
		SQLHelper.update(columnCriteriaList, criterias);
	}

	@Override
	public <T> void delete(Criteria<T> conditions) throws CustomException {
		SQLHelper.delete(conditions);
	}

}