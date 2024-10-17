package util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import dblayer.connect.DBConnection;
import dblayer.model.ColumnCriteria;
import dblayer.model.Criteria;
import util.ColumnYamlUtil.ClassMapping;
import util.ColumnYamlUtil.FieldMapping;

public class SQLHelper {
	
	// map the column name to field name of the pojo class
	@SuppressWarnings("unchecked")
	private static <T> T mapResultSetToObject(ResultSet resultSet, Class<? extends T> clazz, T instance, String tableName) throws CustomException {
		try {
            if(instance == null) {
            	instance = clazz.getDeclaredConstructor().newInstance();
            }
            ClassMapping classMapping = ColumnYamlUtil.getMapping(clazz.getName());
    	    Map<String, FieldMapping> fieldMap = classMapping.getFields();
    	    Field[] fields = clazz.getDeclaredFields();
    	    for(Field field: fields) {
    	    	try {
    	    		FieldMapping fieldMapping = fieldMap.get(field.getName());
        	    	if(fieldMapping == null) {
                		continue;
                	}
        	    	field.setAccessible(true);
        	    	String columnName = fieldMapping.getColumnName();
        	    	Object columnValue = resultSet.getObject(columnName);
                    field.set(instance, columnValue);
    	    	} catch(SQLSyntaxErrorException exception) {
    	    		continue;
    	    	}
    	    }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null && !superclass.getName().equals("dblayer.model.MarkedClass")) {
            	classMapping = ColumnYamlUtil.getMapping(superclass.getName());
                String superClassTableName = classMapping.getTableName();
                mapResultSetToObject(resultSet,(Class <? extends T>) superclass, instance, superClassTableName);
            }
            return instance;
        } catch (Exception e) {
            throw new CustomException("Error mapping result set to object: " + e.getMessage());
        } 
    }
	
	// Returns the preparedStatement after setting the values.
	private static PreparedStatement getPreparedStatement(Connection connection, String query, Object[] values) throws CustomException, SQLException {
		Helper.checkNullValues(query);
		PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
	private static Object executeNonSelect(String query, Object[] values) throws CustomException {
		try(Connection connection = DBConnection.getConnection();
			PreparedStatement preparedStatement = getPreparedStatement(connection, query, values)) {
			preparedStatement.execute();
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getObject(1);
	            }
	        }
			return null;
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		} 
	}
	
	// @AppendQuery method
	// sql : sql string to be appended.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	// conditionValues : It contains the values of the placeholder(?) in the query.
	public static <T> void QueryBuilder(StringBuilder sql, Criteria<T> condition, List<Object> conditionValues) throws CustomException {
		if(condition == null) {
    		throw new CustomException("Condition cannot be null");
    	}
        if (condition.getJoinType() != null && condition.getJoinCriteria() != null) {
            sql.append(condition.getJoinType())
               .append(condition.getJoinCriteria().getTableName())
               .append(" ON ")
               .append(condition.getJoinCriteria().getColumn())
               .append(" "+condition.getJoinCriteria().getOperator() + " ")
               .append(condition.getJoinCriteria().getValue());
        }
        if(condition.getColumn() != null) {
        	sql.append(" WHERE ");
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
        } if (condition.getLimitValue() != null) {
            sql.append("LIMIT").append(condition.getLimitValue());
        }
	}
	
	// @Update method
	// table : name of the table.
	// columnCriteriaList : it contains the column and value to be updated.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	@SuppressWarnings("unchecked")
	public static <T> void update(List<ColumnCriteria> columnCriteriaList, Criteria<? extends T> conditions) throws CustomException {
	    Helper.checkNullValues(conditions);
	    Helper.checkNullValues(columnCriteriaList);
	    Class<? extends T> clazz = (Class<? extends T>) conditions.getClazz();
	    ClassMapping classMapping = ColumnYamlUtil.getMapping(conditions.getClazz().getName());
	    Map<String, FieldMapping> fieldMap = classMapping.getFields();
    	String table = classMapping.getTableName();
	    if (columnCriteriaList.isEmpty()) {
	        throw new IllegalArgumentException("No columns to update.");
	    }
	    StringBuilder updateSql = new StringBuilder("UPDATE " + table + " SET ");
	    List<Object> values = new ArrayList<>(); 
	    for (int i = 0; i < columnCriteriaList.size(); i++) {
	        ColumnCriteria criteria = columnCriteriaList.get(i);
	        FieldMapping fieldMapping = fieldMap.get(criteria.getColumn());
	        if(fieldMapping != null) {
	        	updateSql.append(criteria.getColumn()).append(" = ?");
		        values.add(criteria.getValue()); 
		        if (i < columnCriteriaList.size() - 1) {
		            updateSql.append(", ");
		        }
	        }
	    }
	    if(values.size() > 0) {
		    String query = updateSql.toString();
		    Object[] valuesArray = values.toArray();
		    if(classMapping.getReferedField() != null) {
		    	conditions.setColumn(classMapping.getReferedField());
		    }
	        QueryBuilder(updateSql, conditions, values);
	        query = table;
	        valuesArray = null;
	        System.out.println(updateSql.toString());
//		    executeNonSelect(query, valuesArray);
	    }
	    Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && !superclass.getName().equals("java.lang.Object")) {
        	conditions.setClazz(superclass);
        	update(columnCriteriaList, conditions);
        }
        
	}

	// @Delete method
	// table : name of the table.
	// conditions : It contains the criteria that has to be included in that query (WHERE clause).
	public static <T> void delete(Criteria<T> conditions) throws CustomException {
	    Helper.checkNullValues(conditions);
	    @SuppressWarnings("unchecked")
		Class<? extends T> clazz = (Class<? extends T>) conditions.getClazz();
	    ClassMapping classMapping = ColumnYamlUtil.getMapping(conditions.getClazz().getName());
    	String table = classMapping.getTableName();
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
	@SuppressWarnings("unchecked")
	public static <T> List<T> get(Criteria<T> condition) throws CustomException {
        Helper.checkNullValues(condition);
        ClassMapping classMapping = ColumnYamlUtil.getMapping(condition.getClazz().getName());
    	String table = classMapping.getTableName();
        List<String> selectColumns = condition.getSelectColumn();
        StringBuilder selectSql = new StringBuilder("SELECT ");
        for (int i = 0; i < selectColumns.size(); i++) {
	        selectSql.append(selectColumns.get(i));
	        if (i < selectColumns.size() - 1) {
	        	selectSql.append(", ");
	        }
	    }
        selectSql.append(" FROM ").append(table);
        List<Object> conditionValues = new ArrayList<>();
        if (condition != null) {
            QueryBuilder(selectSql, condition, conditionValues);
        }
        List<T> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
        	PreparedStatement preparedStatement = getPreparedStatement(connection, selectSql.toString(), conditionValues.toArray());
        	ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                list.add((T) mapResultSetToObject(resultSet, condition.getClazz(), null, table));
            }
            return list;
        } catch (SQLException e) {
            throw new CustomException("Error executing SELECT query: " + e.getMessage());
        } 
    }
	
	// @Insert method
	// table : name of the table.
	// pojo : pojo class
	public static void insert(Object pojo) throws CustomException {
	    Helper.checkNullValues(pojo);
	    
        Class<?> clazz = pojo.getClass();
        List<Class<?>> classList = new ArrayList<>();
        while(!clazz.getName().equals("java.lang.MarkedClass")) {
        	classList.add(clazz);
        	clazz = clazz.getSuperclass();
        }
        Object generatedValue = null;
        for(int k=classList.size()-1;k>=0;k--) {
        	clazz = classList.get(k);
        	ClassMapping classMapping = ColumnYamlUtil.getMapping(clazz.getName());
            Map<String, FieldMapping> fieldMap = classMapping.getFields();
        	String tableName = classMapping.getTableName();
            
        	Field[] fields = clazz.getDeclaredFields();
    	    int length = fields.length, ctr = 0;
    	    StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
    	    List<Object> values = new ArrayList<>();
    	    for(int i=0;i<length;i++) {
    	    	Field field = fields[i];
    	    	if(classMapping.getAutoIncrementField() != null && classMapping.getAutoIncrementField().equals(field.getName())) {
    	    		continue;
    	    	}
    	        field.setAccessible(true); 
    	        try {
    	        	FieldMapping fieldMapping = fieldMap.get(field.getName());
    	        	if(fieldMapping == null) {
                		continue;
                	}
    	        	insertSql.append(fieldMapping.getColumnName());
    	        	if (i < length - 1) {
    		            insertSql.append(", ");
    		        } if(generatedValue != null && classMapping.getReferenceField().equals(field.getName())) {
    	        		values.add(generatedValue);
        	    	} else {
        	            values.add(field.get(pojo));
        	    	}
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
    	    generatedValue = executeNonSelect(insertSql.toString(), values.toArray());
        }
	}
}
