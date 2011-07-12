/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.event;

import org.plugtree.training.model.Employee;

/**
 *
 * @author esteban
 */
public class IncomingCallEvent {

    public static enum CallStatus{
        /*
         * Initial state
         */
        PENDING,
        /*
         * The destination is busy, the call is redirected to a
         * telephone ansering machine.
         */
        REDIRECTED,
        /*
         * The destination is available, the call was dipatched to it.
         */
        DISPATCHED;
    }

    private Employee destination;
    private CallStatus status;

    public IncomingCallEvent(Employee destination) {
        this.destination = destination;
        this.status = CallStatus.PENDING;
    }

    public Employee getDestination() {
        return destination;
    }

    public void setDestination(Employee destination) {
        this.destination = destination;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    
    @Override
    public String toString() {
        return "IncomingCallEvent{" + "destination=" + destination + ", status=" + status + '}';
    }


    

}