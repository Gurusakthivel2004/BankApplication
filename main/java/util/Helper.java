package util;

import java.io.BufferedReader;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import dblayer.dao.CrudDAO;
import dblayer.implementation.CrudDAOImp;
import dblayer.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class Helper {
	
    private static ThreadLocal<Long> threadLocal = ThreadLocal.withInitial(() -> 0l);
    private static CrudDAO crudDAO = new CrudDAOImp();
    
    public static Long get() {
    	return threadLocal.get();
    }
     
    public static void set(Long newValue) {
    	threadLocal.set(newValue);
    }
    
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
    
	public static void checkNullValues(Object inputObject) throws CustomException{
	   if(inputObject== null) {  
		   throw new CustomException("Error: Null value provided.");
	   }
	}
    
    public static <T> void checkRole(String table, Class<T> clazz, long userID) throws CustomException {
    	List<User> rows = crudDAO.get("user", User.class, new String[]{"*"}, new String[] {"id"}, new Object[] {userID});
    	if(rows.size() == 0) {
    		throw new CustomException("User doesn't exists.");
    	} else if(!rows.get(0).getRole().equals(table)) {
    		throw new CustomException("User role mismatch.");
    	}
    }
    
    public static boolean checkUserPassword(String username, String password) throws CustomException {
    	List<User> users = crudDAO.get("user", User.class, new String[]{"*"}, new String[] {"username"}, new Object[] {username});
    	if(users.size() == 0) {
    		throw new CustomException("No user exists with the username");
    	}
    	return Helper.checkPassword(password, users.get(0).getPassword());
    }
	
	public static void checkNumber(String number) throws CustomException {
		checkNullValues(number);
        String patternString = "^\\d{10}$";
		if(!Pattern.matches(patternString, number)) {
			throw new CustomException("Error: Mobile number must have 10 digits!");
		}
	}
	
	public static void checkLength(Object[] arr1, Object[] arr2) throws CustomException {
		checkNullValues(arr1);
		checkNullValues(arr2);
		if (arr1.length != arr2.length) {
	        throw new CustomException("Both arrays must have the same length.");
	    }
	}
	
	public static void checkEmail(String email) throws CustomException {
		checkNullValues(email);
		String patternString = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}$";
		if(!Pattern.matches(patternString, email)) {
			throw new CustomException("Error: Email is not valid");
		}
	} 
	
	public static <T> T convertJson(HttpServletRequest request, Class<T> clazz) throws CustomException {
   	 StringBuilder jsonString = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString.toString(), clazz);
        } catch (Exception e) {
			throw new CustomException(e.getMessage());
		}    
   }
	 
}