package dblayer.implementation;

import java.util.List;

import dblayer.dao.TransactionDAO;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Transaction;
import util.CustomException;
import util.SQLHelper;

public class TransactionDAOImp implements TransactionDAO {

	@Override
	public void insertTransaction(Transaction obj) throws CustomException {
		SQLHelper.insert("transaction", obj);
	}

	@Override
	public List<Transaction> getTransaction(List<String> columns, List<Criteria> conditions)
			throws CustomException {
		return SQLHelper.get("transaction", Transaction.class, columns, conditions);
	}

	@Override
	public void deleteTransaction(List<Criteria> conditions) throws CustomException {
		SQLHelper.delete("transaction", conditions);
	}

	
}
	