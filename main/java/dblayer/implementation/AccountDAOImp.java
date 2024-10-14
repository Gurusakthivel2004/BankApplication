package dblayer.implementation;

import java.util.List;

import dblayer.dao.AccountDAO;
import dblayer.model.Account;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.CustomException;
import util.SQLHelper;

public class AccountDAOImp implements AccountDAO {

	@Override
	public void insertAccount(Account obj) throws CustomException {
		SQLHelper.insert("account", obj);
	}

	@Override
	public List<Account> getAccount(List<String> columns, List<Criteria> conditions)
			throws CustomException {
		return SQLHelper.get("account", Account.class, columns, conditions);
	}

	@Override
	public void updateAccount(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions)
			throws CustomException {
		SQLHelper.update("staff", columnCriteriaList, conditions);
	}

	@Override
	public void deleteAccount(List<Criteria> conditions) throws CustomException {
		SQLHelper.delete("staff", conditions);
	}

}
