package dblayer.implementation;

import dblayer.dao.CrudDAO;
import java.util.List;
import util.CustomException;
import util.SQLHelper;

public class CrudDAOImp implements CrudDAO {
	
	public <T> void insert(String table, T Obj) throws CustomException {
		SQLHelper.insert(table, Obj);
	}
    
    public <T> List<T> get(String table, Class<T> clazz, String[] selectColumns, String[] conditionalColumns, Object[] values) throws CustomException {
    	return SQLHelper.get(table, clazz, selectColumns, conditionalColumns, values);
    }
    
    public void update(String table, String[] columns, Object[] values) throws CustomException {
    	SQLHelper.update(table, columns, values);
    }
    
    public void deleteCustomer(String table, String[] columns, Object[] values) throws CustomException {
    	SQLHelper.delete(table, columns, values);
    }
    
    public <T> List<T> findAll(String table, Class<T> clazz) throws CustomException {
    	return SQLHelper.get(table, clazz, new String[] {"*"}, null, null);
    }

}