/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training.simplevalidation.model.control;

import java.util.UUID;

/**
 *
 * @author salaboy
 * This class represent one instance of the validation process
 */
public class Validation {
    private String id;
    private String name;
    public Validation(String name) {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Validation{" + "id=" + id + ", name=" + name + '}';
    }
    
    
    
    
}
