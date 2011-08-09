/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodes.services;

import org.jbpm.api.NodeInstance;

/**
 *
 * @author salaboy
 */
public class DefaultProcessEventSupportService implements ProcessEventSupportService{

    @Override
    public void fireBeforeNodeTriggered(NodeInstance node) {
        System.out.println("BEFORE NODE FIRED: "+ node.getNode());
    }

    @Override
    public void fireAfterNodeTriggered(NodeInstance node) {
        System.out.println("AFTER NODE FIRED: "+ node.getNode());
    }

    @Override
    public void fireBeforeNodeLeft(NodeInstance node) {
        System.out.println("BEFORE NODE LEFT: "+ node.getNode());
    }

    @Override
    public void fireAfterNodeLeft(NodeInstance node) {
        System.out.println("AFTER NODE LEFT: "+ node.getNode());
    }
    
}
