/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodeinstances.impl;

import java.util.List;
import org.jbpm.api.Node;
import org.jbpm.api.NodeInstance;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.SequenceFlow;
import org.jbpm.factories.NodeInstanceFactory;
import org.jbpm.nodes.services.ProcessEventSupportService;
import org.jbpm.nodes.services.ProcessEventSupportServiceFactory;

/**
 *
 * @author salaboy
 */
public abstract class AbstractNodeInstance implements NodeInstance {
    
    private ProcessInstance processInstance;
    private Node node;

    public final void trigger(NodeInstance from, String type) {

        //Fire before Node Triggered

        getProcessEventSupportService().fireBeforeNodeTriggered(this);

        internalTrigger(from, type);

        //Fire after Node Triggered
        getProcessEventSupportService().fireAfterNodeTriggered(this);

    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public abstract void internalTrigger(NodeInstance from, String type);

    private ProcessEventSupportService getProcessEventSupportService() {
        return ProcessEventSupportServiceFactory.getService();
    }

    protected void triggerCompleted(String type, boolean remove) {
        if (remove) {

            processInstance.getNodeContainer().removeNodeInstance(this);
        }
        Node node = getNode();
        List<SequenceFlow> flows = null;
        if (node != null) {
            flows = node.getOutgoingFlows(type);
        }
        if (flows == null || flows.isEmpty()) {
            processInstance.getNodeContainer().nodeInstanceCompleted(this, type);
        } else {
            for (SequenceFlow flow : flows) {
                triggerConnection(flow);
            }
        }
    }

    protected void triggerConnection(SequenceFlow flow) {

        getProcessEventSupportService().fireBeforeNodeLeft(this);
        this.processInstance.getNodeContainer().addNodeInstance(NodeInstanceFactory.newNodeInstance(this.processInstance, flow.getTo()));
        // trigger next node
        this.processInstance.getNodeContainer().getNodeInstance(flow.getTo()).trigger(this, flow.getToType());

        getProcessEventSupportService().fireAfterNodeLeft(this);

    }

    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public Node getNode() {
        return this.node;
    }
}
