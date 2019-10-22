package ar.com.tbf.datatables.response;

import java.util.List;

import org.springframework.data.domain.Page;

public class DataTablesResponse {

	private Integer draw = 1;
	private Integer recordsTotal;
	private Integer recordsFiltered = 0;
	private List<?> data;

	public void build(Page<?> page) {

		this.setRecordsTotal( (int) page.getTotalElements() );
		this.setData(         page.getContent()             );
	}

	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Integer getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Integer getRecordsFiltered() {
		return recordsFiltered == 0 ? this.getRecordsTotal() : this.recordsFiltered;
	}
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
}
