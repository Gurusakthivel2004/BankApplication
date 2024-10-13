package dblayer.dao;

import java.util.List;
import dblayer.model.Transaction;
import util.CustomException;

public interface TransactionDAO {
	
	List<Transaction> getTransactions(long userID) throws CustomException;
    
}