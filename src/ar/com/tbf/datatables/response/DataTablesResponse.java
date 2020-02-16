package ar.com.tbf.datatables.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public class DataTablesResponse {

	private Integer draw = 1;
	private Integer recordsTotal;
	private Integer recordsFiltered = 0;
	private List<?> data = null;

	public void build(Page<?> page) {

		if( page != null ) {
			
			this.setRecordsTotal( (int) page.getTotalElements() );
			this.setData(         page.getContent()             );
		}
	}

	public void build(Page<?> page, List<Map<String, Object>> data) {

		this.setRecordsTotal( (int) page.getTotalElements() );
		this.setData(         data                          );
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
		return data == null ? new ArrayList<Object>(1) : data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}

}
