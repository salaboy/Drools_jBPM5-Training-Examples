/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.examples.definitions;

/**
 *
 * @author esteban
 */
public interface VehicleTrackingSystem{
    public String startTacking(String vehicleId, String vehicleType);
    public void stopTacking(String vehicleId);
    public String queryVehicleStatus(String trackingId);
    public String queryVehicleTrackingId(String vehicleId);
}