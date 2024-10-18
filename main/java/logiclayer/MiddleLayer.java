package logiclayer;

import java.util.Arrays;
import java.util.List;
import dblayer.dao.CrudDAO;
import dblayer.implementation.CrudDAOImp;
import dblayer.model.Account;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.CustomerDetail;
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
    	columnCriteria.getColumn().add("modified_at");
    	columnCriteria.getColumn().add("performed_by");
    	columnCriteria.getValue().add(System.currentTimeMillis());
    	columnCriteria.getValue().add(id);
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
        customerJoinCriteria.setValue(Arrays.asList(2l));
        return crudDao.get(customerJoinCriteria);
    }
    
    public void RemoveCustomer(long customerId) throws CustomException {
    	ColumnCriteria columnCriteria = new ColumnCriteria();
    	columnCriteria.setColumn(Arrays.asList("status"));
    	columnCriteria.setValue(Arrays.asList("Inactive"));
    	Criteria criteria = new Criteria();
    	criteria.setClazz(CustomerDetail.class);
    	if(customerId > 0) {
    		criteria.getColumn().add("id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(customerId);
    	}
    	crudDao.update(columnCriteria, criteria);
    }
    
    // Account handles
    
    public void createAccount(Account account) throws CustomException {
    	crudDao.insert(account);
    }
    
    public List<Account> getAccounts(long branchId, long customerId, int limitValue) throws CustomException {
    	Criteria criteria = new Criteria();
    	criteria.getSimpleCondition(Account.class, Arrays.asList("*"), Arrays.asList("status"), Arrays.asList("="), Arrays.asList("Active"));
    	if(customerId > 0) {
    		criteria.getColumn().add("customer_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(customerId);
    	} if(branchId > 0) {
    		criteria.getColumn().add("branch_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(branchId);
    	} if (limitValue > 0) {
    		criteria.setLimitValue(limitValue);
    	}
        return SQLHelper.get(criteria);
    }
    
    public <T> void updateAccount(ColumnCriteria columnCriterias, Criteria criteria) throws CustomException {
    	crudDao.update(columnCriterias, criteria);
    }
    
    public void RemoveAccount(long customerId, long accountNumber) throws CustomException {
    	ColumnCriteria columnCriteria = new ColumnCriteria();
    	columnCriteria.setColumn(Arrays.asList("status"));
    	columnCriteria.setValue(Arrays.asList("Inactive"));
    	Criteria criteria = new Criteria();
    	criteria.setClazz(Account.class);
    	if(customerId > 0) {
    		criteria.getColumn().add("customer_id");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(customerId);
    	} if(accountNumber > 0) {
    		criteria.getColumn().add("account_number");
    		criteria.getOperator().add("=");
    		criteria.getValue().add(accountNumber);
    	}
    	crudDao.update(columnCriteria, criteria);
    }
	
}