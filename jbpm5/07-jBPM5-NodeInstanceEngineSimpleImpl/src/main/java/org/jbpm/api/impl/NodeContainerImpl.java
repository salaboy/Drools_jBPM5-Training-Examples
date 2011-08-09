/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api.impl;

import java.util.ArrayList;
import java.util.List;
import org.jbpm.api.Node;
import org.jbpm.api.NodeContainer;
import org.jbpm.api.NodeInstance;
import org.jbpm.nodeinstances.impl.AbstractNodeInstance;
import org.jbpm.nodeinstances.impl.EndEventNodeInstance;

/**
 *
 * @author salaboy
 */
public class NodeContainerImpl implements NodeContainer {

    private List<NodeInstance> nodeInstances;

    public NodeContainerImpl() {
        this.nodeInstances = new ArrayList<NodeInstance>();
    }

    public List<NodeInstance> getNodeInstances() {
        return nodeInstances;
    }

    public void setNodeInstances(List<NodeInstance> nodes) {
        this.nodeInstances = nodes;
    }

    @Override
    public void addNodeInstance(NodeInstance node) {
        if (this.nodeInstances == null) {
            this.nodeInstances = new ArrayList<NodeInstance>();
        }
        nodeInstances.add(node);
    }

    @Override
    public void removeNodeInstance(NodeInstance node) {
        nodeInstances.remove(node);
    }

    @Override
    public NodeInstance getNodeInstance(Node node) {
        for (NodeInstance nodeInstance : this.nodeInstances) {
            if (nodeInstance.getNode() == node) {
                return nodeInstance;
            }
        }
        return null;

    }

    @Override
    public void nodeInstanceCompleted(NodeInstance nodeInstance, String outType) {
        if (nodeInstance instanceof EndEventNodeInstance) {

            if (nodeInstances.isEmpty()) {
                nodeInstance.getProcessInstance().triggerCompleted();
            }

        }


    }
}
