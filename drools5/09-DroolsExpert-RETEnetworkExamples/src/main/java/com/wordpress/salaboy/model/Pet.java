package com.wordpress.salaboy.model;

public class Pet {
	private String name;
	private String type;
	private Person person;
	
	
	
	public Pet(String name, String type, Person person) {
		super();
		this.name = name;
		this.type = type;
		this.person = person;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	

}
