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
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;

public class SQLHelper {
	
	// map the column name to field name of the pojo class
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
	
	// execute the query by adding the values
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
	
	// @AppendQuery method
	// sql : sql string to be appended.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	// conditionValues : It contains the values of the placeholder(?) in the query.
	private static void QueryBuilder(StringBuilder sql, List<Criteria> conditions, List<Object> conditionValues) {
	    for (int i = 0; i < conditions.size(); i++) {
	        Criteria condition = conditions.get(i);
	        if(condition.getColumn() != null) {
	        	sql.append(condition.getColumn()).append(" ");
	        }
	        switch (condition.getOperator().toUpperCase()) {
	            case "IN":
	                sql.append("IN (");
	                for (int j = 0; j < condition.getValues().size(); j++) {
	                    sql.append("?");
	                    conditionValues.add(condition.getValues().get(j));
	                    if (j < condition.getValues().size() - 1) {
	                        sql.append(", ");
	                    }
	                }
	                sql.append(")");
	                break;

	            case "BETWEEN":
	                if (condition.getValues().size() == 2) {
	                    sql.append("BETWEEN ? AND ?");
	                    conditionValues.addAll(condition.getValues());
	                }
	                break;

	            case "LIKE":
	                sql.append("LIKE ?");
	                conditionValues.add(condition.getValue());
	                break;

	            case "NOT":
	                sql.append("NOT ");
	                break;

	            case "&": 
	            case "|": 
	            case "^": 
	            case "+":
	            case "-":
	            case "*":
	            case "/":
	            case "=":
	            case ">":
	            case "<":
	            case ">=":
	            case "<=":
	                sql.append(condition.getOperator()).append(" ?");
	                conditionValues.add(condition.getValue());
	                break;

	            case "AND":
	            case "OR":
	                sql.append(" ").append(condition.getOperator()).append(" ");
	                break;

	            default:
	                throw new IllegalArgumentException("Unsupported operator: " + condition.getOperator());
	        }
	    }
	}

	
	// @Update method
	// table : name of the table.
	// columnCriteriaList : it contains the column and value to be updated.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	public static void update(String table, List<ColumnCriteria> columnCriteriaList, List<Criteria> conditions) throws CustomException {
	    Helper.checkNullValues(table);
	    Helper.checkNullValues(columnCriteriaList);
	    if (columnCriteriaList.isEmpty()) {
	        throw new IllegalArgumentException("No columns to update.");
	    }
	    StringBuilder updateSql = new StringBuilder("UPDATE " + table + " SET ");
	    Object[] values = new Object[columnCriteriaList.size()];
	    for (int i = 0; i < columnCriteriaList.size(); i++) {
	        ColumnCriteria criteria = columnCriteriaList.get(i);
	        updateSql.append(criteria.getColumn()).append(" = ?");
	        values[i] = criteria.getValue(); 
	        if (i < columnCriteriaList.size() - 1) {
	            updateSql.append(", ");
	        }
	    }
	    if (conditions != null && !conditions.isEmpty()) {
	        updateSql.append(" WHERE ");
	        List<Object> conditionValues = new ArrayList<>(); 
	        QueryBuilder(updateSql, conditions, conditionValues);
	        Object[] finalValues = new Object[values.length + conditionValues.size()];
	        System.arraycopy(values, 0, finalValues, 0, values.length);
	        System.arraycopy(conditionValues.toArray(), 0, finalValues, values.length, conditionValues.size());
	        setPreparedStatementValue(updateSql.toString(), finalValues);
	    } else {
	        setPreparedStatementValue(updateSql.toString(), values);
	    }
	}

	// @Delete method
	// table : name of the table.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	public static void delete(String table, List<Criteria> conditions) throws CustomException {
	    Helper.checkNullValues(table);
	    Helper.checkNullValues(conditions);
	    if (conditions.isEmpty()) {
	        throw new IllegalArgumentException("No criteria for deletion.");
	    }
	    StringBuilder deleteSql = new StringBuilder("DELETE FROM ").append(table).append(" WHERE ");
	    List<Object> values = new ArrayList<>();

	    QueryBuilder(deleteSql, conditions, values);
	    
	    setPreparedStatementValue(deleteSql.toString(), values.toArray());
	}
	
	// @Get method
	// table : name of the table.
	// clazz : class of the pojo to be returned.
	// columnCriteriaList : it contains the column and value to be updated.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	public static <T> List<T> get(String table,
			Class<T> clazz,
			List<String> selectColumns,
			List<Criteria> conditions) throws CustomException {
		
        Helper.checkNullValues(table);
        Helper.checkNullValues(selectColumns);
        Helper.checkNullValues(clazz);

        StringBuilder selectSql = new StringBuilder("SELECT ");
        for (int i = 0; i < selectColumns.size(); i++) {
	        selectSql.append(selectColumns.get(i));
	        if (i < selectColumns.size() - 1) {
	        	selectSql.append(", ");
	        }
	    }
        selectSql.append(" FROM ").append(table);

        List<Object> conditionValues = new ArrayList<>();
        if (conditions != null && !conditions.isEmpty()) {
            selectSql.append(" WHERE ");
            QueryBuilder(selectSql, conditions, conditionValues);
        }
        System.out.println(selectSql);
        List<T> list = new ArrayList<>();
        try (ResultSet resultSet = setPreparedStatementValue(selectSql.toString(), conditionValues.toArray())) {
            while (resultSet.next()) {
                list.add(mapResultSetToObject(resultSet, clazz, table));
            }
            return list;
        } catch (SQLException e) {
            throw new CustomException("Error executing SELECT query: " + e.getMessage());
        }
    }
	
	// @Insert method
	// table : name of the table.
	// pojo : pojo class
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
