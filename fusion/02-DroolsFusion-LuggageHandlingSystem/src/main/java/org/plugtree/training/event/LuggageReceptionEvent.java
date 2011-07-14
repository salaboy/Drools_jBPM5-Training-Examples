/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.event;

import org.plugtree.training.model.Luggage;

/**
 *
 * @author esteban
 */
public class LuggageReceptionEvent {

    private final Luggage luggage;
    private final String flightCode;

    public LuggageReceptionEvent(Luggage luggage, String flightCode) {
        this.flightCode = flightCode;
        this.luggage = luggage;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public Luggage getLuggage() {
        return luggage;
    }

}
