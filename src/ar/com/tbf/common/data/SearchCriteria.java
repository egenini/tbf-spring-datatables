package ar.com.tbf.common.data;

public class SearchCriteria {

	/**
	 * Great than
	 */
	public static final String GT = ">";
	/**
	 * Great than or equals
	 */
	public static final String GT_EQ = ">=";
	/**
	 * less than
	 */
	public static final String LT = "<";
	/**
	 * less than or equals
	 */
	public static final String LT_EQ = "<=";
	/**
	 * Like
	 */
	public static final String CONTAINS = ":";
	/**
	 * Equals
	 */
	public static final String EQ = "=";

	private String key;
	private String operation;
	private Object value;
	
	public SearchCriteria( final String key, final String operation, final Object value) {

		this.key       = key;
		this.operation = operation;
		this.value     = value;
		
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

}
