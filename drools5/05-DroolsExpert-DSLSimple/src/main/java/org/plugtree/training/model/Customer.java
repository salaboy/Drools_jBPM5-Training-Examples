package org.plugtree.training.model;

import org.plugtree.training.enums.CustomerType;

public class Customer {

	private String name;
	private String type;
	
	public Customer(String name, CustomerType type) {
		this.name = name;
		this.type = type.name();
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setType(CustomerType type) {
		this.type = type.name();
	}
	public String getType() {
		return type;
	}

}
