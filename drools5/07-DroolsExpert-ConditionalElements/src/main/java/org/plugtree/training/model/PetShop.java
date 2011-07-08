package org.plugtree.training.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * 
 * @author lucaz
 *
 */
@Entity
public class PetShop {

	private Integer id;
	private String name;
	private Address address;
	private boolean habilited = true;

	public PetShop(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	@OneToOne(cascade=CascadeType.ALL)
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	public void setHabilited(boolean habilited) {
		this.habilited = habilited;
	}

	public boolean isHabilited() {
		return habilited;
	}

}
