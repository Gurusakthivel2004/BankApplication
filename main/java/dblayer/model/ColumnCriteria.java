package dblayer.model;

public class ColumnCriteria implements Bank {
	
    private String column; 
    private Object value; 
    
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

}
