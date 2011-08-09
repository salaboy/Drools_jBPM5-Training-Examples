/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.factories;

import org.jbpm.api.Node;
import org.jbpm.api.NodeInstance;
import org.jbpm.api.ProcessInstance;
import org.jbpm.nodeinstances.impl.ActionNodeInstance;
import org.jbpm.nodeinstances.impl.EndEventNodeInstance;
import org.jbpm.nodeinstances.impl.StartEventNodeInstance;
import org.jbpm.nodes.impl.ActionNode;
import org.jbpm.nodes.impl.EndEventNode;
import org.jbpm.nodes.impl.StartEventNode;

/**
 *
 * @author salaboy
 */
public class NodeInstanceFactory {
    public static NodeInstance newNodeInstance(ProcessInstance processInstance, Node node){
       
        if(node instanceof StartEventNode){
            StartEventNodeInstance startEvent = new StartEventNodeInstance();
            startEvent.setProcessInstance(processInstance);
            startEvent.setNode(node);
            return startEvent;
        }
        if(node instanceof ActionNode){
            ActionNodeInstance actionNode = new ActionNodeInstance(((ActionNode)node).getAction());
            actionNode.setProcessInstance(processInstance);
            actionNode.setNode(node);
            return actionNode;
        }
        
        if(node instanceof EndEventNode){
            EndEventNodeInstance endEvent = new EndEventNodeInstance();
            endEvent.setProcessInstance(processInstance);
            endEvent.setNode(node);
            return endEvent;
        }
        
        return null;
    }
}
