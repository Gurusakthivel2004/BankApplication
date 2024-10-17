package dblayer.model;

import java.util.List;

public class Criteria<T> implements Bank {
	
	private Class<T> clazz;
	private String tableName;
	private List<String> selectColumn;
    private String column;
    private Object value;
    private String operator;
    private List<Object> values;
    private String joinType; 
    private Criteria<T> joinCriteria;
    private String orderBy; 
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

	public Criteria<T> getJoinCriteria() {
		return joinCriteria;
	}

	public void setJoinCriteria(Criteria<T> joinCriteria) {
		this.joinCriteria = joinCriteria;
	}

	public List<String> getSelectColumn() {
		return selectColumn;
	}

	public void setSelectColumn(List<String> selectColumn) {
		this.selectColumn = selectColumn;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> superclass) {
		this.clazz = (Class<T>) superclass;
	}
}
