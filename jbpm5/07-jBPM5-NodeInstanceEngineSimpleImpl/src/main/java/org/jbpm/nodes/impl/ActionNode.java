/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodes.impl;

import org.jbpm.api.Action;

/**
 *
 * @author salaboy
 */
public class ActionNode extends AbstractBaseNode {
    private Action action;

    public ActionNode(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
    
    
}
