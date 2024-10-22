package logiclayer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import dblayer.dao.CrudDAO;
import dblayer.implementation.CrudDAOImp;
import dblayer.model.Account;
import dblayer.model.Branch;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.CustomerDetail;
import dblayer.model.Staff;
import dblayer.model.Transaction;
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
    
    // Customer handles
    
    public void createCustomer(CustomerDetail customer) throws CustomException {
    	Helper.checkNullValues(customer);
    	customer.setCreatedAt(System.currentTimeMillis());
    	customer.setPerformedBy(id);
    	crudDao.insert(customer);
    }
    
    public void updateCustomer(ColumnCriteria columnCriteria, Criteria criterias) throws CustomException {
    	Helper.checkNullValues(criterias);
    	Helper.checkNullValues(columnCriteria);
    	columnCriteria.getFields().add("modified_at");
    	columnCriteria.getFields().add("performed_by");
    	columnCriteria.getValues().add(System.currentTimeMillis());
    	columnCriteria.getValues().add(id);
    	crudDao.update(columnCriteria, criterias);
    }
    
    public List<CustomerDetail> getCustomers(long customerId) throws CustomException {
    	Criteria customerJoinCriteria = new Criteria();
        customerJoinCriteria.setJoinTable(Arrays.asList("customer", "user"));
        customerJoinCriteria.setJoinColumn(Arrays.asList("customerDetail.user_id", "customer.user_id"));
        customerJoinCriteria.setJoinOperator(Arrays.asList("=", "="));
        customerJoinCriteria.setJoinValue(Arrays.asList("customer.user_id", "user.id"));
        customerJoinCriteria.setSelectColumn(Arrays.asList("*"));
        customerJoinCriteria.setColumn(Arrays.asList("customerDetail.user_id"));
        customerJoinCriteria.setClazz(CustomerDetail.class);
        customerJoinCriteria.setOperator(Arrays.asList("="));
        customerJoinCriteria.setValue(Arrays.asList(customerId));
        return crudDao.get(customerJoinCriteria);
    }
    
    public void removeCustomer(long customerId) throws CustomException {
    	ColumnCriteria columnCriteria = new ColumnCriteria();
    	columnCriteria.setFields(Arrays.asList("status"));
    	columnCriteria.setValues(Arrays.asList("Inactive"));
    	Criteria criteria = new Criteria();
    	criteria.setClazz(CustomerDetail.class);
    	criteria.initialize();
    	criteria.getColumn().add("id");
		criteria.getOperator().add("=");
		criteria.getValue().add(customerId);
    	crudDao.update(columnCriteria, criteria);
    	criteria.setClazz(Account.class);
    	criteria.getColumn().remove(0);
    	criteria.getColumn().add("customer_id");
    	crudDao.update(columnCriteria, criteria);
    }
    
	// staff handles
    
    public void createStaff(Staff staff) throws CustomException {
    	Helper.checkNullValues(staff);
    	staff.setCreatedAt(System.currentTimeMillis());
    	staff.setPerformedBy(id);
    	crudDao.insert(staff);
    }
    
    public void updateStaff(ColumnCriteria columnCriteria, Criteria criterias) throws CustomException {
    	Helper.checkNullValues(criterias);
    	Helper.checkNullValues(columnCriteria);
    	columnCriteria.getFields().add("modified_at");
    	columnCriteria.getFields().add("performed_by");
    	columnCriteria.getValues().add(System.currentTimeMillis());
    	columnCriteria.getValues().add(id);
    	crudDao.update(columnCriteria, criterias);
    }
    
    public List<Staff> getStaff(long userId) throws CustomException {
    	Criteria customerJoinCriteria = new Criteria();
        customerJoinCriteria.setJoinTable(Arrays.asList("user"));
        customerJoinCriteria.setJoinColumn(Arrays.asList("staff.user_id"));
        customerJoinCriteria.setJoinOperator(Arrays.asList("="));
        customerJoinCriteria.setJoinValue(Arrays.asList("user.id"));
        customerJoinCriteria.setSelectColumn(Arrays.asList("*"));
        customerJoinCriteria.setColumn(Arrays.asList("staff.user_id"));
        customerJoinCriteria.setClazz(Staff.class);
        customerJoinCriteria.setOperator(Arrays.asList("="));
        customerJoinCriteria.setValue(Arrays.asList(userId));
        return crudDao.get(customerJoinCriteria);
    }
    
    public void removeStaff(long userId) throws CustomException {
    	ColumnCriteria columnCriteria = new ColumnCriteria();
    	columnCriteria.setFields(Arrays.asList("status"));
    	columnCriteria.setValues(Arrays.asList("Inactive"));
    	Criteria criteria = new Criteria();
    	criteria.setClazz(Staff.class);
    	criteria.initialize();
    	criteria.getColumn().add("user_id");
		criteria.getOperator().add("=");
		criteria.getValue().add(userId);
    	crudDao.update(columnCriteria, criteria);
    }
    
    // Account handles
    
    public void createAccount(Account account) throws CustomException {
    	crudDao.insert(account);
    }
    
    public List<Account> getAccounts(long userId, long accountNumber, long branchId, int limitValue) throws CustomException {
    	Criteria criteria = new Criteria();
    	criteria.getSimpleCondition(Account.class, Arrays.asList("*"), Arrays.asList("status"), Arrays.asList("="), Arrays.asList("Active"));
    	if(userId > 0) {
    		criteria.getColumn().add("user_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(userId);
    	} if(branchId > 0) {
    		criteria.getColumn().add("branch_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(branchId);
    	} if(accountNumber > 0) {
    		criteria.getColumn().add("account_number");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(accountNumber);
    	} if (limitValue > 0) {
    		criteria.setLimitValue(limitValue);
    	} if(criteria.getColumn().size() > 1) {
    		criteria.setLogicalOperator("AND");
    	}
        return SQLHelper.get(criteria);
    }
    
    public <T> void updateAccount(ColumnCriteria columnCriterias, Criteria criteria) throws CustomException {
    	crudDao.update(columnCriterias, criteria);
    }
    
    public void removeAccount(long userId, long accountNumber) throws CustomException {
    	ColumnCriteria columnCriteria = new ColumnCriteria();
    	columnCriteria.setFields(Arrays.asList("status"));
    	columnCriteria.setValues(Arrays.asList("Inactive"));
    	Criteria criteria = new Criteria();
    	criteria.setClazz(Account.class);
    	criteria.initialize();
    	if(userId > 0) {
    		criteria.getColumn().add("user_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(userId);
    	} if(accountNumber > 0) {
    		criteria.getColumn().add("account_number");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(accountNumber);
    	} if(criteria.getColumn().size() > 1) {
    		criteria.setLogicalOperator("AND");
    	}
    	crudDao.update(columnCriteria, criteria);
    }
    
    // Branch handles
    
    public void createBranch(Branch branch) throws CustomException {
    	Helper.checkNullValues(branch);
    	branch.setCreatedAt(System.currentTimeMillis());
    	branch.setPerformedBy(id);
    	crudDao.insert(branch);
    }
    
    public List<Branch> getBranch(long branchId, long limitValue) throws CustomException {
    	Criteria criteria = new Criteria();
    	criteria.setClazz(Branch.class);
    	criteria.setSelectColumn(Arrays.asList("*"));
    	criteria.initialize();
    	if(branchId > 0) {
    		criteria.getColumn().add("branch_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(branchId);
    	} if (limitValue > 0) {
    		criteria.setLimitValue(limitValue);
    	}
        return SQLHelper.get(criteria);
    }
    
    // Transaction handles
    
    public List<Transaction> getTransaction(long customerId, long accountNumber, long limitValue, long from, long to) throws CustomException {
    	Criteria criteria = new Criteria();
    	criteria.setClazz(Transaction.class);
    	criteria.setSelectColumn(Arrays.asList("*"));
    	criteria.initialize();
    	if(customerId > 0) {
    		criteria.getColumn().add("customer_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(customerId);
    	} if(accountNumber > 0) {
    		criteria.getColumn().add("account_number");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(accountNumber);
    	} if (limitValue > 0) {
    		criteria.setLimitValue(limitValue);
    	} if(from > 0 && to > 0) {
    		criteria.getColumn().add("transaction_time");
    		criteria.getOperator().add(">=");
    		criteria.getValue().add(from);
    		criteria.getColumn().add("transaction_time");
    		criteria.getOperator().add("<=");
    		criteria.getValue().add(to);
    	}
    	if(criteria.getColumn().size() > 1) {
    		criteria.setLogicalOperator("AND");
    	}
        return SQLHelper.get(criteria);
    }
    
    public void makeTransaction(Transaction transaction, boolean thisBank) throws CustomException {
    	createTransaction(transaction);
    	if(!thisBank) return;
    	Long accountNumber = transaction.getAccountNumber();
    	Long transactionAccountNumber = transaction.getTransactionAccountNumber();
    	List<Account> accounts = getAccounts(0, transactionAccountNumber, 0, 0);
    	Helper.checkNullValues(accounts);
    	String transactionType = transaction.getTransactionType();
    	Long transactionUserId = accounts.get(0).getUserId();
    	Long transactionId = getTransaction(0, accountNumber, 0, 0, 0).get(0).getId();
    	transaction.setAccountNumber(transactionAccountNumber);
    	transaction.setTransactionAccountNumber(accountNumber);
    	transaction.setCustomerId(transactionUserId);
    	transaction.setId(transactionId);
    	transaction.setTransactionType(transactionType.equals("Credit") ? "Debit" : "Credit");
    	createTransaction(transaction);
    }
    
    public void createTransaction(Transaction transaction) throws CustomException {
    	transaction.setPerformedBy(id);
    	transaction.setStatus("Completed");
    	transaction.setTransactionTime(System.currentTimeMillis());
    	String transactionType = transaction.getTransactionType();
    	BigDecimal amount = transaction.getAmount();
    	List<Account> accounts = getAccounts(0, transaction.getAccountNumber(), 0, 0);
    	Helper.checkNullValues(accounts);
    	BigDecimal accountBalance = accounts.get(0).getBalance();
    	if(transactionType.equals("Debit")) {
    		transaction.setClosingBalance(accountBalance.add(amount));
    	} else {
    		transaction.setClosingBalance(accountBalance.subtract(amount));
    	}
    	crudDao.insert(transaction);
    	// update account details
    	ColumnCriteria columnCriteria = new ColumnCriteria();
    	columnCriteria.setFields(Arrays.asList("balance"));
    	columnCriteria.setValues(Arrays.asList(transaction.getClosingBalance()));
    	Criteria criteria = new Criteria();
    	criteria.setClazz(Account.class);
    	criteria.initialize();
    	criteria.getColumn().add("account_number");
		criteria.getOperator().add("=");
		criteria.getValue().add(transaction.getAccountNumber());
    	crudDao.update(columnCriteria, criteria);
    }
	
}