/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.example.persistence;

/**
 *
 * @author esteban
 */
public class TrackingJobInfo {
    private String trackingId;
    private String vehicleId;
    private String vehicleType;

    public TrackingJobInfo() {
    
    }
    
    public TrackingJobInfo(String trackingId, String vehicleId, String vehicleType) {
        this.trackingId = trackingId;
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

}
