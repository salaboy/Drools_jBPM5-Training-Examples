package org.plugtree.training.handsonlabs.model;

import org.plugtree.training.handsonlabs.enums.ItemType;

public class SpecialOffer {
	
	private ItemType type;
	private String brand;
	private double price;
	
	public SpecialOffer() {
	}
	
	public SpecialOffer(ItemType type, String brand, double price) {
		super();
		this.type = type;
		this.brand = brand;
		this.price = price;
	}
	
	public ItemType getType() {
		return type;
	}
	public void setType(ItemType type) {
		this.type = type;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
