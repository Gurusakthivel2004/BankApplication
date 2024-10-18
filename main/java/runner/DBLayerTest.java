package runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Customer;
import dblayer.model.CustomerDetail;
import util.CustomException;
import util.SQLHelper;

public class DBLayerTest {

	public static void main(String[] args) {
		try {
			DBLayerTest.testJoinAndWhereCondition();
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateTest() throws CustomException {
		ColumnCriteria columnCriteria = new ColumnCriteria();
		columnCriteria.setColumn(Arrays.asList("password", "dob"));
		columnCriteria.setValue(Arrays.asList("lapulga", "21-06-1987"));
		
		Criteria customerJoinCriteria = new Criteria();
		customerJoinCriteria.setClazz(CustomerDetail.class);
		customerJoinCriteria.setSelectColumn(new ArrayList<> (Arrays.asList("*")));
        customerJoinCriteria.setColumn(new ArrayList<> (Arrays.asList("user_id")));
        customerJoinCriteria.setOperator(new ArrayList<> (Arrays.asList("=")));
        customerJoinCriteria.setValue(new ArrayList<> (Arrays.asList(2l)));
        
        SQLHelper.update(columnCriteria, customerJoinCriteria);
	}
	
	public static void insertTest() throws CustomException {
		Customer customer = new Customer("Lionel messi", "messi@gmail.com", 1234567890L, "Customer", "leo_messi", "hashed_password", "Active",System.currentTimeMillis(),null, "PAN123456789",987654321012L, 2L );
		SQLHelper.insert(customer);
	}
	
	public static void testGet() throws CustomException {
		Criteria criteria = new Criteria();
		criteria.getSimpleCondition(Customer.class, Arrays.asList("*"), Arrays.asList("user_id", "pan_number"), Arrays.asList("=", "="), Arrays.asList(2l, "ABCDE1234F"));
		criteria.setLogicalOperator("AND");
		List<Customer> customers = SQLHelper.get(criteria);
        System.out.println(customers);
	}
    private static void testJoinAndWhereCondition() throws CustomException {

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
        
        List<CustomerDetail> customers = SQLHelper.get(customerJoinCriteria);
        System.out.println(customers);
    }
}
