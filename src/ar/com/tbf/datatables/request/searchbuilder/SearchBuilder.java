package ar.com.tbf.datatables.request.searchbuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.com.tbf.common.data.SearchOperation;
import ar.com.tbf.common.data.SpecSearchCriteria;
import ar.com.tbf.common.data.exception.PredicateException;

public class SearchBuilder {
	
	private static final DateFormat DATEFORMAT_FULL = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss" );
	private static final DateFormat DATEFORMAT      = new SimpleDateFormat( "dd/MM/yyyy" );
	private static final DateFormat ISO_DATEFORMAT  = new SimpleDateFormat( "yyyy-MM-dd" );
	
	private static final String TYPE_DATE    = "date";
	private static final String TYPE_NUM     = "num";
	private static final String TYPE_STRING  = "string";
	
	private static final String EQ          = "=";
	private static final String NOT_EQ      = "!=";
	private static final String STARTS      = "starts";
	private static final String CONTAINS    = "contains";
	private static final String ENDS        = "ends";
	private static final String BETWEEN     = "between";
	private static final String NOT_BETWEEN = "!between";	
	private static final String NULL          = "null";
	private static final String NOT_NULL      = "!null";
	private static final String GREATER_THAN  = ">";
	private static final String LESS_THAN     = "<";
	private static final String GREATER_THAN_OR_EQUALS  = ">=";
	private static final String LESS_THAN_OR_EQUALS     = "<=";
	
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
		
		SearchOperation condition = null;
		String javaType;
		Deque<Object> specStack = new LinkedList<>();
		
