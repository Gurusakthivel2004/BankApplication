package util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import dblayer.connect.DBConnection;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.ColumnYamlUtil.ClassMapping;
import util.TableYamlUtil.ColumnMapping;
import util.ColumnYamlUtil.FieldMapping;
import util.TableYamlUtil.TableMapping;

public class SQLHelper {
	
	// map the column name to field name of the pojo class
	private static <T> T mapResultSetToObject(ResultSet resultSet, Class<T> type, String tableName) throws CustomException {
		try {
        	T instance = type.getDeclaredConstructor().newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            TableMapping tableMapping = TableYamlUtil.getMapping(tableName);
            Map<String, ColumnMapping> columnMap = tableMapping.getFields();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                try {
                	ColumnMapping fieldMapping = columnMap.get(columnName);
                    Field field = type.getDeclaredField(fieldMapping.getColumnName());
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
	
	// Returns the preparedStatement after setting the values.
	private static PreparedStatement getPreparedStatement(Connection connection, String query, Object[] values) throws CustomException, SQLException {
		Helper.checkNullValues(query);
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		for(int i=1;i<=values.length;i++) {
    		Object value = values[i-1];
    		if(value == null) {
    			preparedStatement.setNull(i, Types.NULL);
		    } 
    		preparedStatement.setObject(i, value);
    	}
		return preparedStatement;
	}
	
	// execute the preparedStatement for the nonSelect queries.
	private static void executeNonSelect(String query, Object[] values) throws CustomException {
		try(Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = getPreparedStatement(connection, query, values)) {
			preparedStatement.execute();
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		} 
	}
	
	// @AppendQuery method
	// sql : sql string to be appended.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	// conditionValues : It contains the values of the placeholder(?) in the query.
	public static void QueryBuilder(StringBuilder sql, List<Criteria> conditions, List<Object> conditionValues) throws CustomException {
	    for (Criteria condition : conditions) {
	    	if(condition == null) {
	    		throw new CustomException("Condition cannot be null");
	    	}
	        if (condition.getJoinType() != null && condition.getJoinTable() != null && condition.getJoinCondition() != null) {
	            sql.append(" ")
	               .append(condition.getJoinType())
	               .append(" JOIN ")
	               .append(condition.getJoinTable())
	               .append(" ON ");
	            QueryBuilder(sql, new ArrayList<Criteria>(Arrays.asList(condition.getJoinCondition())), conditionValues);
	        }
	        if(condition.getColumn() != null) {
	        	sql.append(condition.getColumn()).append(" ");
	        } if(condition.getOperator() != null) {
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
	        if (condition.getOrderBy() != null) {
	            sql.append("ORDER BY ").append(condition.getOrderBy());
	            break;
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
	    List<Object> values = new ArrayList<>(); 
	    for (int i = 0; i < columnCriteriaList.size(); i++) {
	        ColumnCriteria criteria = columnCriteriaList.get(i);
	        updateSql.append(criteria.getColumn()).append(" = ?");
	        values.add(criteria.getValue()); 
	        if (i < columnCriteriaList.size() - 1) {
	            updateSql.append(", ");
	        }
	    }
	    String query = updateSql.toString();
	    Object[] valuesArray = values.toArray();
	    if (conditions != null && !conditions.isEmpty()) {
	        updateSql.append(" WHERE ");
	        QueryBuilder(updateSql, conditions, values);
	        query = table;
	        valuesArray = null;
	    }
	    executeNonSelect(query, valuesArray);
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
	    executeNonSelect(deleteSql.toString(), values.toArray());
	}
	
	// @Get method
	// table : name of the table.
	// clazz : class of the pojo to be returned.
	// columnCriteriaList : it contains the column and value to be updated.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	public static <T> List<T> get(String table, Class<T> clazz, List<String> selectColumns, List<Criteria> conditions) throws CustomException {
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
        List<T> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
        	PreparedStatement preparedStatement = getPreparedStatement(connection, selectSql.toString(), conditionValues.toArray());
        	ResultSet resultSet = preparedStatement.executeQuery()) {
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
	    ClassMapping tableMapping = ColumnYamlUtil.getMapping(table);
        Map<String, FieldMapping> columnMap = tableMapping.getFields();
	    
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
	        	FieldMapping fieldMapping = columnMap.get(field.getName());
	        	insertSql.append(fieldMapping.getColumnName());
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
	    executeNonSelect(insertSql.toString(), values.toArray());
	}

}
