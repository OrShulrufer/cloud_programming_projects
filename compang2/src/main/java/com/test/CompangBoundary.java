package com.test;

import java.util.Map;

public class CompangBoundary {
	private String campagnName;
	private Map<String, String> mandatoryFields;
	
		
	public CompangBoundary() {

	}


	public CompangBoundary(String campagnName, Map<String, String> mandatoryFields) {
		this.campagnName = campagnName;
		this.mandatoryFields = mandatoryFields;
	}


	public String getCampagnName() {
		return campagnName;
	}


	public void setCampagnName(String campagnName) {
		this.campagnName = campagnName;
	}


	public Map<String, String> getMandatoryFields() {
		return mandatoryFields;
	}


	public void setMandatoryFields(Map<String, String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}
	
	
	
	
	
	

}