		for( Criteria criteria : this.criteria ) {
			
			if( criteria.getCondition() == null ) {
				
				SearchBuilder sb = new SearchBuilder();
				
				sb.setCriteria( criteria.getCriteria() );
				sb.setLogic(    criteria.getLogic()    );
				
		        Collections.reverse( (List<?>) specStack );

		        while( ! specStack.isEmpty() ) {					

					output.push( specStack.pop() );
				}
				
				sb.build(output, attributeDataType);

				output.push( this.getLogic() );
			}
			
			if( criteria.getCondition() != null && (criteria.getCondition().equals(NULL) || criteria.getCondition().equals(NOT_NULL) ) ) {
				
				criteria.getValue().add("null");
			}

			if( criteria.getValue() != null && ! criteria.getValue().isEmpty() && ! criteria.getValue().get(0).isEmpty() ) {
										
				boolean ignore = false;
				
				switch (criteria.getCondition()) {
				
				case EQ:
					condition = SearchOperation.EQUALITY;
					
					javaType = getJavaType(criteria.getOrigData(), attributeDataType);
					
					// por fecha igual se agrega como rango.
					if( javaType.equals("Timestamp") || javaType.equals("Date") ) {
					
						if( ! this.searchByTimeToo( criteria.getValue().get(0) ) ) {

							Object valueByType  = this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType);
	
							Object valueByType2 = this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType);
							
							addDays( valueByType2, 1 );
	
							specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN_OR_EQUALS, valueByType ) );
	
							specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.LESS_THAN, valueByType2 ) );	
	
							specStack.push(logicAsSearchOperation());
							
							ignore = true;
						}
					}
					
					break;

				case BETWEEN:
					
					ignore = true;
					
					if( criteria.getValue().size() == 2 && ! criteria.getValue().get(1).isEmpty() ) {
						
						specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN_OR_EQUALS, 
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType) ) );	
						
						Object valueByType = this.getValueByType(criteria.getOrigData(), criteria.getValue().get(1), criteria.getType(), attributeDataType);
						// si este es un valor de una fecha sumo 1 d�a
						if( attributeDataType.containsKey( criteria.getOrigData())) {
							
							javaType = attributeDataType.get( criteria.getOrigData() );
							
							if( javaType.equals("Timestamp") || javaType.equals("Date") ) {
								
								if( ! this.searchByTimeToo( criteria.getValue().get(0) ) ) {
									
									addDays( valueByType, 1 );
								}
							}
						}
						specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.LESS_THAN_OR_EQUALS, valueByType ) );	
						
						specStack.push(logicAsSearchOperation());						
					}

					break;
				case STARTS:
					
					condition = SearchOperation.STARTS_WITH;
					break;

				case ENDS:
					
					condition = SearchOperation.ENDS_WITH;
					break;
					
				case CONTAINS:
					
					condition = SearchOperation.CONTAINS;
					break;
					
				case NOT_EQ:
					
					condition = SearchOperation.NEGATION;
					
					javaType = getJavaType(criteria.getOrigData(), attributeDataType);
					
					// por fecha igual se agrega como rango.
					if( javaType.equals("Timestamp") || javaType.equals("Date") ) {
					
						if( ! this.searchByTimeToo( criteria.getValue().get(0) ) ) {

							Object valueByType  = this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType);
	
							Object valueByType2 = this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType);
							
							addDays( valueByType2, 1 );
	
							specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.LESS_THAN, valueByType ) );
	
							specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN_OR_EQUALS, valueByType2 ) );	
	
							specStack.push(SearchOperation.OR_OPERATOR);
							
							ignore = true;
						}
					}
					
					break;
					
				case NOT_BETWEEN:

					ignore = true;

					if( criteria.getValue().size() == 2 && ! criteria.getValue().get(1).isEmpty() ) {
						
						specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.LESS_THAN, 
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType) ) );	
						
						Object valueByType = this.getValueByType(criteria.getOrigData(), criteria.getValue().get(1), criteria.getType(), attributeDataType);
						// si este es un valor de una fecha sumo 1 d�a
						javaType = this.getJavaType( criteria.getOrigData(), attributeDataType );
						
						if( javaType.equals("Timestamp") || javaType.equals("Date") ) {
							
							if( ! this.searchByTimeToo( criteria.getValue().get(0) ) ) {
								
								addDays( valueByType, 1 );
								specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN_OR_EQUALS, valueByType ) );	
							}
							else {
								specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN, valueByType ) );
							}
						}
						else {
							
							specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN, valueByType ) );	
						}
						
						specStack.push(SearchOperation.OR_OPERATOR);						
					}

					break;

				case GREATER_THAN:
					
					condition = SearchOperation.GREATER_THAN;

					ignore = true;

					if( ! criteria.getValue().isEmpty() && ! criteria.getValue().get(0).isEmpty() ) {
											
						Object valueByType = this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType);
						// si este es un valor de una fecha sumo 1 d�a
						javaType = this.getJavaType( criteria.getOrigData(), attributeDataType );
						
						if( javaType.equals("Timestamp") || javaType.equals("Date") ) {
							
							if( ! this.searchByTimeToo( criteria.getValue().get(0) ) ) {
								
								addDays( valueByType, 1 );

								specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN_OR_EQUALS, valueByType ) );	
							}
							else {
								specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN, valueByType ) );	
							}
						}
						else {
							
							specStack.push( new SpecSearchCriteria( criteria.getOrigData(), SearchOperation.GREATER_THAN, valueByType ) );	
						}
					}
										
					break;

				case GREATER_THAN_OR_EQUALS:
					
					condition = SearchOperation.GREATER_THAN_OR_EQUALS;
					
					break;
					
				case LESS_THAN:
					
					condition = SearchOperation.LESS_THAN;

					break;

				case LESS_THAN_OR_EQUALS:
					
					condition = SearchOperation.LESS_THAN_OR_EQUALS;

					break;
					
				case NULL:
					
					condition = SearchOperation.EQUALITY;

					break;
					
				case NOT_NULL:
					
					condition = SearchOperation.NEGATION;

					break;
					
				default:
					break;
				}
									
				if( ! ignore ) {
					
					if( specStack.size() < 2) {
						
						specStack.push( new SpecSearchCriteria( criteria.getOrigData(), condition,
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType) ) );						
					}
					
					if( specStack.size() == 2) {
						
						specStack.push(logicAsSearchOperation());
						
					}
					else if ( specStack.size() > 2) {
						
						specStack.push( new SpecSearchCriteria( criteria.getOrigData(), condition,  
								this.getValueByType(criteria.getOrigData(), criteria.getValue().get(0), criteria.getType(), attributeDataType) ) );	
						specStack.push(logicAsSearchOperation());
					}
				}
			}
		}

		Collections.reverse( (List<?>) specStack );

		while( ! specStack.isEmpty() ) {
				
			output.push( specStack.pop() );
		}
	}
	
	private Object logicAsSearchOperation() {
		
		return logic.equals("OR") ? SearchOperation.OR_OPERATOR : SearchOperation.AND_OPERATOR;
	}
	
	private Object getValueByType(String name, String value, String type, Map<String, String> attributeDataType) {
		
		Object newValue = value;
		String javaType;
		
		if( ! value.equals("null")) {
			
			switch (type) {
			case TYPE_DATE:
				
				if(value.contains("T")) {
					
					try {
						newValue = javax.xml.bind.DatatypeConverter.parseDateTime(value).getTime();
						
					}catch( IllegalArgumentException e) {
						
						throw new PredicateException( "No se pudo interpretar la fecha "+ value +" para el atributo "+ name );
					}				
				}
				else if( value.contains("-") ) {				
					try {
						if( value.contains(":") ) {
							
							newValue = javax.xml.bind.DatatypeConverter.parseDateTime(value).getTime();
						}
						else {
							
							newValue = ISO_DATEFORMAT.parse(value);
						}
					} catch (ParseException e) {
						throw new PredicateException( "No se pudo interpretar la fecha "+ value +" para el atributo "+ name );
					}
				}
				else {
					try {
						if( value.contains(":") ) {
							
							newValue = DATEFORMAT_FULL.parse(value);
						}
						else {
							newValue = DATEFORMAT.parse(value);
						}
						
					} catch (ParseException e) {
						throw new PredicateException( "No se pudo interpretar la fecha "+ value +" para el atributo "+ name );
					}
				}
				
				javaType = getJavaType(name, attributeDataType);
				
				if( javaType == "Timestamp" && newValue instanceof java.util.Date) {
					
					newValue = new Timestamp( ((java.util.Date )newValue).getTime() );
				}
				break;
			case TYPE_NUM:
				
				javaType = getJavaType(name, attributeDataType);
					
				try {
					
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
				}catch(Exception e) {					
					throw new PredicateException( "No se pudo interpretar n�mero "+ value +" para el atributo "+ name );
				}
				break;
			case TYPE_STRING:
				
				javaType = getJavaType(name, attributeDataType);

				if( javaType.equals("Boolean") || javaType.equals("boolean")) {
					
					if( value.equalsIgnoreCase("si") || value.equalsIgnoreCase("verdadero") ) {
						
						newValue = true;
					}
					else if(value.equalsIgnoreCase("no") || value.equalsIgnoreCase("falso") ) {

						newValue = false;
					}
					else {
						try {
							newValue = Boolean.parseBoolean(value);
						}catch(Exception e ) {
							
						}
					}
				}
				
				break;
				
			default:
				break;
			}
		}
		
		return newValue;
	}
	
	public void addDays( Object value, int days ){
		
		Calendar calendar = Calendar.getInstance();
		long     time     = 0;
		
		if( value instanceof Timestamp ) {
			
			time = ((Timestamp )value).getTime();
		}
		else if( value instanceof java.util.Date ) {
			
			time = ((java.util.Date )value).getTime();
		}
		else if( value instanceof java.sql.Date ) {
			
			time = ((java.sql.Date )value).getTime();
		}
		
		calendar.setTimeInMillis( time );
		calendar.add( Calendar.DATE, days );
		
		if( value instanceof Timestamp ) {
			
			((Timestamp) value).setTime(calendar.getTimeInMillis());
		}
		else if( value instanceof java.util.Date ) {
			
			((java.util.Date )value).setTime(calendar.getTimeInMillis());
		}
		else if( value instanceof java.sql.Date ) {
			
			((java.sql.Date )value).setTime(calendar.getTimeInMillis());
		}
	}
	
	private String getJavaType( String name, Map<String, String> attributeDataType ) {
		
		return attributeDataType.containsKey( name ) ? attributeDataType.get( name ) : "String";
	}
	
	/**
	 * El método es tivial pero está para evidenciar su intención.
	 * @param s
	 * @return
	 */
	private boolean searchByTimeToo(String s) {
		
		return s.indexOf(":") != -1;
	}
}
