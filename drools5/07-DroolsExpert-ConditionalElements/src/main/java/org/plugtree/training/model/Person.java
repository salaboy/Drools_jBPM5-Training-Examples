package org.plugtree.training.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author salaboy
 */
public class Person {

	public enum OcupationType { FBI_SPECIAL_AGENT, STUDENT, LAWYER, OFFICER};

	private String name;
	private String lastName;
	private int age;
	private List<Pet> pets;
	private Address address;
	private OcupationType ocupation;

	public Person(String name, String lastName, int age, OcupationType ocupation) {
		this.name = name;
		this.lastName = lastName;
		this.age = age;
		this.ocupation = ocupation;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public void addPet(Pet pet) {
		if(this.pets == null)
			this.pets = new ArrayList<Pet>();
		this.pets.add(pet);
	}

	public void setOcupation(OcupationType ocupation) {
		this.ocupation = ocupation;
	}

	public OcupationType getOcupation() {
		return ocupation;
	}

	public boolean isOld() {
		return (age > 50 || (age > 25 && pets.size() > 5));
	}

}
