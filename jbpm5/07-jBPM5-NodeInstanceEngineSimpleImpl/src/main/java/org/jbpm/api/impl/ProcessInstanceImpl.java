/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api.impl;

import java.util.Random;
import org.jbpm.api.ContextInstance;
import org.jbpm.api.NodeContainer;
import org.jbpm.api.NodeInstance;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.factories.NodeInstanceFactory;
import org.jbpm.nodeinstances.impl.StartEventNodeInstance;
import org.jbpm.nodes.impl.StartEventNode;

/**
 *
 * @author salaboy
 */
public class ProcessInstanceImpl implements ProcessInstance {

    private long id;
    private ProcessDefinition process;
    private ContextInstance context;
    private NodeContainer nodeContainer;
    public ProcessInstanceImpl() {
    }

    public ProcessInstanceImpl(ProcessDefinition process) {
        this.id = new Random().nextLong();
        this.process = process;
        this.context = new ContextInstanceImpl();
        this.nodeContainer = new NodeContainerImpl();
    }

    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public ProcessDefinition getProcessDefinition() {
        return process;
    }

    @Override
    public void start() {
        // We should check that the first node inside the process.nodes is a startEventNode
        StartEventNodeInstance startEventNode = NodeInstanceFactory.newStartEventNodeInstance((StartEventNode)process.getNodes().get(0));
        this.nodeContainer.addNodeInstance(startEventNode);
        startEventNode.trigger(null, null);
    	
    }

    @Override
    public void setProcessDefinition(ProcessDefinition process) {
        this.process = process;
    }

    @Override
    public ContextInstance getContextInstance() {
        return context;
    }
    
   
}
