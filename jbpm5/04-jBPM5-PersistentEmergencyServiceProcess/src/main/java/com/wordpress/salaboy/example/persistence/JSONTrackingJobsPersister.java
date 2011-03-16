/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author esteban
 */
public class JSONTrackingJobsPersister {

    private static JSONTrackingJobsPersister INSTANCE;
    private File store;

    private JSONTrackingJobsPersister() {
        try {
            store = File.createTempFile("workItemStore", "json");
        } catch (IOException ex) {
            Logger.getLogger(JSONTrackingJobsPersister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static JSONTrackingJobsPersister getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JSONTrackingJobsPersister();
        }
        return INSTANCE;
    }

    public synchronized void markTrackingJobAsStarted(TrackingJobInfo trackingJobInfo) {
        Map<String, TrackingJobInfo> uncompletedTrackingJobs = this.getUncompletedTrackingJobs();
        uncompletedTrackingJobs.put(trackingJobInfo.getTrackingId(), trackingJobInfo);
        this.persistTrackingJobs(uncompletedTrackingJobs);
    }

    public synchronized void markTrackingJobAsCompleted(String trackingJobUID) {
        Map<String, TrackingJobInfo> uncompletedTrackingJobs = this.getUncompletedTrackingJobs();
        uncompletedTrackingJobs.remove(trackingJobUID);
        this.persistTrackingJobs(uncompletedTrackingJobs);
    }

    public synchronized TrackingJobInfo getUncompletedTrackingJob(String trackingId) {
        return this.getUncompletedTrackingJobs().get(trackingId);
    }
        
    public synchronized Map<String, TrackingJobInfo> getUncompletedTrackingJobs() {
        FileReader reader = null;
        try {
            //The uncompleted WorkItems are those contained in the json file
            Gson gson = new Gson();
            reader = new FileReader(store);
            Type type = new TypeToken<Map<String,TrackingJobInfo>>() {
            }.getType();
            Map<String, TrackingJobInfo> result = gson.fromJson(reader, type);
            return (result == null)? new HashMap<String, TrackingJobInfo>():result;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JSONTrackingJobsPersister.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Couldn't find store file!");
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(JSONTrackingJobsPersister.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    private void persistTrackingJobs(Map<String,TrackingJobInfo> workItemsId){
        FileWriter writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(store);
            gson.toJson(workItemsId, writer);
        } catch (Exception ex) {
            Logger.getLogger(JSONTrackingJobsPersister.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Couldn't persist WsorkItems ids!");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(JSONTrackingJobsPersister.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
