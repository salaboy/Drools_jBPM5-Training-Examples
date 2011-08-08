/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodeinstances.impl;

import org.jbpm.api.Node;
import org.jbpm.api.NodeInstance;

/**
 *
 * @author salaboy
 */
public class ActionNodeInstance implements NodeInstance{

    private Node node;

    public ActionNodeInstance() {
    }
    
    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void trigger(NodeInstance from, String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
