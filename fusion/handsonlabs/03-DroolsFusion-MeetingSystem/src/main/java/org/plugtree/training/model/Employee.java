/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

/**
 *
 * @author esteban
 */
public class Employee {
    private String name;
    private String lastName;

    public Employee(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
