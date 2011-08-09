/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm;

import org.jbpm.api.Action;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.SequenceFlow;
import org.jbpm.factories.ProcessInstanceFactory;
import org.jbpm.api.impl.ProcessDefinitionImpl;
import org.jbpm.nodes.impl.ActionNode;
import org.jbpm.nodes.impl.EndEventNode;
import org.jbpm.nodes.impl.SequenceFlowImpl;
import org.jbpm.nodes.impl.StartEventNode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author salaboy
 */
public class SimpleProcessExecutionTest {
    
    public SimpleProcessExecutionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void simpleProcessExecution() {
        ProcessDefinition process = new ProcessDefinitionImpl();
        StartEventNode startEvent = new StartEventNode();
        process.addNode(0L, startEvent);
        ActionNode actionNode = new ActionNode(new Action() {

            @Override
            public void execute() {
                System.out.println("Executing the Action!!");
            }
        });
        process.addNode(1L, actionNode);
        EndEventNode endEvent = new EndEventNode();
        process.addNode(2L, endEvent);
        
        startEvent.addOutgoingFlow(SequenceFlow.FLOW_DEFAULT_TYPE, new SequenceFlowImpl(SequenceFlow.FLOW_DEFAULT_TYPE, actionNode));
        actionNode.addOutgoingFlow(SequenceFlow.FLOW_DEFAULT_TYPE, new SequenceFlowImpl(SequenceFlow.FLOW_DEFAULT_TYPE, endEvent));
        
        ProcessInstance processInstance = ProcessInstanceFactory.newProcessInstance(process);
                
        processInstance.start();        
    
    }
}
