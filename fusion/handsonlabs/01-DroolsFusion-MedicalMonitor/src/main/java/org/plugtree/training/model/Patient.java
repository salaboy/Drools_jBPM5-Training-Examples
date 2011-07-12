/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

/**
 *
 * @author esteban
 */
public class Patient extends Person {

    public Patient(String name, String lastName) {
        super(name, lastName);
    }

    @Override
    public String toString() {
        return "{Patient [name= "+this.getName()+", lastName= "+this.getLastName()+"]}";
    }
}
