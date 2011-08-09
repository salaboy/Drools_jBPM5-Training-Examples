/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodeinstances.impl;

import org.jbpm.api.NodeInstance;
import org.jbpm.api.SequenceFlow;

/**
 *
 * @author salaboy
 */
public class StartEventNodeInstance extends AbstractNodeInstance {
    private long id;
   
    
    
    public StartEventNodeInstance() {
    }
 
   

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

   

    @Override
    public void internalTrigger(NodeInstance from, String type) {
        if (type != null) {
            throw new IllegalArgumentException(
                "A StartNode does not accept incoming connections!");
        }
        if (from != null) {
            throw new IllegalArgumentException(
                "A StartNode can only be triggered by the process itself!");
        }
        triggerCompleted();
    }

    public void triggerCompleted() {
        triggerCompleted(SequenceFlow.FLOW_DEFAULT_TYPE, true);
    }
    
    
    
    
    
    
    
}
