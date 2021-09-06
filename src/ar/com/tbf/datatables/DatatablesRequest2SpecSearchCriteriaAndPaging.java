package ar.com.tbf.datatables;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import ar.com.tbf.common.data.SpecSearchCriteria;
import ar.com.tbf.datatables.request.Column;
import ar.com.tbf.datatables.request.DataTablesRequest;
import ar.com.tbf.datatables.request.Order;

public class DatatablesRequest2SpecSearchCriteriaAndPaging {

	private static final String DESC = "desc";
	private Deque<Object> output = new LinkedList<>();
	Pageable page = null;
	private static final String defaultOperation = ":*";
	private boolean usingSearchBuilder = true;
	
	public DatatablesRequest2SpecSearchCriteriaAndPaging build( DataTablesRequest request, Map<String, String> attributeOperation) {
		return build(request, attributeOperation, null);
	}

		public DatatablesRequest2SpecSearchCriteriaAndPaging build(DataTablesRequest request, Map<String, String> attributeOperation, Map<String, String> attributeDataType) {

		String operation = defaultOperation;
		String prefix    = null;
		String suffix    = null;
		String value;
		
		org.springframework.data.domain.Sort.Order[] orders = new org.springframework.data.domain.Sort.Order[request.getOrder().size()];
		
		int index = 0;
		for( Order order : request.getOrder() ) {
			
			orders[ index ] = new org.springframework.data.domain.Sort.Order( 
					order.getDir().equals( DESC ) ? Direction.DESC : Direction.ASC ,
					request.getColumns().get( order.getColumn() ).getData() 
					);
			
			index++;
		}
		
		if( request.getLength() == -1 ) {
			
			page = PageRequest.of( 0, Integer.MAX_VALUE, Sort.by( orders ) );
			
		}else {
			
			page = PageRequest.of( request.getStart() == 0 ? 0 : request.getStart() / request.getLength(), request.getLength(), Sort.by( orders ) );
		}
		
		if( request.getSearchBuilder() != null && request.getSearchBuilder().getLogic() != null ) {
			
			request.getSearchBuilder().build(output, attributeDataType);
			
			this.setUsingSearchBuilder(true);
		}
		else {
			
			String allSearch = request.getSearch().getValue().trim();
			
			for( Column column : request.getColumns() ) {
				
				if( column.isSearchable() && column.getSearch().getValue() != null ) {
					
					value = column.getSearch().getValue().trim();
					
					value = ! allSearch.isEmpty() ? allSearch : value;
					
					if( ! value.isEmpty() ) {
						
						prefix    = "";
						
						if( attributeOperation.containsKey( column.getData() )) {
							
							operation = attributeOperation.get( column.getData() );
						}
						else {
							operation = defaultOperation;
						}
						
						long count = operation.chars().filter(ch -> ch == '*').count();
						
						if( count != 0 ) {
							
							// puede estar adelante o atrás
							if( operation.substring(1 ).equals("*") ) {
								
								prefix    = "*";
								operation = operation.substring( 0, 1 );
							}
						}
						
						if( output.size() < 2) {
							
							output.push( new SpecSearchCriteria( column.getData(), operation, prefix, value, suffix ) );	
						}
						if( output.size() == 2) {
							
							output.push("AND");
							
						}else if ( output.size() > 2) {
							
							output.push( new SpecSearchCriteria( column.getData(), operation, prefix, value, suffix ) );
							output.push("AND");
						}
					}
				}
			}
		}
		return this;
	}

	public Deque<Object> getOutput() {
		return output;
	}

	public void setOutput(Deque<Object> output) {
		this.output = output;
	}

	public Pageable getPageable() {
		return page;
	}

	public boolean isUsingSearchBuilder() {
		return usingSearchBuilder;
	}

	public void setUsingSearchBuilder(boolean usingSearchBuilder) {
		this.usingSearchBuilder = usingSearchBuilder;
	}

}
