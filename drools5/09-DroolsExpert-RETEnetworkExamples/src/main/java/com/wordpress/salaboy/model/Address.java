/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.model;

/**
 *
 * @author salaboy
 */
public class Address {
      private String addressLine1;
      private String addressLine2;
      private int postalCode;
      private String city;

    public Address(String addressLine1, String addressLine2, int postalCode, String city) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.city = city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if ((this.addressLine1 == null) ? (other.addressLine1 != null) : !this.addressLine1.equals(other.addressLine1)) {
            return false;
        }
        if ((this.addressLine2 == null) ? (other.addressLine2 != null) : !this.addressLine2.equals(other.addressLine2)) {
            return false;
        }
        if (this.postalCode != other.postalCode) {
            return false;
        }
        if ((this.city == null) ? (other.city != null) : !this.city.equals(other.city)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.addressLine1 != null ? this.addressLine1.hashCode() : 0);
        hash = 59 * hash + (this.addressLine2 != null ? this.addressLine2.hashCode() : 0);
        hash = 59 * hash + this.postalCode;
        hash = 59 * hash + (this.city != null ? this.city.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Address{" + "addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", postalCode=" + postalCode + ", city=" + city + '}';
    }
      
    
      
}
