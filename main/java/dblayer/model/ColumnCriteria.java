package dblayer.model;

import java.util.List;

public class ColumnCriteria {
	
	// column: field of the pojo class to be updated.
	// value: value of the field.
	
    private List<String> column; 
    private List<Object> value; 
    
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

}
