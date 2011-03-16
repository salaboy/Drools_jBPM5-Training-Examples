/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.example.persistence;

import java.util.HashMap;
import java.util.Map;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * This class is used to store the information of running work items in a session.
 * You can register new Work Items and then complete them.
 * This class doesn't support ksession crashes. The ksession passed to its 
 * constructor must leave through all the process execution.
 * @author esteban
 */
public class ActiveWorkItemService {
    
    private static ActiveWorkItemService INSTANCE;
    
    public static synchronized ActiveWorkItemService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActiveWorkItemService();
        }
        
        return INSTANCE;
    }

    
    private StatefulKnowledgeSession ksession;
    
    /**
     * An in-memory map of WorkItem ids and External System ids.
     * For easy access, the key of the map is the external system (job) id and 
     * the value is the work item id.
     */
    private Map<String,Long> workItemsIdsMap = new HashMap<String, Long>();
    private Map<String,Long> jobTimestampsMap = new HashMap<String, Long>();
    
    private ActiveWorkItemService() {
    }

    /**
     * Method to set the ksession of this object. This method must be called 
     * only once. 
     * I know, I know... I should have created a Factory or something else ;)
     * @param ksession 
     */
    public void setKsession(StatefulKnowledgeSession ksession) {
        if (this.ksession != null){
            throw new IllegalStateException("The ksession is already set!");
        }
        this.ksession = ksession;
    }
    
    /**
     * Registers a new active work item and binds it to an external system job.
     * It also marks the starting timestamp of the external job 
     * @param workItemId
     * @param externalSystemJobId 
     */
    public void registerWorkItem(Long workItemId, String externalSystemJobId){
        this.workItemsIdsMap.put(externalSystemJobId, workItemId);
        this.jobTimestampsMap.put(externalSystemJobId, System.currentTimeMillis());
    }
    
    /**
     * This method should be invoked by an external system when it finished the
     * execution of a job. 
     * This method will complete the work item associated with the passed job id.
     * At this point, the process associated with the work item will continue
     * its execution.
     * This method will also calculate the total execution time of the job and
     * pass it as an output parameter.
     * @param externalSystemJobId
     * @param parameters 
     */
    public void externalSystemJobCompleted(String externalSystemJobId, Map<String,Object> parameters){
        //gets the work item id associated with the job id.
        Long workItemId = this.workItemsIdsMap.remove(externalSystemJobId);
        
        //calculates the execution time of the job
        long currentTimeMillis = System.currentTimeMillis();
        Long startTime = this.jobTimestampsMap.remove(externalSystemJobId);
        double executionTime = (currentTimeMillis - startTime)/1000; 
 
        //set the execution time in the output parameters
        parameters.put("trackingExecutionTime", executionTime);
        
        //Completes the work item
        ksession.getWorkItemManager().completeWorkItem(workItemId, parameters);
    }
    
    
    
}
