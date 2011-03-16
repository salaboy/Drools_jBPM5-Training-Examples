/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 2/14/11
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
package com.wordpress.salaboy.example.model;

import java.io.Serializable;
import java.util.UUID;

public class Ambulance implements Vehicle, Serializable{
    private String id;
    private String name;

    public Ambulance(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ambulance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String getType() {
        return "Ambulance";
    }
}
