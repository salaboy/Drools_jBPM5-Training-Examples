package org.plugtree.training.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

/**
 *
 * @author lucaz
 */
@Entity
@NamedQuery(name="getPetsFromPetShopId", query="SELECT p FROM Pet p WHERE p.petShop.id=:petShopId")
public class Pet {

	public enum PetType { DOG, CAT, BIRD, MONKEY, FISH};

	private Integer id;
	private String name;
	private PetType type;
	private PetShop petShop;

	public Pet(String name, PetType type) {
		this.name = name;
		this.type = type;
	}

	public Pet(String name, PetType type, PetShop petShop) {
		this.name = name;
		this.type = type;
		this.petShop = petShop;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PetType getType() {
		return type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	public PetShop getPetShop() {
		return petShop;
	}

	public void setPetShop(PetShop petShop) {
		this.petShop = petShop;
	}

}
