package logiclayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dblayer.dao.AccountDAO;
import dblayer.dao.CustomerDAO;
import dblayer.dao.UserDAO;
import dblayer.implementation.AccountDAOImp;
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

public class MiddleLayer {
	
	ThreadLocal<Long> threadLocal;
	private static UserDAO userDAO = new UserDAOImp();
	private static CustomerDAO customerDAO = new CustomerDAOImp();
	private static AccountDAO accountDAO = new AccountDAOImp();
	
	private MiddleLayer() {}

    private static class SingletonHelper {
        private static final MiddleLayer INSTANCE = new MiddleLayer();
    }

    public static MiddleLayer getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    public void storeUser(User user) throws CustomException {
    	Helper.checkEmail(user.getEmail());
    	Helper.checkNumber(user.getPhone() + "");
    	String hashedPassword = Helper.hashPassword(user.getPassword());
    	user.setPassword(hashedPassword);
    	userDAO.insertUser(user);
    }
    
    public <T> boolean storeCustomer(String table, T obj) throws CustomException {
    	if(obj instanceof Customer) {
    	    Customer customer = (Customer) obj;
    	    Helper.checkRole(customer.getUserID(), "customer");
    	} else if(obj instanceof CustomerDetail) {
    		CustomerDetail customerDetail = (CustomerDetail) obj;
    	    Helper.checkRole(customerDetail.getUserID(), "customer");
    	} else if(obj instanceof Nominee) {
    		Nominee nominee = (Nominee) obj;
    	    Helper.checkRole(nominee.getUserId(), "customer");
    	}
    	customerDAO.insertCustomer("customer", obj);
    	return true;
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