/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.event;

import org.plugtree.training.model.Meeting;

/**
 *
 * @author esteban
 */
public class MeetingStartEvent {
    private long duration;
    private Meeting meeting;

    public MeetingStartEvent(Meeting meeting) {
        this.meeting = meeting;
        this.duration = meeting.getDuration();
    }

    public long getDuration() {
        return duration;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    @Override
    public String toString() {
        return "MeetingStartEvent{" + "duration=" + duration + ", meeting=" + meeting + '}';
    }

    
}
