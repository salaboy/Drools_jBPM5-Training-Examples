/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.business;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.plugtree.training.model.Alert;
import org.plugtree.training.model.Alert.AlertType;

/**
 *
 * @author esteban
 */
public class AlertSystem {

    private Map<AlertType, List<Alert>> alerts = new EnumMap<AlertType, List<Alert>>(AlertType.class);

    public AlertSystem() {
        this.initAlerts();
    }

    public void addAlert(AlertType type, String luggageCode, String message){
        Alert alert = new Alert(type, luggageCode, message);
        System.out.println("Alert created: "+ alert);
        this.alerts.get(type).add(alert);
    }

    public void clearAlertsForType(AlertType type){
        alerts.put(type, new ArrayList<Alert>());
    }

    public void clearAllAlerts(){
        this.initAlerts();
    }

    public int getAlertCount(){
        int total = 0;
        for (Map.Entry<AlertType, List<Alert>> entry : this.alerts.entrySet()) {
            total += entry.getValue().size();
        }
        return total;
    }

    public int getAlertCount(AlertType type){
        return this.alerts.get(type).size();
    }

    private void initAlerts(){
        for (AlertType alertType : AlertType.values()) {
            alerts.put(alertType, new ArrayList<Alert>());
        }
    }

}
