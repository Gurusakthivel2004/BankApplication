package dblayer.dao;

import java.util.List;

import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Transaction;
import util.CustomException;

public interface TransactionDAO {
	
	void insertTransaction(Transaction obj) throws CustomException;
    
    List<Transaction> getTransaction(List<String> columns, List<Criteria> conditions) throws CustomException;
    
    void deleteTransaction(List<Criteria> conditions) throws CustomException;
    
}