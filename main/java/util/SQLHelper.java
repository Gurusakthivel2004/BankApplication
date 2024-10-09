package util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import dblayer.connect.DBConnection;

public class SQLHelper {
	
	public static <T> T mapResultSetToObject(ResultSet resultSet, Class<T> type, String tableName) throws CustomException {
		try {
        	T instance = type.getDeclaredConstructor().newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, String> map = JsonUtil.getMapping(tableName);
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
		try{
			Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
        	for(int i=1;i<=values.length;i++) {
        		Object value = values[i-1];
        		 if (value instanceof String) {
        		        preparedStatement.setString(i, (String) value);
        		    } else if (value instanceof Long) {
        		        preparedStatement.setLong(i, (Long) value);
        		    } else if (value instanceof Integer) {
        		        preparedStatement.setInt(i, (Integer) value);
        		    } else if (value instanceof Double) {
        		        preparedStatement.setDouble(i, (Double) value);
        		    } else {
        		        throw new CustomException("Unsupported value type: " + value.getClass());
        		    }
        	}
            return preparedStatement.executeQuery();
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
    	System.out.print(updateSql);
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
	
	public static <T> T get(String table, String[] selectColumns, String[] conditionColumns, Object[] conditionValues, Class<T> clazz) throws CustomException {
	    Helper.checkNullValues(table);
	    Helper.checkNullValues(selectColumns);
	    Helper.checkNullValues(conditionColumns);
	    Helper.checkNullValues(conditionValues);
	    Helper.checkNullValues(clazz);
	    Helper.checkLength(conditionColumns, conditionValues);
	    
	    StringBuilder selectSql = new StringBuilder("SELECT ");
	    for (int i = 0; i < selectColumns.length; i++) {
	        selectSql.append(selectColumns[i]);
	        if (i < selectColumns.length - 1) {
	            selectSql.append(", ");
	        }
	    }
	    selectSql.append(" FROM ").append(table).append(" WHERE ");
	    for (int i = 0; i < conditionColumns.length; i++) {
	        selectSql.append(conditionColumns[i]).append(" = ?");
	        if (i < conditionColumns.length - 1) {
	            selectSql.append(" AND ");
	        }
	    }
	    try(ResultSet resultSet = setPreparedStatementValue(selectSql.toString(),conditionValues);) {
	    	 if (resultSet.next()) {
	             return SQLHelper.mapResultSetToObject(resultSet, clazz, table);
	         } else {
	             throw new CustomException(clazz.getSimpleName() + " not found with specified criteria.");
	         }
	    } catch (SQLException e) {
        	throw new CustomException(e.getMessage());
		}
	}

}
