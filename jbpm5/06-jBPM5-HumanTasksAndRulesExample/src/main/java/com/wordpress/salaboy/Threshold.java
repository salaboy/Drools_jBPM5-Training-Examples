/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

/**
 *
 * @author salaboy
 */
public class Threshold {
    private String name;
    private double valueUpperThreshold;
    private double valueLowerThreshold;

    public Threshold(String name, double valueUpperThreshold, double valueLowerThreshold) {
        this.name = name;
        this.valueUpperThreshold = valueUpperThreshold;
        this.valueLowerThreshold = valueLowerThreshold;
    }
    

    public String getName() {
        return name;
    }

    public double getValueLowerThreshold() {
        return valueLowerThreshold;
    }

    public double getValueUpperThreshold() {
        return valueUpperThreshold;
    }

    @Override
    public String toString() {
        return "Threshold{" + "name=" + name + ", valueUpperThreshold=" + valueUpperThreshold + ", valueLowerThreshold=" + valueLowerThreshold + '}';
    }

    
    
    
}
