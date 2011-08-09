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

/**
 *
 * @author salaboy
 */
public class ProcessInstanceImpl implements ProcessInstance {
    public enum STATUS  {ACTIVE, SUSPENDED, CANCELLED, ENDED};
    private long id;
    private ProcessDefinition process;
    private ContextInstance context;
    private NodeContainer nodeContainer;
    private STATUS status;
    
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
        NodeInstance startEventNode = NodeInstanceFactory.newNodeInstance(this, process.getNodes().get(0L));
        this.nodeContainer.addNodeInstance(startEventNode);
        this.status = STATUS.ACTIVE;
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

    public NodeContainer getNodeContainer() {
        return nodeContainer;
    }

    @Override
    public void triggerCompleted() {
       this.status = STATUS.ENDED;
    }

    public STATUS getStatus() {
        return status;
    }
    
    
    
   
}
