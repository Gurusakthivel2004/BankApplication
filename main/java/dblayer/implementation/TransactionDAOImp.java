package dblayer.implementation;

import java.util.List;

import dblayer.dao.TransactionDAO;
import dblayer.model.Transaction;
import util.CustomException;
import util.SQLHelper;

public class TransactionDAOImp implements TransactionDAO {

	@Override
	public List<Transaction> getTransactions(long userID) throws CustomException {
		return SQLHelper.get("transaction", Transaction.class, new String[] {"*"}, new String[] {"customer_id"}, new Object[] {userID});
	}
	
}
	