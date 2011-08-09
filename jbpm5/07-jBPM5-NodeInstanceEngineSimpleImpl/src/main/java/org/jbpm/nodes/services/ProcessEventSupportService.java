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
public interface ProcessEventSupportService {
    public void fireBeforeNodeTriggered(NodeInstance node);
    public void fireAfterNodeTriggered(NodeInstance node);
    public void fireBeforeNodeLeft(NodeInstance node);
    public void fireAfterNodeLeft(NodeInstance node);
}
