package runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dblayer.dao.UserDAO;
import dblayer.implementation.UserDAOImp;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.User;
import util.CustomException;
import util.SQLHelper;

public class DBLayerTest {

	public static void main(String[] args) {
		DBLayerTest dbLayerTest = new DBLayerTest();
		dbLayerTest.testGetActiveCustomers();
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
//		}});
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
	
    void testGetActiveCustomers() {
		 List<Criteria> conditions = new ArrayList<>();
	     Criteria statusCriteria = new Criteria();
	     statusCriteria.setColumn("id");
	     statusCriteria.setOperator("=");
	     statusCriteria.setValue(3l);
	     conditions.add(statusCriteria);
	     
	     Criteria statusCriteria3 = new Criteria();
	     statusCriteria3.setOperator("OR");
	     conditions.add(statusCriteria3);
	     
	     Criteria statusCriteria2 = new Criteria();
	     statusCriteria2.setColumn("username");
	     statusCriteria2.setOperator("=");
	     statusCriteria2.setValue("guru");
	     conditions.add(statusCriteria2);
	     
	     UserDAO userDAO = new UserDAOImp();
	     List<User> activeCustomers;
			try {
				activeCustomers = userDAO.getUser(new ArrayList<String>(Arrays.asList("*")), conditions);
	            System.out.println("Active Customers: " + activeCustomers);
			} catch (CustomException e) {
				e.printStackTrace();
			}
    }

}
