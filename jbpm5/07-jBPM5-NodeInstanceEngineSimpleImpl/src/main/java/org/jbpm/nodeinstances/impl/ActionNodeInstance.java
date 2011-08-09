/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodeinstances.impl;

import org.jbpm.api.Action;
import org.jbpm.api.NodeInstance;
import org.jbpm.api.SequenceFlow;

/**
 *
 * @author salaboy
 */
public class ActionNodeInstance extends AbstractNodeInstance {

    private Action action;

    public ActionNodeInstance(Action action) {
        this.action = action;
    }

    public ActionNodeInstance() {
    }

    @Override
    public void internalTrigger(NodeInstance from, String type) {
        action.execute();
        triggerCompleted(SequenceFlow.FLOW_DEFAULT_TYPE, true);
    }

    public Action getAction() {
        return action;
    }
}
