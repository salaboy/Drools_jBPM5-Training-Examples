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

/**
 *
 * @author salaboy
 */
public class NodeContainerImpl implements NodeContainer{
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
    public void addNodeInstance( NodeInstance node) {
        if(this.nodeInstances == null){
            this.nodeInstances = new ArrayList<NodeInstance>();
        }
        nodeInstances.add(node);
    }
    
    
}
