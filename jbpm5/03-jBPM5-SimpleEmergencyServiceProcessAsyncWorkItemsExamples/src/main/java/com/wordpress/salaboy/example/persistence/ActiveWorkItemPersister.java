/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.example.persistence;

import java.util.HashMap;
import java.util.Map;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * Simple implementation of a Work Item Persister.
 * This class is used to store the information of running work items in a session.
 * You can register new Work Items and then complete them.
 * This class doesn't support ksession crashes. The ksession passed to its 
 * constructor must leave through all the process execution.
 * @author esteban
 */
public class ActiveWorkItemPersister {
    
    private static ActiveWorkItemPersister INSTANCE;
    
    public static synchronized ActiveWorkItemPersister getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActiveWorkItemPersister();
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
    
    private ActiveWorkItemPersister() {
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
     * @param workItemId
     * @param externalSystemJobId 
     */
    public void registerWorkItem(Long workItemId, String externalSystemJobId){
        this.workItemsIdsMap.put(externalSystemJobId, workItemId);
    }
    
    /**
     * This method should be invoked by an external system when it finished the
     * execution of a job. 
     * This method will complete the work item associated with the passed job id.
     * At this point, the process associated with the work item will continue
     * its execution.
     * @param externalSystemJobId
     * @param parameters 
     */
    public void externalSystemJobCompleted(String externalSystemJobId, Map<String,Object> parameters){
        Long workItemId = this.workItemsIdsMap.remove(externalSystemJobId);
        ksession.getWorkItemManager().completeWorkItem(workItemId, parameters);
    }
    
    
    
}
