package dblayer.implementation;

import dblayer.dao.CustomerDAO;
import java.util.List;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Customer;
import util.CustomException;
import util.SQLHelper;

public class CustomerDAOImp implements CustomerDAO {

	@Override
	public <T> void insertCustomer(String table, T obj) throws CustomException {
		SQLHelper.insert(table, obj);
	}

	@Override
	public <T> List<T> getCustomer(String table, Class<T> clazz, List<String> columns, List<Criteria> conditions)
			throws CustomException {
		return SQLHelper.get(table, clazz, columns, conditions);
	}

	@Override
	public void updateCustomer(String table, List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions)
			throws CustomException {
		SQLHelper.update(table, columnCriteriaList, conditions);
	}

	@Override
	public void deleteCustomer(String table, List<Criteria> conditions) throws CustomException {
		SQLHelper.delete(table, conditions);
	}
	
}