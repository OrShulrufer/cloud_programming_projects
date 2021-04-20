package com.test;

import java.util.Map;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Id;

import com.test.MapToJsonConverter;

@Entity
@Table(name = "compang")
public class CompangEntity {
	
	private Long id;
	private String campagnName;
	private Map<String, String> MandatoryFields;
	
	
	public CompangEntity() {

	}

	public CompangEntity(Long id, String campagnName, Map<String, String> mandatoryFields) {
		super();
		this.id = id;
		this.campagnName = campagnName;
		MandatoryFields = mandatoryFields;
	}


	@Id
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCampagnName() {
		return campagnName;
	}


	public void setCampagnName(String campagnName) {
		this.campagnName = campagnName;
	}


    @Convert(converter = MapToJsonConverter.class)
	@Lob  // Because it's big object
	public Map<String, String> getMandatoryFields() {
		return MandatoryFields;
	}


	public void setMandatoryFields(Map<String, String> mandatoryFields) {
		MandatoryFields = mandatoryFields;
	}

}
