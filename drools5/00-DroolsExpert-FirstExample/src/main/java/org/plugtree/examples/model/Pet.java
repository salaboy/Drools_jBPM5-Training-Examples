/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.examples.model;

/**
 *
 * @author salaboy
 */
public class Pet {
    private String name;
    private String position;
    public enum PetType{CAT, DOG};
    private PetType type;
    public Pet() {
    }

    public Pet(String name, String position, PetType type) {

        this.name = name;
        this.position = position;
        this.type = type;
        System.out.println(name +"("+type.toString()+"): is "+position);
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        System.out.println(name + "("+ type.toString() + "): is "+position);
        this.position = position;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Pet [type= '"+this.type+"', name= '"+this.name+"', position= '"+this.position+"']";
    }

    
}
