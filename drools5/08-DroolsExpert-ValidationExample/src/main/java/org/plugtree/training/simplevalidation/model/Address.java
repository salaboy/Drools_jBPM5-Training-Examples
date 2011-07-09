/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training.simplevalidation.model;

/**
 *
 * @author salaboy
 */
public class Address {
    private String street;
    private String postalCode;
    private String personId;
    private Integer number;
    
    
    public Address(String personId, String street, Integer number, String postalCode) {
        this.personId = personId;
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPersonId() {
        return personId;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Address{" + "street=" + street + ", postalCode=" + postalCode + ", personId=" + personId + ", number=" + number + '}';
    }
    
    

   
    
    

   
    
}
