/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api;

/**
 *
 * @author salaboy
 */
public interface ProcessInstance {
    public void setId(long id);

    public long getId();
    
    public void setProcessDefinition(ProcessDefinition process);

    public ProcessDefinition getProcessDefinition();   
    
    public ContextInstance getContextInstance();
    
    public void start();
}
