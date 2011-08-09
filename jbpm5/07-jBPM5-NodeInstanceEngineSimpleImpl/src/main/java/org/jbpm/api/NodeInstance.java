/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api;

/**
 *
 * @author salaboy
 */
public interface NodeInstance {

    public void setNode(Node node);

    public Node getNode();

    public void trigger(NodeInstance from, String type);

    public ProcessInstance getProcessInstance();
}
