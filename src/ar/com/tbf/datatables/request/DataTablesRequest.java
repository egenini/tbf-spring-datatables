package ar.com.tbf.datatables.request;

import java.util.ArrayList;
import java.util.List;

import ar.com.tbf.datatables.request.searchbuilder.SearchBuilder;

public class DataTablesRequest {

	private int           draw;
	private int           start;
	private int           length;
	private List<Column>  columns = new ArrayList<Column>();
	private Search        search = null;
	private List<Order>   order = new ArrayList<Order>();
	private SearchBuilder searchBuilder = null;

	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	public List<Order> getOrder() {
		return order;
	}
	public void setOrder(List<Order> order) {
		this.order = order;
	}
	public SearchBuilder getSearchBuilder() {
		return searchBuilder;
	}
	public void setSearchBuilder(SearchBuilder searchBuilder) {
		this.searchBuilder = searchBuilder;
	}
}
