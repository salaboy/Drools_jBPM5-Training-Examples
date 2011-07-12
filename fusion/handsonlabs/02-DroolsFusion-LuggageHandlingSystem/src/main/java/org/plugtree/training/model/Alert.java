/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

/**
 *
 * @author esteban
 */
public class Alert {
    public static enum AlertType{
        LUGGAGE_NOT_CHECKED_IN_ON_TIME,
        NON_EXISTENT_LUGGAGE
    }

    private final AlertType type;
    private final String luggageCode;
    private final String message;

    public Alert(AlertType type, String luggageCode, String message) {
        this.type = type;
        this.message = message;
        this.luggageCode = luggageCode;
    }

    public String getMessage() {
        return message;
    }

    public AlertType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Alert{" + "type=" + type + ", luggageCode='" + luggageCode + "', message='" + message + "'}";
    }

}
