/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.example.persistence;

import java.util.Map;
import org.drools.KnowledgeBase;
import org.drools.definition.process.WorkflowProcess;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.Environment;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkflowProcessInstance;

/**
 *
 * @author esteban
 */
public class PersistentProcessManager {

    private KnowledgeBase kbase;
    private Integer ksessionId;
    private Environment env;
    private Map<String,WorkItemHandler> workItemsHandlers;
    private String processId;
    private long processInstanceId;

    private StatefulKnowledgeSession ksession;
    
    public PersistentProcessManager(KnowledgeBase kbase, Environment env, Map<String, WorkItemHandler> workItemsHandlers, String processId) {
        this.kbase = kbase;
        this.env = env;
        this.workItemsHandlers = workItemsHandlers;
        this.processId = processId;
    }
    
    
    public void startProcess(Map<String, Object> parameters){
        ksession = this.getKnowledgeSession();
        ProcessInstance process = ksession.startProcess(processId, parameters);
        this.processInstanceId = process.getId();
    }
    
    public void insertFact(Object fact){
        ksession.insert(fact);
    }
    
    /**
     * Inserts the current process
     */
    public void insertProcess(){
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
        ksession.insert(process);
    }
    
    public int fireAllRules(){
        return ksession.fireAllRules();
    }
    
    public int getProcessState(){
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
        return process.getState();
    }
    
    public boolean isProcessInstanceCompleted(){
       return ksession.getProcessInstance(this.processInstanceId) == null;
    }
    
    public int getNodeInstancesSize(){
        return ((WorkflowProcessInstance)ksession.getProcessInstance(this.processInstanceId)).getNodeInstances().size();
    }
    
    public String getCurrentNodeName(){
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
        long nodeId = process.getNodeInstances().iterator().next().getNodeId();
        return ((WorkflowProcess)this.kbase.getProcess(this.processId)).getNode(nodeId).getName();
    }
    
    public void completeWorkItem(long workItemId, Map<String, Object> outputParameters){
        ksession.getWorkItemManager().completeWorkItem(workItemId, outputParameters);
    }
    
    public Object getProcessVariable(String name){
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
        return process.getVariable(name);
    }
    
    public void setProcessVariable(String name, Object value){
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
        process.setVariable(name,value);
    }
    
    private StatefulKnowledgeSession getKnowledgeSession() {
        StatefulKnowledgeSession ksession = null;
        if (ksessionId == null) {
            
            ksession = JPAKnowledgeService.newStatefulKnowledgeSession(
                kbase,
                null,
                env);
            
            ksessionId = ksession.getId();
        }else{
            ksession = JPAKnowledgeService.loadStatefulKnowledgeSession(
                ksessionId,
                kbase,
                null,
                env);
        }
        
        
        for (Map.Entry<String, WorkItemHandler> entry : this.workItemsHandlers.entrySet()) {
            ksession.getWorkItemManager().registerWorkItemHandler(entry.getKey(), entry.getValue());
        }
        
        //Configures a logger for the session
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        return ksession;
    }
}
