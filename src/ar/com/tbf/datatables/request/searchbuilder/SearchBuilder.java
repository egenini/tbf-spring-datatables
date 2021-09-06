package ar.com.tbf.datatables.request.searchbuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;

import ar.com.tbf.common.data.SearchOperation;
import ar.com.tbf.common.data.SpecSearchCriteria;

public class SearchBuilder {
	
	private static final DateFormat DATEFORMAT     = new SimpleDateFormat( "dd/MM/yyyy" );
	private static final DateFormat ISO_DATEFORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
	
	private static final String TYPE_DATE = "date";
	private static final String TYPE_NUM = "num";
	private static final String TYPE_String = "string";
	
	private static final String BETWEEN = "between";
	private static final String EQ      = "=";
	private static final String EQ_SP   = ":";
	
	private ArrayList<Criteria> criteria;
	private String logic = null;
	
	public ArrayList<Criteria> getCriteria() {
		return criteria;
	}
	public void setCriteria(ArrayList<Criteria> criteria) {
		this.criteria = criteria;
	}
	public String getLogic() {
		return logic;
	}
	public void setLogic(String logic) {
		this.logic = logic;
	}
	
	public void build(Deque<Object> output, Map<String, String> attributeDataType) {
		
		String condition;
		
		for( Criteria criteria : this.criteria ) {
			
			if( criteria.getValue() != null && ! criteria.getValue().isEmpty() ) {
				
				if( criteria.getCondition().equals(BETWEEN)  ) {
					
					if( criteria.getValue().size() == 2 && ! criteria.getValue().get(1).isEmpty() ) {
						
						
						output.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN_OR_EQUALS, 
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType) ) );	
						
						output.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.LESS_THAN_OR_EQUALS   , 
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(1), criteria.getType(), attributeDataType) ) );	
						
						output.push(logic);
					}
				}
				else {
					
					condition = criteria.getCondition();
					
					if( criteria.getCondition().equals( EQ )) {
						condition = EQ_SP;
					}
					
					if( output.size() < 2) {
						
						output.push( new SpecSearchCriteria( criteria.getOrigData(), condition, null, 
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType), null ) );	
					}
					if( output.size() == 2) {
						
						output.push(logic);
						
					}else if ( output.size() > 2) {
						
						output.push( new SpecSearchCriteria( criteria.getOrigData(), condition, null, 
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType), null ) );	
						output.push(logic);
					}
				}
			}
		}
	}
	
	private Object getValueByType(String name, String value, String type, Map<String, String> attributeDataType) {
		
		Object newValue = value;

		switch (type) {
		case TYPE_DATE:
			
			if( value.contains("-") ) {				
				try {
					newValue = ISO_DATEFORMAT.parse(value);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					newValue = DATEFORMAT.parse(value);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			break;
		case TYPE_NUM:
			
			if( attributeDataType.containsKey(name)) {
				
				String javaType = attributeDataType.get(name);
				
				if( javaType.equals("BigDecimal")) {
					newValue = new BigDecimal(value);
				} else if (javaType.equals( "BigInteger")) {
					newValue =  new BigInteger(value);
				}else if (javaType.equals( "Double")) {
					newValue =  Double.valueOf(value);
				} else if (javaType.equals( "Float")) {
					newValue =  Float.valueOf(value);
				} else if (javaType.equals( "Integer")) {
					newValue =  Integer.valueOf(value);
				} else if (javaType.equals( "Long")) {
					newValue =  Long.valueOf(value);
				} else if (javaType.equals( "Short")) {
					newValue =  Short.valueOf(value);
				}
			}
			break;
		default:
			break;
		}
		
		return newValue;
	}
}
