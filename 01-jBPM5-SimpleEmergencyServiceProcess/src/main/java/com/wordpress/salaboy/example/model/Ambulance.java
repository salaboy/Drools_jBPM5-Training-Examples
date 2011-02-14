/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 2/14/11
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
package com.wordpress.salaboy.example.model;

public class Ambulance implements Vehicle{
    private long id;
    private String name;

    public Ambulance(String name) {
        this.id = 1;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ambulance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
