package logiclayer;

import java.util.List;
import dblayer.dao.CrudDAO;
import dblayer.dao.TransactionDAO;
import dblayer.implementation.CrudDAOImp;
import dblayer.implementation.TransactionDAOImp;
import dblayer.model.Account;
import dblayer.model.Customer;
import dblayer.model.CustomerDetail;
import dblayer.model.Nominee;
import dblayer.model.Transaction;
import dblayer.model.User;
import util.CustomException;
import util.Helper;

public class MiddleLayer {
	
	private CrudDAO crudDAO = new CrudDAOImp();
	private TransactionDAO transactionDAO = new TransactionDAOImp();
	
	ThreadLocal<Long> threadLocal;
	
	private MiddleLayer() {}

    private static class SingletonHelper {
        private static final MiddleLayer INSTANCE = new MiddleLayer();
    }

    public static MiddleLayer getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    public boolean storeUser(User user) throws CustomException {
    	Helper.checkEmail(user.getEmail());
    	Helper.checkNumber(user.getPhone() + "");
    	String hashedPassword = Helper.hashPassword(user.getPassword());
    	user.setPassword(hashedPassword);
    	crudDAO.insert("user", user);
    	return true;
    }
    
    public <T> boolean storeCustomer(String table, T obj) throws CustomException {
    	if(obj instanceof Customer) {
    	    Customer customer = (Customer) obj;
    	    Helper.checkRole("customer", Customer.class, customer.getUserID());
    	} else if(obj instanceof CustomerDetail) {
    		CustomerDetail customerDetail = (CustomerDetail) obj;
    		Helper.checkRole("customer", CustomerDetail.class, customerDetail.getUserID());
    	} else if(obj instanceof Nominee) {
    		Nominee nominee = (Nominee) obj;
    		Helper.checkRole("customer", Nominee.class, nominee.getUserId());
    	}
    	crudDAO.insert(table, obj);
    	return true;
    }
    
    public void storeAccount(Account account) throws CustomException {
    	crudDAO.insert("account", account);
    }
    
    public List<Transaction> getTransaction() throws CustomException {
    	long userID = Helper.get();
    	return transactionDAO.getTransactions(userID);
    }
    
    public <T> List<T> getDetails(String table, Class<T> clazz) throws CustomException {
    	long userID = Helper.get();
    	return crudDAO.get(table, clazz, new String[] {"*"}, new String[] {"user_id"}, new Object[] {userID});
    }
	
}