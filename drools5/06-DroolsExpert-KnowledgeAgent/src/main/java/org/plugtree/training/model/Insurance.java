package org.plugtree.training.model;

import org.plugtree.training.enums.InsuranceType;

public class Insurance {

	private Integer years;
	private InsuranceType type;
	private Float value;
	
	public Insurance(InsuranceType type) {
		this.type = type;
	}
	
	public Insurance(InsuranceType type, Integer years, Float value) {
		this.type = type;
		this.years = years;
		this.value = value;
	}

	public Integer getYears() {
		return years;
	}
	public void setYears(Integer years) {
		this.years = years;
	}
	public InsuranceType getType() {
		return type;
	}
	public void setType(InsuranceType type) {
		this.type = type;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Insurance type: " +  type.toString() + " years: " + years + " value: " + value;
	}

}
