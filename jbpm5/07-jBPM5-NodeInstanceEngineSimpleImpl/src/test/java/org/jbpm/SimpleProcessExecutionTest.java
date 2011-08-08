/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
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
        process.addNode(0L, new StartEventNode());
        process.addNode(1L, new ActionNode());
        process.addNode(2L, new EndEventNode());
        
        process.addSequenceFlow(0L, new SequenceFlowImpl(0L, 1L));
        process.addSequenceFlow(1L, new SequenceFlowImpl(1L, 2L));
        
        ProcessInstance processInstance = ProcessInstanceFactory.newProcessInstance(process);
                
        processInstance.start();        
    
    }
}
