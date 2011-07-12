/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author esteban
 */
public class Meeting {

    private String subject;
    private List<Employee> participants = new ArrayList<Employee>();
    private long duration;

    public Meeting(String subject, long duration) {
        this.subject = subject;
        this.duration = duration;
    }

    public List<Employee> getParticipants() {
        return participants;
    }

    public void addParticipant(Employee participant) {
        this.participants.add(participant);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getDuration() {
        return duration;
    }

    


}
