/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.examples.definitions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author esteban
 */
public class MyTrackingSystemMock implements VehicleTrackingSystem {
    private Map<String, String> currentTrackingsById = new HashMap<String, String>();
    private Map<String, String> currentTrackingsByVehicleId = new HashMap<String, String>();
    
    public String startTacking(String vehicleId, String vehicleType){
        String trackingId = UUID.randomUUID().toString();
        this.currentTrackingsById.put(trackingId, vehicleId );
        this.currentTrackingsByVehicleId.put(vehicleId, trackingId );
        return trackingId;
    }
    
    public void stopTacking(String vehicleId){
        this.currentTrackingsById.remove(vehicleId);
    }
    
    public String queryVehicleStatus(String vehicleId){
        String trackingId = this.currentTrackingsByVehicleId.get(vehicleId);
        if(trackingId != null && !trackingId.equals("")){
            return "Vehicle "+vehicleId+" Located at 5th and A Avenue";
        }else{
            return "There is tracking for the vehicle with id = "+vehicleId;
        }
    }

    public String queryVehicleTrackingId(String vehicleId) {
        return this.currentTrackingsByVehicleId.get(vehicleId);
    }
}