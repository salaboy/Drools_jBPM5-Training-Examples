/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api.impl;

import java.util.HashMap;
import java.util.Map;
import org.jbpm.api.Node;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.SequenceFlow;

/**
 *
 * @author salaboy
 */
public class ProcessDefinitionImpl implements ProcessDefinition{
    private Map<Long, Node> nodes;
    private Map<Long, SequenceFlow> sequenceFlows;
    
    public ProcessDefinitionImpl() {
        this.nodes = new HashMap<Long, Node>();
        this.sequenceFlows = new HashMap<Long, SequenceFlow>();
    }
    
    
    @Override
    public Map<Long, Node> getNodes() {
        return this.nodes;
    }

    @Override
    public void setNodes(Map<Long, Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void addNode(Long id, Node node) {
        if(this.nodes == null){
            this.nodes = new HashMap<Long, Node>();
        }
        this.nodes.put(id, node);
    }

    public Map<Long, SequenceFlow> getSequenceFlows() {
        return sequenceFlows;
    }

    public void setSequenceFlows(Map<Long, SequenceFlow> sequenceFlows) {
        this.sequenceFlows = sequenceFlows;
    }
    
     @Override
    public void addSequenceFlow(Long id, SequenceFlow sequenceFlow) {
        if(this.sequenceFlows == null){
            this.sequenceFlows = new HashMap<Long, SequenceFlow>();
        }
        this.sequenceFlows.put(id, sequenceFlow);
    }
    
    
}
