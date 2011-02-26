package com.wordpress.salaboy.examples;

import com.wordpress.salaboy.example.handlers.MyHumanChangingValuesSimulatorWorkItemHandler;
import com.wordpress.salaboy.example.model.Ambulance;
import com.wordpress.salaboy.example.model.Emergency;
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
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 2/14/11
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmergencyProcessWorkItemsTest {
    private static StatefulKnowledgeSession ksession;

    public EmergencyProcessWorkItemsTest() {
    }

    

    @Test
    public void emergencyWithRulesTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceTracking.bpmn"), ResourceType.BPMN2);
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
        MyTrackingSystemMock trackingSystem = new MyTrackingSystemMock();
        StartVehicleTrackingMockSystem trackingSystemHandler = new StartVehicleTrackingMockSystem(trackingSystem);
        ksession.getWorkItemManager().registerWorkItemHandler("StartTrackingSystem", trackingSystemHandler);

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
        Vehicle selectedVehicle = ((Vehicle) process.getVariable("vehicle"));
        Assert.assertTrue(selectedVehicle instanceof Ambulance);
        
        
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
        
        //Start Tracking and Reporting are automatic -> Check the report in the console
        //We can also check against the tracking system itself
        Assert.assertEquals("Vehicle "+selectedVehicle.getId()+" Located at 5th and A Avenue"
                                    , trackingSystem.queryTackingStatus(selectedVehicle.getId()));
        
        
        // Is the process completed?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());

    }


   
}

class StartVehicleTrackingMockSystem implements WorkItemHandler{

    private VehicleTrackingSystem trackingSystem;
    
    public StartVehicleTrackingMockSystem(VehicleTrackingSystem trackingSystem) {
        this.trackingSystem = trackingSystem;
    }

    
    public void executeWorkItem(WorkItem wi, WorkItemManager wim) {
       String vehicleId = (String) wi.getParameter("vehicle.id");
       String vehicleType = (String) wi.getParameter("vehicle.type");
       String trackingId = this.trackingSystem.startTacking(vehicleId, vehicleType);
       Map<String, Object> outputParameters = new HashMap<String, Object>();
       outputParameters.put("trackingId", trackingId);
       wim.completeWorkItem(wi.getId(), outputParameters);
       
    }

    public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}

interface VehicleTrackingSystem{
    public String startTacking(String vehicleId, String vehicleType);
    public void stopTacking(String vehicleId);
    public String queryTackingStatus(String trackingId);
}

class MyTrackingSystemMock implements VehicleTrackingSystem{
    private Map<String, String> currentTrackingsById = new HashMap<String, String>();
    private Map<String, String> currentTrackingsByVehicleId = new HashMap<String, String>();
    
    public String startTacking(String vehicleId, String vehicleType){
        String trackingId = UUID.randomUUID().toString();
        this.currentTrackingsById.put(trackingId, vehicleId );
        this.currentTrackingsByVehicleId.put(vehicleId, trackingId );
        return trackingId;
    }
    
    public void stopTacking(String vehicleId){
        this.currentTrackingsById.remove(vehicleId);
    }
    
    public String queryTackingStatus(String vehicleId){
        String trackingId = this.currentTrackingsByVehicleId.get(vehicleId);
        if(trackingId != null && !trackingId.equals("")){
            return "Vehicle "+vehicleId+" Located at 5th and A Avenue";
        }else{
            return "There is tracking for the vehicle with id = "+vehicleId;
        }
    }
}