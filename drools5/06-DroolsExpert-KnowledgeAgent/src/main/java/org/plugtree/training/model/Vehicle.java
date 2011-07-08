package org.plugtree.training.model;

import org.plugtree.training.enums.VehicleType;

public class Vehicle {

	private Person owner;
	private VehicleType type;
	private Insurance insurance;
	private int year;
	private Float value;

	public Vehicle(Person owner, VehicleType type, int year, Float value) {
		this.owner = owner;
		this.type = type;
		this.year = year;
		this.value = value;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public Person getOwner() {
		return owner;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	public Insurance getInsurance() {
		return insurance;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public VehicleType getType() {
		return type;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Float getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "Vehicle type: " + type + " year: " + year + " value: " + value + " " + insurance.toString();
	}

}
