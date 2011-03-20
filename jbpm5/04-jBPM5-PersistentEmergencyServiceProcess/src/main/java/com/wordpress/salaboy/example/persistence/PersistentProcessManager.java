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
 * Implementation of ProcessManager that creates/load a persistent ksession 
 * for each invocation. 
 * Instead of maintain a ksession in memory, this implementation only have the
 * id of the session. This id is used in every method to retrieve the ksession
 * from the database.
 * @author esteban
 */
public class PersistentProcessManager implements ProcessManager {

    private KnowledgeBase kbase;
    private Integer ksessionId;
    private Environment env;
    private Map<String, WorkItemHandler> workItemsHandlers;
    private String processId;
    private long processInstanceId;

    public PersistentProcessManager(KnowledgeBase kbase, Environment env, Map<String, WorkItemHandler> workItemsHandlers, String processId) {
        this.kbase = kbase;
        this.env = env;
        this.workItemsHandlers = workItemsHandlers;
        this.processId = processId;
    }

    public void startProcess(Map<String, Object> parameters) {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        ProcessInstance process = ksession.startProcess(processId, parameters);
        this.processInstanceId = process.getId();
        ksession.dispose();
    }

    public void insertFact(Object fact) {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        ksession.insert(fact);
        ksession.dispose();
    }

    /**
     * Inserts the current process
     */
    public void insertProcess() {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
        ksession.insert(process);
        ksession.dispose();
    }

    public int fireAllRules() {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        try{
            return ksession.fireAllRules();
        } finally{
            ksession.dispose();
        }
    }

    public int getProcessState() {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        try {
            WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
            return process.getState();
        } finally {
            ksession.dispose();
        }
    }

    public boolean isProcessInstanceCompleted() {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        try {
            return ksession.getProcessInstance(this.processInstanceId) == null;
        } finally {
            ksession.dispose();
        }
    }

    public int getNodeInstancesSize() {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        try{
            return ((WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId)).getNodeInstances().size();
        } finally{
            ksession.dispose();
        }
    }

    public String getCurrentNodeName() {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        try{
            WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
            long nodeId = process.getNodeInstances().iterator().next().getNodeId();
            return ((WorkflowProcess) this.kbase.getProcess(this.processId)).getNode(nodeId).getName();
        } finally{
            ksession.dispose();
        }
    }

    public void completeWorkItem(long workItemId, Map<String, Object> outputParameters) {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        ksession.getWorkItemManager().completeWorkItem(workItemId, outputParameters);
        ksession.dispose();
    }

    public Object getProcessVariable(String name) {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        try{
            WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
            return process.getVariable(name);
        } finally{
            ksession.dispose();
        }
    }

    public void setProcessVariable(String name, Object value) {
        StatefulKnowledgeSession ksession = this.getKnowledgeSession();
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.getProcessInstance(this.processInstanceId);
        process.setVariable(name, value);
        ksession.dispose();
    }

    private StatefulKnowledgeSession getKnowledgeSession() {
        StatefulKnowledgeSession ksession = null;
        if (ksessionId == null) {

            ksession = JPAKnowledgeService.newStatefulKnowledgeSession(
                    kbase,
                    null,
                    env);

            ksessionId = ksession.getId();
        } else {
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
