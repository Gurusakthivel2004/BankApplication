package util;

import java.util.regex.Pattern;


public class Helper {

    private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "default");
    
    public static String get() {
    	return threadLocal.get();
    }
     
    public static void set(String newValue) {
    	threadLocal.set(newValue);
    }
    
	public static void checkNullValues(Object inputObject) throws CustomException{
	   if(inputObject== null) {  
		   throw new CustomException("Error: Null value provided.");
	   }
	}
	
	public static void checkNumber(String number) throws CustomException {
		checkNullValues(number);
        String patternString = "^\\d{10}$";
		if(!Pattern.matches(patternString, number)) {
			throw new CustomException("Error: Mobile number must have 10 digits!");
		}
	}
	
	public static void checkEmail(String email) throws CustomException {
		checkNullValues(email);
		String patternString = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}$";
		if(!Pattern.matches(patternString, email)) {
			throw new CustomException("Error: Email is not valid");
		}
	}
	
	public static void checkIndexBounds(int index, int limit) throws CustomException{
		if (index >= limit || index < 0) {
			throw new CustomException("Error: The specified index is out of bounds.");
		}
	}
	  
	public static void checkTwoIndexBounds(int startIndex, int endIndex, int limit) throws CustomException {
		checkIndexBounds(startIndex, limit);
	    checkIndexBounds(endIndex, limit);
	    if (startIndex > endIndex) {
	        throw new CustomException("Error: The start index is greater than end index.");
	    }
	}
}