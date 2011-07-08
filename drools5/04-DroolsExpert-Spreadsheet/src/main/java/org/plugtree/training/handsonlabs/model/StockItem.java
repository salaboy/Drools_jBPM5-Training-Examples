package org.plugtree.training.handsonlabs.model;

import org.plugtree.training.handsonlabs.enums.ItemType;

public class StockItem {
	
	private ItemType type;
	private String brand;
	private double price;
	
	public StockItem() {
	}
	
	public StockItem(ItemType type, String brand) {
		super();
		this.type = type;
		this.brand = brand;
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
