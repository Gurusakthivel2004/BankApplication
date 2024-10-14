package dblayer.dao;

import java.util.List;

import dblayer.model.Account;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.CustomException;

public interface AccountDAO {

	void insertAccount(Account obj) throws CustomException;
    
    List<Account> getAccount(List<String> columns, List<Criteria> conditions) throws CustomException;
    
    void updateAccount(List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException;
    
    void deleteAccount(List<Criteria> conditions) throws CustomException;	
	
}
