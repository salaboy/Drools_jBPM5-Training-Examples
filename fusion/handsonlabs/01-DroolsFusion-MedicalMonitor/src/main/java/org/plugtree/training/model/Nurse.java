/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author esteban
 */
public class Nurse extends Person {

    private List<Notification> notification = new ArrayList<Notification>();

    public Nurse(String name, String lastName) {
        super(name, lastName);
    }

    public void addNotification(Notification notification){
        this.notification.add(notification);
    }

    public List<Notification> getNotifications(){
        return Collections.unmodifiableList(notification);
    }


    @Override
    public String toString() {
        return "{Nurse [name= "+this.getName()+", lastName= "+this.getLastName()+", notifications= "+this.getNotifications().size()+"]}";
    }

    public void clearNotifications() {
        this.notification.clear();
    }
}
