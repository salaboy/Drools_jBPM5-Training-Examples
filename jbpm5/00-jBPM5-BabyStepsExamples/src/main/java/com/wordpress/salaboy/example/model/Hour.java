package com.wordpress.salaboy.example.model;

public class Hour {
	private Integer value;
	public Hour() {}
	public Hour(Integer value) {
		this.value=value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value+"h";
	}
}
