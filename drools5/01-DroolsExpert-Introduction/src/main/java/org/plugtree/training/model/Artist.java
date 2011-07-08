/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

/**
 *
 * @author salaboy
 */
public class Artist {
    private String name;
    private String lastname;

    public Artist() {
    }

    public Artist(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Artist {name= '"+this.getName()+"', lastname= '"+this.getLastname()+"'}";
    }


    

    
}
