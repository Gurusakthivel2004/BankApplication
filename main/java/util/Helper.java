package util;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.catalina.util.CustomObjectInputStream;
import org.eclipse.jdt.internal.compiler.lookup.ImplicitNullAnnotationVerifier;

import dblayer.connect.DBConnection;

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
	
	public static void checkLength(Object[] arr1, Object[] arr2) throws CustomException {
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
	 
}