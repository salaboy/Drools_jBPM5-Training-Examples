/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.model;

/**
 *
 * @author salaboy
 */
public class PhoneNumber {
    private String name;
    private String nro;

    public PhoneNumber(String name, String nro) {
        this.name = name;
        this.nro = nro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PhoneNumber other = (PhoneNumber) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.nro == null) ? (other.nro != null) : !this.nro.equals(other.nro)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 47 * hash + (this.nro != null ? this.nro.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" + "name=" + name + ", nro=" + nro + '}';
    }
    
}
