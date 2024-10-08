package util;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
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
	
	 public static <T> T mapResultSetToObject(ResultSet resultSet, Class<T> type) throws CustomException {
        try {
            if (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object columnValue = resultSet.getObject(i);
                    try {
                        Field field = type.getDeclaredField(columnName);
                        field.setAccessible(true);
                        field.set(instance, columnValue);
                    } catch (NoSuchFieldException e) {
                    	throw new CustomException(e.getMessage());
                    }
                }
                return instance;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new CustomException("Error mapping result set to object: " + e.getMessage());
        }
    }
}