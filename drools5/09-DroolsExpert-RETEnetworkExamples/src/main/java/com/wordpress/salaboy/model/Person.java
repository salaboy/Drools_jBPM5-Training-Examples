/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author salaboy
 */
public class Person {
    private String name;
    private int age;
    private List<PhoneNumber> phones;
    private  Address address;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhoneNumber> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneNumber> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Person{" + "name=" + name + ", age=" + age + ", phones=" + phones + ", addresses=" + address + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.age != other.age) {
            return false;
        }
        if (this.phones != other.phones && (this.phones == null || !this.phones.equals(other.phones))) {
            return false;
        }
        if (this.address != other.address && (this.address == null || !this.address.equals(other.address))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + this.age;
        hash = 79 * hash + (this.phones != null ? this.phones.hashCode() : 0);
        hash = 79 * hash + (this.address != null ? this.address.hashCode() : 0);
        return hash;
    }
    
   
           
    
}
