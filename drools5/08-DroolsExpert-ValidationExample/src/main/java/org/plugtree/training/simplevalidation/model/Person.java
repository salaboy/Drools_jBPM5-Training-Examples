/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training.simplevalidation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author salaboy
 */
public class Person {
    private String id;
    private String name;
    private List<Phone> phones;
    private List<Address> addresses;
    private List<Pet> pets;

    public Person(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    
    
    public void addAddress(Address address){
        if(addresses == null){
            addresses = new ArrayList<Address>();
        }
        addresses.add(address);
    }
    
    public void addPhone(Phone phone){
        if(phones == null){
            phones = new ArrayList<Phone>();
        }
        phones.add(phone);
    }
    
    public void addPet(Pet pet){
        if(pets == null){
            pets = new ArrayList<Pet>();
        }
        pets.add(pet);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Person{" + "name=" + name + ", phones=" + phones + ", addresses=" + addresses + ", pets=" + pets + '}';
    }
   
    
    
}
