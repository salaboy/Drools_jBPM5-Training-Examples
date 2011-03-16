/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.examples.definitions;

import java.util.HashMap;
import java.util.Map;
import org.drools.runtime.process.*;

/**
 *
 * @author esteban
 */
public class StartVehicleTrackingMockSystem implements WorkItemHandler{

    private VehicleTrackingSystem trackingSystem;
    
    public StartVehicleTrackingMockSystem(VehicleTrackingSystem trackingSystem) {
        this.trackingSystem = trackingSystem;
    }

    
    public void executeWorkItem(WorkItem wi, WorkItemManager wim) {
       String vehicleId = (String) wi.getParameter("vehicle.id");
       String vehicleType = (String) wi.getParameter("vehicle.type");
       String trackingId = this.trackingSystem.startTacking(vehicleId, vehicleType);
       Map<String, Object> outputParameters = new HashMap<String, Object>();
       outputParameters.put("trackingId", trackingId);
       wim.completeWorkItem(wi.getId(), outputParameters);
       
    }

    public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
