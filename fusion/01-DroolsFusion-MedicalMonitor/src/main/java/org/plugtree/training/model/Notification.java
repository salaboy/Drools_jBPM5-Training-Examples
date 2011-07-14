/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

/**
 *
 * @author esteban
 */
public class Notification {
    private Patient patient;
    private String cause;

    public Notification(Patient patient, String cause) {
        this.patient = patient;
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    public Patient getPatient() {
        return patient;
    }
    
}
