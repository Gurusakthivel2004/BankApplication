package dblayer.model;

import java.util.List;

public class Criteria {
	
	private Class<? extends MarkedClass> clazz;
	private String tableName;
	private List<String> selectColumn;
	private List<String> column;
    private List<Object> value;
    private List<String> operator;
    private String logicalOperator;
    private List<Object> values;
	private List<String> joinColumn;
	private List<String> joinOperator;
	private List<String> joinValue;
    private List<Object> joinTable;
    private String orderBy; 
    private Object limitValue;
    
    public String getTableName() {
        return tableName;
    }
    
    public void getSimpleCondition(Class<? extends MarkedClass> clazz, List<String> selectColumn, List<String> column,List<String> operator, List<Object> value) {
    	this.clazz = clazz;
    	this.selectColumn = selectColumn;
    	this.column = column;
    	this.operator = operator;
    	this.value = value;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public List<String> getColumn() {
        return column;
    }

    public void setColumn(List<String> column) {
        this.column = column;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public List<String> getOperator() {
        return operator;
    }

    public void setOperator(List<String> operator) {
        this.operator = operator;
    }
    
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

	public void setClazz(Class<? extends MarkedClass> clazz) {
		this.clazz = clazz;
	}

	public Object getLimitValue() {
		return limitValue;
	}

	public void setLimitValue(Object limitValue) {
		this.limitValue = limitValue;
	}

	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public List<String> getJoinColumn() {
		return joinColumn;
	}

	public void setJoinColumn(List<String> joinColumn) {
		this.joinColumn = joinColumn;
	}

	public List<String> getJoinValue() {
		return joinValue;
	}

	public void setJoinValue(List<String> joinValue) {
		this.joinValue = joinValue;
	}

	public List<Object> getJoinTable() {
		return joinTable;
	}

	public void setJoinTable(List<Object> joinTable) {
		this.joinTable = joinTable;
	}

	public List<String> getJoinOperator() {
		return joinOperator;
	}

	public void setJoinOperator(List<String> joinOperator) {
		this.joinOperator = joinOperator;
	}
}
