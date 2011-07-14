/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

import java.util.UUID;

/**
 *
 * @author esteban
 */
public class Luggage {

    public static enum Location{
        FRONT_DESK,
        CAROUSEL,
        CART,
        AIRCRAFT;
    }

    private final String code;

    private Location location;

    public Luggage() {
        this.location = Location.FRONT_DESK;
        this.code = UUID.randomUUID().toString();
    }

    public String getCode() {
        return code;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
