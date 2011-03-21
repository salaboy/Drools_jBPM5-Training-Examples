/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author salaboy
 */
@Entity
public class Artist {
    
    private Integer id;

    private String name;
    private String lastname;

    public Artist() {
    }

    public Artist(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "Artist {id= "+this.id+", name= '"+this.getName()+"', lastname= '"+this.getLastname()+"'}";
    }


    

    
}
