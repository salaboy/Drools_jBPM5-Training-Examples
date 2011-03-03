package com.wordpress.salaboy.examples;

import com.wordpress.salaboy.example.handlers.MyHumanChangingValuesSimulatorWorkItemHandler;
import com.wordpress.salaboy.example.model.Ambulance;
import com.wordpress.salaboy.example.model.Emergency;
import com.wordpress.salaboy.example.model.FireTruck;
import com.wordpress.salaboy.example.model.Vehicle;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 2/14/11
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleEmergencyProcessTest {
    private static StatefulKnowledgeSession ksession;

    public SimpleEmergencyProcessTest() {
    }

    /*
     * This test shows how to interact with a business process that contains
     * human tasks activities using an Automatic Human Task Activities simulator. This business process
     * also contains a BusinessRuleTask activity defined. A set of rules will be evaluated when this activity is
     * executed by the process. Take a look at the comments in the test for more explanations.
     * When a Human Task is found inside the business process, the  MyHumanActivitySimulatorWorkItemHandler
     * automatically completes the task causing the process to advance to the next activity.
     */
    @Test
    public void automaticHumanTasksTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyAutomaticHumanSimulatorWorkItemHandler humanActivitiesSimHandler = new MyAutomaticHumanSimulatorWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
 
        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService");

        //I need to call the FireAllRules method because I have a RuleTask in my business process
        ksession.fireAllRules();
        
        // Is the process completed?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());


    }

    /*
     * This test shows how to interact with a business process that contains
     * human tasks activities using an Automatic Human Task Activities simulator. This business process
     * also contains a BusinessRuleTask activity defined. A set of rules will be evaluated when this activity is
     * executed by the process. Take a look at the comments in the test for more explanations.
     * When a Human Task is found inside the business process, the  MyHumanActivitySimulatorWorkItemHandler
     * automatically completes the task causing the process to advance to the next activity.
     */
    @Test
    public void nonAutomaticHumanTasksTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        ksession = kbase.newStatefulKnowledgeSession();
        MyNonAutomaticHumanSimulatorWorkItemHandler humanActivitiesSimHandler = new MyNonAutomaticHumanSimulatorWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService");

        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Ask for Emergency Information" activity?
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        
        System.out.println("Completing the first Activity !");
        //Complete the first human activity
        humanActivitiesSimHandler.completeWorkItem(null);
        
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());

        //I need to call the FireAllRules method because I have a RuleTask inside the business process
        ksession.fireAllRules();
        
        System.out.println("Completing the second Activity");
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Dispatch Vehicle" activity?
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        //Complete the second human activity
        humanActivitiesSimHandler.completeWorkItem(null);

        // Is the process completed?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());
    }

    @Test
    public void inputDataTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyHumanChangingValuesSimulatorWorkItemHandler humanActivitiesSimHandler = new MyHumanChangingValuesSimulatorWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);


        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", new Emergency("555-1234"));


        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);

        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Ask for Emergency Information" activity?
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        // Lets check the value of the emergency.getRevision(), it should be 1 
        Assert.assertEquals(1, ((Emergency) process.getVariable("emergency")).getRevision());
        
        
        //Complete the first human activity
        System.out.println("Completing the first Activity");
        humanActivitiesSimHandler.completeWorkItem();
        
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        
        //I need to call the FireAllRules method because I have a RuleTask inside the business process
        ksession.fireAllRules();
        
        
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Dispatch Vehicle" activity?
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        // Lets check the value of the emergency.getRevision(), it should be 2, because the work item handler changes it 
        Assert.assertEquals(2, ((Emergency) process.getVariable("emergency")).getRevision());
        
        System.out.println("Completing the second Activity");
        //Complete the second human activity
        humanActivitiesSimHandler.completeWorkItem();

        // Is the process completed?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());
        
    }

    @Test
    public void emergencyWithRulesTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        kbuilder.add(new ClassPathResource("SelectEmergencyVehicleSimple.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        MyHumanChangingValuesSimulatorWorkItemHandler humanActivitiesSimHandler = new MyHumanChangingValuesSimulatorWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

       
        Emergency emergency = new Emergency("555-1234");
        emergency.setType("Heart Attack");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", emergency);


        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession
                                            .startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);
        // My Emergency and My Process are both inserted as Facts / Truths in my Knowledge Session
        // Now Emergency and the Process Instance can be used by the inference engine
        ksession.insert(emergency);
        ksession.insert(process);
        
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Ask for Emergency Information" activity?
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        // Lets check the value of the emergency.getRevision(), it should be 1 
        Assert.assertEquals(1, ((Emergency) process.getVariable("emergency")).getRevision());
        
        System.out.println("Completing the first Activity");
        //Complete the first human activity
        humanActivitiesSimHandler.completeWorkItem();
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        
        //I need to call the FireAllRules method because I have a RuleTask inside the business process
        int fired = ksession.fireAllRules();
        // Now that I have rules being evaluated, at least one should fire
        Assert.assertEquals(1, fired);
        
        
        // Lets check the value of the vehicle variable it should be an Ambulance => Heart Attack 
        Assert.assertTrue(((Vehicle) process.getVariable("vehicle")) instanceof Ambulance);
        
        
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Dispatch Vehicle" activity?
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        // Lets check the value of the emergency.getRevision(), it should be 1 
        Assert.assertEquals(2, ((Emergency) process.getVariable("emergency")).getRevision());
        
        
        
        System.out.println("Completing the second Activity");
        //Complete the second human activity
        humanActivitiesSimHandler.completeWorkItem();
        
        // Is the process completed?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());

    }


    @Test
    public void reactiveProcessAndRulesTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceSimple.bpmn"), ResourceType.BPMN2);
        kbuilder.add(new ClassPathResource("SelectEmergencyVehicleSimple.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            return;
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        ksession = kbase.newStatefulKnowledgeSession();
        
        //Setting the process engine and the rule engine in reactive mode
        // This will cause that if a rule is activated, the rule will fire without waiting
        // the user to call the fireAllRules() method.
        new Thread(new Runnable() {

            public void run() {
                ksession.fireUntilHalt();
            }
        }).start();
        
        
        MyHumanChangingValuesSimulatorWorkItemHandler humanActivitiesSimHandler = new MyHumanChangingValuesSimulatorWorkItemHandler();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);


        Emergency emergency = new Emergency("555-1234");
        //Run with Heart Attack and check the output. An Ambulance must appear in the report
        //emergency.setType("Heart Attack");
        //Run with Fire and check the output. A FireTruck must appear in the report
        emergency.setType("Fire");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", emergency);


        WorkflowProcessInstance process = (WorkflowProcessInstance) ksession
                                            .startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);
        // My Emergency and My Process are both inserted as Facts / Truths in my Knowledge Session
        // Now Emergency and the Process Instance can be used by the inference engine
        ksession.insert(emergency);
        ksession.insert(process);
        
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Ask for Emergency Information" activity?
        Assert.assertEquals("Ask for Emergency Information", process.getNodeInstances().iterator().next().getNodeName());
        // Lets check the value of the emergency.getRevision(), it should be 1 
        Assert.assertEquals(1, ((Emergency) process.getVariable("emergency")).getRevision());
        
        System.out.println("Completing the first Activity");
        //Complete the first human activity
        humanActivitiesSimHandler.completeWorkItem();
        
        
        // I need to sleep for a little while, because the other thread can be executing some activated rules
        Thread.sleep(1000);
        
        // Lets check the value of the vehicle variable it should be a Fire Truck => Fire 
        Assert.assertTrue(((Vehicle) process.getVariable("vehicle")) instanceof FireTruck);
        
        
        // Is the Process still Active?
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        // Is there a running node instance?
        Assert.assertEquals(1, process.getNodeInstances().size());
        // Is the process stopped in the "Dispatch Vehicle" activity?
        Assert.assertEquals("Dispatch Vehicle", process.getNodeInstances().iterator().next().getNodeName());
        // Lets check the value of the emergency.getRevision(), it should be 2
        Assert.assertEquals(2, ((Emergency) process.getVariable("emergency")).getRevision());
        
        System.out.println("Completing the second Activity");
        //Complete the second human activity
        humanActivitiesSimHandler.completeWorkItem();
        
        // Is the process completed?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());
    }


}

class MyNonAutomaticHumanSimulatorWorkItemHandler implements WorkItemHandler {
    private WorkItemManager workItemManager;
    private long workItemId;

    public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
        this.workItemId = workItem.getId();
        this.workItemManager = workItemManager;
        System.out.println("Map of Parameters = " + workItem.getParameters());
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

    }

    public void completeWorkItem(Map<String, Object> parameters) {
        this.workItemManager.completeWorkItem(this.workItemId, parameters);

    }

}




class MyAutomaticHumanSimulatorWorkItemHandler implements WorkItemHandler {

    public void executeWorkItem(WorkItem workItem, WorkItemManager workItemManager) {
        System.out.println("Map of Parameters = " + workItem.getParameters());
        workItemManager.completeWorkItem(workItem.getId(), null);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager workItemManager) {

    }


}
