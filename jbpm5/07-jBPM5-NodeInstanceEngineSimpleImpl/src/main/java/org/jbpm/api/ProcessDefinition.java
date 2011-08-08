/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api;

import java.util.Map;

/**
 *
 * @author salaboy
 */
public interface ProcessDefinition {
    public Map<Long, Node> getNodes();
    public void setNodes(Map<Long, Node> nodes);
    public void addNode(Long id, Node node);
    public void addSequenceFlow(Long id, SequenceFlow sequenceFlow);
}
