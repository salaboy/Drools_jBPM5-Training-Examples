/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.factories;

import org.jbpm.nodeinstances.impl.StartEventNodeInstance;
import org.jbpm.nodes.impl.StartEventNode;

/**
 *
 * @author salaboy
 */
public class NodeInstanceFactory {
    public static StartEventNodeInstance newStartEventNodeInstance(StartEventNode node){
        StartEventNodeInstance startEvent = new StartEventNodeInstance();
        startEvent.setNode(node);
        return startEvent;
    }
}
