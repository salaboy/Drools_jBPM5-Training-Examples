/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

/**
 *
 * @author salaboy
 */
public class Average {
    private Double value;

    public Average(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    
    @Override
    public String toString() {
        return "Average{" + "value=" + value + '}';
    }
    
    
    
    
}
