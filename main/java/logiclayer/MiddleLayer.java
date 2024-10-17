package logiclayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dblayer.dao.AccountDAO;
import dblayer.dao.CrudDAO;
import dblayer.dao.CustomerDAO;
import dblayer.dao.UserDAO;
import dblayer.implementation.AccountDAOImp;
import dblayer.implementation.CrudDAOImp;
import dblayer.implementation.CustomerDAOImp;
import dblayer.implementation.UserDAOImp;
import dblayer.model.Account;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Customer;
import dblayer.model.CustomerDetail;
import dblayer.model.Nominee;
import dblayer.model.Transaction;
import dblayer.model.Transaction.TransactionType;
import dblayer.model.User;
import util.CustomException;
import util.Helper;
import util.SQLHelper;

public class MiddleLayer {
	
	ThreadLocal<Long> threadLocal;
	private long id = Helper.getThreadLocalValue();
	private static CrudDAO crudDao = new CrudDAOImp();
	
	private MiddleLayer() {}

    private static class SingletonHelper {
        private static final MiddleLayer INSTANCE = new MiddleLayer();
    }

    public static MiddleLayer getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    public void storeCustomer(Customer customer) throws CustomException {
    	String hashedPassword = Helper.hashPassword(customer.getPassword());
    	customer.setPassword(hashedPassword);
    	crudDao.insert(customer);
    }
    
    public void createAccount(Account account) throws CustomException {
    	crudDao.insert(account);
    }
    
    public List<Account> getAccounts(int limitValue) throws CustomException {
    	Criteria<Account> criteria = new Criteria<>();
    	criteria.setSimpleCondition(Account.class, new ArrayList<> (Arrays.asList("*")), "customer_id", "=", id);
    	criteria.setLimitValue(limitValue);
        return SQLHelper.get(criteria);
    }
    
    public void sendAmount(Transaction transaction) throws CustomException {
    	Criteria criteria = new Criteria();
    	criteria.setColumn("account_number");
    	criteria.setOperator("=");
    	criteria.setValue(transaction.getTransactionAccountNumber());
    	Account account = accountDAO.getAccount(new ArrayList<String>(Arrays.asList("customer_id", "balance")), 
    			new ArrayList<Criteria>(Arrays.asList(criteria))).get(0);
    	if(account == null) {
    		throw new CustomException("Transaction account doesnt exists");
    	}
    	BigDecimal transAccountupdatedBalance = account.getBalance().add(transaction.getAmount());
    	crudDAO.update("account", new String[] {"balance, modified_at, customer_id"}, new Object[] {transaction.getClosingBalance(), System.currentTimeMillis(), transaction.getCustomerId()});
    	crudDAO.update("account", new String[] {"balance, modified_at, customer_id"}, new Object[] {transAccountupdatedBalance, System.currentTimeMillis(), account.getCustomerId()});
    	crudDAO.insert("transaction", transaction);
    	transaction.setCustomerId(account.getCustomerId());
    	transaction.setTransactionAccountNumber(transaction.getAccountNumber());
    	transaction.setAccountNumber(account.getAccountNumber());
    	transaction.setTransactionType(TransactionType.CREDIT);	
    	transaction.setClosingBalance(transAccountupdatedBalance);
    	crudDAO.insert("transaction", transaction);
    }
	
}