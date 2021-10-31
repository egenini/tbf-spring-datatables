package ar.com.tbf.datatables.request.searchbuilder;

import java.util.ArrayList;

public class Criteria {

	private String condition;
	private String origData;
	private ArrayList<String> value;
	private String type;
	private ArrayList<Criteria> criteria;
	private String logic = null;
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getOrigData() {
		return origData;
	}
	public void setOrigData(String origData) {
		this.origData = origData;
	}
	public ArrayList<String> getValue() {
		return value;
	}
	public void setValue(ArrayList<String> value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
}
