package runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dblayer.dao.CustomerDAO;
import dblayer.dao.UserDAO;
import dblayer.implementation.CustomerDAOImp;
import dblayer.implementation.UserDAOImp;
import dblayer.model.Branch;
import dblayer.model.Customer;
import dblayer.model.Nominee;
import dblayer.model.User;
import dblayer.dao.BranchDAO;
import dblayer.implementation.BranchDAOImp;
import util.CustomException;
import util.Helper;
import util.JsonUtil;

public class DBRunner {
	public static void main(String[] args) {
		branchTest();
	}
	public static void branchTest() {
		BranchDAO branchDAO = new BranchDAOImp();
        try {
//            branchDAO.insert(branch);
            Branch retrievedBranch = branchDAO.get(new String[] {"*"}, new String[] {"id"}, new Object[] {3L});
            System.out.println("Retrieved Branch: " + retrievedBranch);
            branchDAO.update(new String[] {"name", "contact_number", "id"}, new Object[] {"ICICI", 9361661639L,  3L});
            Branch updatedBranch = branchDAO.get(new String[] {"*"}, new String[] {"id"}, new Object[] {3L});
            System.out.println("Updated Branch: " + updatedBranch);
//            branchDAO.delete(1L);
            List<Branch> allBranches = branchDAO.findAll();
            System.out.println("All Branches: " + allBranches);

        } catch (CustomException e) {
            e.printStackTrace();
        }
	}
	public static void userTest() {
		UserDAO userDAO = new UserDAOImp();
		User userPojo = new User("gurusakthivel", "vijayguru2004@gmail,com", 9361409778l, "Customer", "guru", "leo", "Active", System.currentTimeMillis(), null);
		try {
			userDAO.insert(userPojo);
			userDAO.update(Long.parseLong("2"), "phone", 9597437233l);
			User getUser = userDAO.get(Long.parseLong("2"));
			System.out.print(getUser);
			
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}
	public static void customerTest() {
		CustomerDAO customerDAO = new CustomerDAOImp();
        Customer customer = new Customer(
            2L,
            "1990-01-01",
            "Father Name",
            "Mother Name",
            "123 Main St",
            "Single",
            "ABCDE1234F",
            123456789012L,
            System.currentTimeMillis(),
            null,
            1L,
            new ArrayList<>()
        );

        Nominee nominee = new Nominee(2L, "Nominee Name", "Relationship");
        customer.getNominees().add(nominee);

        try {
            customerDAO.insert(customer);
            System.out.println("Customer inserted successfully!");

            Customer retrievedCustomer = customerDAO.getAllCustomerDetails(2L);
            System.out.println("Retrieved Customer: " + retrievedCustomer);

            customerDAO.update(2l, 1l, "customerDetail", "address", "456 New Address");
            System.out.println("Customer updated successfully!");

            Customer updatedCustomer = customerDAO.getAllCustomerDetails(2L);
            System.out.println("Updated Customer: " + updatedCustomer);

//            customerDAO.delete(2L);
//            System.out.println("Customer deleted successfully!");

        } catch (CustomException e) {
            e.printStackTrace();
        }
	}
}
