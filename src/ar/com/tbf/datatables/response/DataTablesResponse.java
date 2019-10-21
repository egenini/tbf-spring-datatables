package ar.com.tbf.datatables.response;

import java.util.List;

import org.springframework.data.domain.Page;

public class DataTablesResponse {

	private int draw;
	private int filteredResultsCount;
	private int totalResultsCount;
	private List<?> data;
	
	public void build( Page<?> page ) {
		
		filteredResultsCount = page.getTotalPages() == 0 ? 0 : page.getTotalPages() / page.getSize();
		totalResultsCount    = (int) page.getTotalElements();
		
		setData(page.getContent());
	}
	
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getFilteredResultsCount() {
		return filteredResultsCount;
	}
	public void setFilteredResultsCount(int filteredResultsCount) {
		this.filteredResultsCount = filteredResultsCount;
	}
	public int getTotalResultsCount() {
		return totalResultsCount;
	}
	public void setTotalResultsCount(int totalResultsCount) {
		this.totalResultsCount = totalResultsCount;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	
}
