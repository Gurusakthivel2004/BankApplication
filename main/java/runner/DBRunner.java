package runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dblayer.dao.CustomerDAO;
import dblayer.dao.UserDAO;
import dblayer.implementation.CustomerDAOImp;
import dblayer.implementation.UserDAOImp;
import dblayer.model.Branch;
import dblayer.model.Criteria;
import dblayer.model.Customer;
import dblayer.model.CustomerDetail;
import dblayer.model.Nominee;
import dblayer.model.User;
import logiclayer.MiddleLayer;
import dblayer.dao.BranchDAO;
import dblayer.dao.CrudDAO;
import dblayer.implementation.BranchDAOImp;
import dblayer.implementation.CrudDAOImp;
import util.CustomException;
import util.Helper;
import util.SQLHelper;
import util.TableYamlUtil;

public class DBRunner {
	public static void main(String[] args) {
		try {
            List<Criteria> conditions = new ArrayList<>();
            Criteria statusCriteria = new Criteria();
            statusCriteria.setColumn("id");
            statusCriteria.setOperator("=");
            statusCriteria.setValue(3l);
            conditions.add(statusCriteria);
            
            List<User> activeCustomers = SQLHelper.get("user", User.class, new String[]{"*"}, conditions);
            System.out.println("Active Customers: " + activeCustomers);
        } catch (CustomException e) {
            System.out.print("Exception thrown while retrieving active customers: " + e.getMessage());
        }
	}
	public static void branchTest() {
		BranchDAO branchDAO = new BranchDAOImp();
		Branch branch = new Branch();
        branch.setId(null);
        branch.setIfscCode("IFSC12345");
        branch.setContactNumber("9876543210");
        branch.setName("madurai");
        branch.setAddress("123 Main Street, City, State, Zip");
        branch.setCreatedAt(System.currentTimeMillis());
        branch.setModifiedAt(null);
        branch.setPerformedBy(1001L);
	}
	public static void userTest() {
		try {
			User user = new User();
	        user.setId(null);
	        user.setFullname("John Doe");
	        user.setEmail("john.doe@example.com");
	        user.setPhone(9876543210L);
	        user.setRole("Customer");
	        user.setUsername("messi");
	        user.setPassword("leodas");
	        user.setStatus("ACTIVE");
	        user.setCreatedAt(null);
	        user.setModifiedAt(null);
	        
	        MiddleLayer middleLayer = MiddleLayer.getInstance();
//	        middleLayer.storeUser(user);
	        System.out.print(Helper.checkUserPassword("messi", "leodas"));
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}
	public static void customerTest() {
		Customer customer = new Customer();
		customer.setUserID(3l);
		customer.setAadharNumber(905147854318l);
		customer.setPanNumber("EOF12341A");
		customer.setPerformedBy(3l);
		
		CustomerDetail customerDetail = new CustomerDetail();
		customerDetail.setUserID(3l);
		customerDetail.setAddress("Madurai");
		customerDetail.setDob("08-06-2004");
		customerDetail.setFatherName("vijayakumar");
		customerDetail.setMaritalStatus("Single");
		customerDetail.setMotherName("Kavitha");
		
        try {
        	MiddleLayer middleLayer = MiddleLayer.getInstance();
//        	middleLayer.storeCustomer("customer", customer);
        	middleLayer.storeCustomer("customerDetail", customerDetail);
        } catch (CustomException e) {
            e.printStackTrace();
        }
	}
}
