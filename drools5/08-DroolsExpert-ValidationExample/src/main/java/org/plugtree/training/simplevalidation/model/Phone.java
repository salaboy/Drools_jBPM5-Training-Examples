/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training.simplevalidation.model;

/**
 *
 * @author salaboy
 */
public class Phone {
    private String number;
    private String personId;
    public Phone(String personId, String number) {
        this.personId = personId;
        this.number = number;
    }

    public String getPersonId() {
        return personId;
    }

    
    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Phone{" + "number=" + number + ", personId=" + personId + '}';
    }

    
    
    
}
