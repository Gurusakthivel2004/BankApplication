package dblayer.model;

import java.util.List;

public class Criteria implements Bank {
	
	private String tableName;
    private String column;
    private Object value;
    private String operator;
    private List<Object> values;
    private String joinType; 
    private Criteria joinCriteria;
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

	public Criteria getJoinCriteria() {
		return joinCriteria;
	}

	public void setJoinCriteria(Criteria joinCriteria) {
		this.joinCriteria = joinCriteria;
	}
}
