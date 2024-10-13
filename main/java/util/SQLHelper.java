package util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dblayer.connect.DBConnection;

public class SQLHelper {
	
	public static <T> T mapResultSetToObject(ResultSet resultSet, Class<T> type, String tableName) throws CustomException {
		try {
        	T instance = type.getDeclaredConstructor().newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, String> map = YamlUtil.getMapping(tableName);
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                try {
                    Field field = type.getDeclaredField(map.get(columnName));
                    field.setAccessible(true);
                    field.set(instance, columnValue);
                } catch (NoSuchFieldException e) {
                	throw new CustomException(e.getMessage());
                }
            }
            return instance;
        } catch (Exception e) {
            throw new CustomException("Error mapping result set to object: " + e.getMessage());
        }
    }
	
	private static ResultSet setPreparedStatementValue(String query, Object[] values) throws CustomException {
		Helper.checkNullValues(query);
		try{
			Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			if(values != null) { 
	        	for(int i=1;i<=values.length;i++) {
	        		Object value = values[i-1];
	        		if(value == null) {
	        			preparedStatement.setNull(i, Types.NULL);
	    		    } 
	        		preparedStatement.setObject(i, value);
	        	}
			}
        	if(query.contains("SELECT")) {
        		return preparedStatement.executeQuery();
        	} else {
        		preparedStatement.execute();
        		return null;
        	}
        } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
        }
	}
	 
	public static void update(String table, String[] columns, Object[] values) throws CustomException {
		Helper.checkNullValues(table);
    	Helper.checkNullValues(columns);
    	Helper.checkNullValues(values);
	    Helper.checkLength(columns, values);
	    
    	int len = columns.length;
    	StringBuilder updateSql = new StringBuilder("UPDATE " + table + " SET ");
    	for(int i=0;i<len-1;i++) {
    		updateSql.append(columns[i] + " = ? " );
    		if(i <= len-3) {
    			updateSql.append(", ");
    		}
    	}
    	updateSql.append("WHERE " + columns[len-1] + " = ?;" );
    	setPreparedStatementValue(updateSql.toString(),values);
    }
	
	public static void delete(String table, String[] columns, Object[] values) throws CustomException {
	    Helper.checkNullValues(table);
	    Helper.checkNullValues(columns);
	    Helper.checkNullValues(values);
	    Helper.checkLength(columns, values);
	    StringBuilder deleteSql = new StringBuilder("DELETE FROM ").append(table).append(" WHERE ");
	    for (int i = 0; i < columns.length; i++) {
	        deleteSql.append(columns[i]).append(" = ?");
	        if (i < columns.length - 1) {
	            deleteSql.append(" AND ");
	        }
	    }
	    setPreparedStatementValue(deleteSql.toString(),values);
	}
	
	public static <T> List<T> get(String table, Class<T> clazz, String[] selectColumns, String[] conditionColumns, Object[] conditionValues) throws CustomException {
	    Helper.checkNullValues(table);
	    Helper.checkNullValues(selectColumns);
	    Helper.checkNullValues(clazz);
	    Helper.checkLength(conditionColumns, conditionValues);
	    
	    StringBuilder selectSql = new StringBuilder("SELECT ");
	    for (int i = 0; i < selectColumns.length; i++) {
	        selectSql.append(selectColumns[i]);
	        if (i < selectColumns.length - 1) {
	            selectSql.append(", ");
	        }
	    }
	    selectSql.append(" FROM ").append(table);
	    System.out.println(conditionColumns[0]);
	    if(conditionColumns != null) {
	    	selectSql.append(" WHERE ");
	    	for (int i = 0; i < conditionColumns.length; i++) {
		        selectSql.append(conditionColumns[i]).append(" = ?");
		        if (i < conditionColumns.length - 1) {
		            selectSql.append(" AND ");
		        }
		    }
	    }
	    List<T> list = new ArrayList<T>();
	    System.out.println(selectSql);
	    try(ResultSet resultSet = setPreparedStatementValue(selectSql.toString(), conditionValues);) {
	    	 while (resultSet.next()) {
	             list.add(mapResultSetToObject(resultSet, clazz, table));
	         } 
	    	 return list;
	    } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
		}
	}

	public static void insert(String table, Object pojo) throws CustomException {
	    Helper.checkNullValues(table);
	    Helper.checkNullValues(pojo);
	    
	    Map<String, String> map = YamlUtil.getMapping(table);
	    Map<String, String> fieldToColumnMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	    
	    Class<?> clazz = pojo.getClass();
	    Field[] fields = clazz.getDeclaredFields();
	    int length = fields.length, ctr = 0;
	    StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(table).append(" (");
	    List<Object> values = new ArrayList<>();
	    for(int i=0;i<length;i++) {
	    	Field field = fields[i];
	    	if(field.getName() == "id") {
	    		continue;
	    	}
	        field.setAccessible(true); 
	        try {
	        	insertSql.append(fieldToColumnMap.get(field.getName()));
	        	if (i < length - 1) {
		            insertSql.append(", ");
		        }
	            values.add(field.get(pojo));
	        } catch (IllegalAccessException e) {
	            throw new CustomException("Error accessing field value: " + e.getMessage());
	        }
	        ctr++;
	    }
	    insertSql.append(") VALUES (");
	    for (int i=0;i<ctr;i++) {
	        insertSql.append("?");
	        if (i < ctr - 1) {
	            insertSql.append(", ");
	        }
	    }
	    insertSql.append(");");
	    System.out.println(insertSql.toString());
	    setPreparedStatementValue(insertSql.toString(), values.toArray());
	}

}
