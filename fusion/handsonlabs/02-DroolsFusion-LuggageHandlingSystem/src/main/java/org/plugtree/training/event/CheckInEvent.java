/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.event;

/**
 *
 * @author esteban
 */
public class CheckInEvent {
    private final String luggageCode;

    public CheckInEvent(String luggageCode) {
        this.luggageCode = luggageCode;
    }

    public String getLuggageCode() {
        return luggageCode;
    }

}
