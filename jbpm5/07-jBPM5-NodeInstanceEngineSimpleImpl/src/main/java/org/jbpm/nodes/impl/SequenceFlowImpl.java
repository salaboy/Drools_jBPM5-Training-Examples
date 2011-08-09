/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodes.impl;

import org.jbpm.api.Node;
import org.jbpm.api.SequenceFlow;

/**
 *
 * @author salaboy
 */
public class SequenceFlowImpl implements SequenceFlow{
    private Node from;
    private Node to;
    private String fromType;
    private String toType;

    public SequenceFlowImpl(String toType, Node to) {
        this.to = to;
        this.toType = toType;
    }
    
    @Override
    public Node getFrom() {
        return from;
    }

    @Override
    public Node getTo() {
        return to;
    }

    @Override
    public String getFromType() {
        return fromType;
    }

    @Override
    public String getToType() {
        return toType;
    }
    

    
    
    
    
}
