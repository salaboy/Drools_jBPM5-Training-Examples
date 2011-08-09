/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api;

/**
 *
 * @author salaboy
 */
public interface SequenceFlow {
    public static final String FLOW_DEFAULT_TYPE = "DEFAULT_FLOW";
    
    public Node getFrom();

    public Node getTo();

    public String getFromType();

    public String getToType();
}
