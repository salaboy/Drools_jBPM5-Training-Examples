/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodeinstances.impl;

import org.jbpm.api.NodeInstance;

/**
 *
 * @author salaboy
 */
public class EndEventNodeInstance extends AbstractNodeInstance {

    @Override
    public void internalTrigger(NodeInstance from, String type) {
        System.out.println("YOU REACH THE END OF THE PROCESS");
    }

   
    
}
