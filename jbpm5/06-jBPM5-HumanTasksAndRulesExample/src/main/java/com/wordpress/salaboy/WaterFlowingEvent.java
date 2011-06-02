/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy;

import java.io.Serializable;

/**
 *
 * @author salaboy
 */
public class WaterFlowingEvent implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private boolean processed;
    private int value;
    

    public WaterFlowingEvent(int value) {
        this.value = value;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
    
    public boolean isProcessed() {
        return processed;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "WaterFlowingEvent{" + "processed=" + processed + ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WaterFlowingEvent other = (WaterFlowingEvent) obj;
        if (this.processed != other.processed) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.processed ? 1 : 0);
        hash = 97 * hash + this.value;
        return hash;
    }

    

}
