/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.factories;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.impl.ProcessInstanceImpl;

/**
 *
 * @author salaboy
 */
public class ProcessInstanceFactory {
    public static ProcessInstance newProcessInstance(ProcessDefinition process){
        ProcessInstance instance = new ProcessInstanceImpl(process);
        return instance;
    }
}
