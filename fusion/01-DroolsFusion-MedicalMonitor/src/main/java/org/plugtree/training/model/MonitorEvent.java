/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

/**
 *
 * @author esteban
 */
public class MonitorEvent {

    public static enum Symptom{
        HIGH_TEMPERATURE,
        HIGH_BLOOD_PRESSURE;
    }

    private Patient patient;
    private Symptom symptom;

    public MonitorEvent(Patient patient, Symptom symptom) {
        this.patient = patient;
        this.symptom = symptom;
    }

    public Symptom getSymptom() {
        return symptom;
    }

    public Patient getPatient() {
        return patient;
    }
    
}
