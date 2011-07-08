package org.plugtree.training.model;

import org.plugtree.training.enums.ShippingType;

public class Order {

	private Customer customer;
	private String shipping;
	private float amount;
	private boolean insured;
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setShipping(ShippingType shipping) {
		this.shipping = shipping.name();
	}

	public String getShipping() {
		return shipping;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public void setInsured(boolean insured) {
		this.insured = insured;
	}

	public boolean isInsured() {
		return insured;
	}

}
