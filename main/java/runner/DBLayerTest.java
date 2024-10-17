package runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dblayer.model.Account;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Customer;
import dblayer.model.CustomerDetail;
import util.CustomException;
import util.SQLHelper;

public class DBLayerTest {

	public static void main(String[] args) {
		DBLayerTest dbLayerTest = new DBLayerTest();
		try {
			dbLayerTest.testGet();
		} catch (CustomException e) {
			e.printStackTrace();
		}
//		List<Criteria> conditions = new ArrayList<>();
//
//		// Criteria for INNER JOIN
//		Criteria joinCriteria = new Criteria();
//		joinCriteria.setJoinType("INNER");
//		joinCriteria.setJoinTable("Customers");
//		joinCriteria.setJoinCondition(new Criteria() {{
//		    setColumn("Orders.CustomerID");
//		    setOperator("=");
//		    setValue("Customers.CustomerID");
//		}});testJoinAndWhereCondition
//		joinCriteria.setColumn(" Orders.OrderDate");
//		joinCriteria.setOrderBy("ASC");
//		conditions.add(joinCriteria);
//
//		// Criteria for selecting columns
//		// Note: You may need to extend your Criteria class to support column selection directly
//		// Here we assume there's a way to handle selected columns if needed
//
//		StringBuilder sql = new StringBuilder("SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate");
//		sql.append(" FROM Orders");
//
//		// Now we can use the QueryBuilder method to add joins, conditions, etc.
//		try {
//		List<Object> conditionValues = new ArrayList<>();
//		SQLHelper.QueryBuilder(sql, conditions, conditionValues);
//		System.out.println(sql.toString()); // Check the generated query.
//		} catch (CustomException e) {
//          System.out.print("Exception thrown while retrieving active customers: " + e.getMessage());
//      }
	}
	
	public static void updateTest() throws CustomException {
		List<ColumnCriteria> listColumnCriterias = new ArrayList<>();
		ColumnCriteria columnCriteria = new ColumnCriteria();
		columnCriteria.setColumn("password");
		columnCriteria.setValue("leomessi");
		ColumnCriteria columnCriteria2 = new ColumnCriteria();
		columnCriteria2.setColumn("dob");
		columnCriteria2.setValue("June");
		listColumnCriterias.add(columnCriteria);
		listColumnCriterias.add(columnCriteria2);
		
		Criteria<Customer> customerJoinCriteria = new Criteria<>();
		customerJoinCriteria.setClazz(CustomerDetail.class);
		customerJoinCriteria.setSelectColumn(new ArrayList<> (Arrays.asList("*")));
        customerJoinCriteria.setColumn("user_id");
        customerJoinCriteria.setOperator("=");
        customerJoinCriteria.setValue(3l);
        
        SQLHelper.update(listColumnCriterias, customerJoinCriteria);
	}
	
	public static void insertTest() throws CustomException {
		Customer customer = new Customer(
			    "Lionel messi", // fullname
			    "messi@gmail.com", // email
			    1234567890L, // phone
			    "Customer", // role
			    "leo_messi", // username
			    "hashed_password", // password (assumed to be hashed)
			    "Active", // status
			    System.currentTimeMillis(), // createdAt (current timestamp)
			    null, // modifiedAt (assuming it's null for now)
			    "PAN123456789", // panNumber
			    987654321012L, // aadharNumber
			    2L // performedBy (assuming it's the same user who created the record)
		);
		SQLHelper.insert(customer);

	}
	
	public static void testGet() throws CustomException {
		Criteria<Customer> criteria = new Criteria<>();
		criteria.setSimpleCondition(Customer.class, new ArrayList<> (Arrays.asList("*")), null, null, null);
        List<Customer> customers = SQLHelper.get(criteria);
        System.out.println(customers);
	}
	
//	SELECT user.*, customer.*, customerDetail.*
//	FROM user
//	JOIN customer ON user.id = customer.user_id
//	WHERE condition;

//	SELECT user.*, customer.*, customerDetail.*
//	FROM user
//	JOIN customer ON user.id = customer.user_id 
//	WHERE user_id = ?

    private static void testJoinAndWhereCondition() throws CustomException {
        List<Criteria<Customer>> conditions = new ArrayList<>();

        Criteria<Customer> customerJoinCriteria = new Criteria<>();
        Criteria<Customer> joinCriteria = new Criteria<>();
        joinCriteria.setColumn("user.id");
        joinCriteria.setTableName("user");
        joinCriteria.setOperator("=");
        joinCriteria.setValue("customer.user_id");
        customerJoinCriteria.setJoinType(" JOIN ");
        customerJoinCriteria.setSelectColumn(new ArrayList<> (Arrays.asList("*")));
        customerJoinCriteria.setJoinCriteria(joinCriteria);
        customerJoinCriteria.setColumn("user_id");
        customerJoinCriteria.setClazz(Customer.class);
        customerJoinCriteria.setOperator("=");
        customerJoinCriteria.setValue(2l);
        conditions.add(customerJoinCriteria);
        
        List<Customer> customers = SQLHelper.get(customerJoinCriteria);
        System.out.println(customers);
    }


}
