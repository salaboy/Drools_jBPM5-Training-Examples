/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api;

import java.util.List;

/**
 *
 * @author salaboy
 */
public interface NodeContainer {
    public List<NodeInstance> getNodeInstances();
    public void setNodeInstances(List<NodeInstance> nodes);
    public void addNodeInstance(NodeInstance node);
    public void removeNodeInstance(NodeInstance node);
    public NodeInstance getNodeInstance(Node node);
    public void nodeInstanceCompleted(NodeInstance nodeInstance, String outType);
}
