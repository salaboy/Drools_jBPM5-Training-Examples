/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.example.persistence;

import java.util.Map;

/**
 * Utility class that encapsulates a Knowledge Session and a process instance.
 * Using a persistent ksession abstraction makes the process of create or 
 * retrieve the session from the database transparent.
 * @author esteban
 */
public interface ProcessManager {

    /**
     * Completes a Work Item
     * @param workItemId
     * @param outputParameters 
     */
    void completeWorkItem(long workItemId, Map<String, Object> outputParameters);

    /**
     * Fire all the activated rules
     * @return 
     */
    int fireAllRules();

    /**
     * Returns the name of the current node
     * @return 
     */
    String getCurrentNodeName();

    /**
     * Returns the quantity of running nodes
     * @return 
     */
    int getNodeInstancesSize();

    /**
     * Returns the process state 
     * @return 
     */
    int getProcessState();

    /**
     * Retrieve the value of a process variable
     * @param name
     * @return 
     */
    Object getProcessVariable(String name);

    /**
     * Insert an object in the underlying ksession
     * @param fact 
     */
    void insertFact(Object fact);

    /**
     * Inserts the current process into the underlying ksession
     */
    void insertProcess();

    /**
     * Returns true if the current process instance is completed.
     * @return 
     */
    boolean isProcessInstanceCompleted();

    /**
     * Sets the value of a process variable
     */
    void setProcessVariable(String name, Object value);

    /**
     * Starts a process instance
     * @param parameters 
     */
    void startProcess(Map<String, Object> parameters);

}
