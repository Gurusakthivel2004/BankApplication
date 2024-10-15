package dblayer.implementation;

import dblayer.dao.UserDAO;
import java.util.List;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.User;
import util.CustomException;
import util.SQLHelper;

public class UserDAOImp implements UserDAO {

	@Override
	public void insertUser(User obj) throws CustomException {
		SQLHelper.insert("user", obj);
	}

	@Override
	public List<User> getUser(List<String> columns, List<Criteria> conditions)
			throws CustomException {
		return SQLHelper.get("user", User.class, columns, conditions);
	}

	@Override
	public void updateUser(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException {
		SQLHelper.update("user", columnCriteriaList, conditions);
	}

	@Override
	public void deleteUser(List<Criteria> conditions) throws CustomException {
		SQLHelper.delete("user", conditions);
	}

}