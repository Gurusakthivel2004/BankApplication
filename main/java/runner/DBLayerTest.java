package runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dblayer.model.Branch;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import dblayer.model.Transaction;
import dblayer.model.User;
import util.CustomException;
import util.SQLHelper;

public class DBLayerTest {

	public static void main(String[] args) {
		DBLayerTest dbLayerTest = new DBLayerTest();
		dbLayerTest.testGetActiveCustomers();
	}
	
    void testGetActiveCustomers() {
    	try {
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
            
            ColumnCriteria columnCriteria = new ColumnCriteria();
            columnCriteria.setColumn("*");
            
            List<User> activeCustomers = SQLHelper.get("user", User.class, new ArrayList<ColumnCriteria>(Arrays.asList(columnCriteria)), conditions);
            System.out.println("Active Customers: " + activeCustomers);
        } catch (CustomException e) {
            System.out.print("Exception thrown while retrieving active customers: " + e.getMessage());
        }
    }

}
