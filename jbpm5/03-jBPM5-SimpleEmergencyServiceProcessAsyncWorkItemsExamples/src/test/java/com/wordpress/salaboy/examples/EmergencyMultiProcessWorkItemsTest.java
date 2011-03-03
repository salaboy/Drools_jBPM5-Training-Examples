package com.wordpress.salaboy.examples;

import com.wordpress.salaboy.example.persistence.JSONTrackingJobsPersister;
import com.wordpress.salaboy.example.handlers.MyHumanChangingValuesSimulatorWorkItemHandler;
import com.wordpress.salaboy.example.model.Ambulance;
import com.wordpress.salaboy.example.model.Emergency;
import com.wordpress.salaboy.example.model.Vehicle;
import com.wordpress.salaboy.example.persistence.TrackingJobInfo;
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
import org.junit.Before;

public class EmergencyMultiProcessWorkItemsTest {
    private StatefulKnowledgeSession ksession;
    private WorkflowProcessInstance process;
    private MyHumanChangingValuesSimulatorWorkItemHandler humanActivitiesSimHandler;
    private AsyncVehicleTrackingSystem trackingSystem;

    public EmergencyMultiProcessWorkItemsTest() {
    }

    @Before
    public void startProcesses(){
        
        this.trackingSystem = new MyAsyncTrackingSystemMock();
        
        Emergency emergency = new Emergency("555-1234");
        emergency.setType("Heart Attack");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", emergency);
        process = this.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);
        
//        emergency = new Emergency("555-1234");
//        emergency.setType("Heart Attack");
//        parameters = new HashMap<String, Object>();
//        parameters.put("emergency", emergency);
//        this.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);
        
        // My Emergency and My Process are both inserted as Facts / Truths in my Knowledge Session
        // Now Emergency and the Process Instance can be used by the inference engine
        ksession.insert(emergency);
        ksession.insert(process);
    }

    @Test
    public void emergencyWithRulesTest() throws InterruptedException {
        
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
                                    , trackingSystem.queryVehicleStatus(selectedVehicle.getId()));
        
        
        // Is the process completed? It shouldn't be because we didn't stop the tracking
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        
        //Now complete the tracking
        this.trackingSystem.stopTacking(this.trackingSystem.queryVehicleTrackingId(selectedVehicle.getId()));
        
        // Is the process completed now?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());

    }

    
    public void completeWorkItem(long workItemId) {
        getKnowledgeSession().getWorkItemManager().completeWorkItem(workItemId, null);
        JSONTrackingJobsPersister.getInstance().markTrackingJobAsCompleted(null);
    }

    public WorkflowProcessInstance startProcess(String processId, Map<String, Object> parameters) {
        return (WorkflowProcessInstance) getKnowledgeSession().startProcess(processId, parameters);
    }

    private StatefulKnowledgeSession getKnowledgeSession() {
        if (ksession == null) {
            ksession = createSession();
            humanActivitiesSimHandler = new MyHumanChangingValuesSimulatorWorkItemHandler();
            ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

            AsyncStartVehicleTrackingMockSystem trackingSystemHandler = new AsyncStartVehicleTrackingMockSystem(trackingSystem);
            ksession.getWorkItemManager().registerWorkItemHandler("StartTrackingSystem", trackingSystemHandler);
            
            KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        }
        return ksession;
    }

    private StatefulKnowledgeSession createSession() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("EmergencyServiceTracking.bpmn"), ResourceType.BPMN2);
        kbuilder.add(new ClassPathResource("SelectEmergencyVehicleSimple.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            throw new IllegalStateException("Error building kbase!");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        return kbase.newStatefulKnowledgeSession();
    }

    public Map<String,TrackingJobInfo> getUncompleteWorkItems() {
        return JSONTrackingJobsPersister.getInstance().getUncompletedTrackingJobs();
    }

   
}

interface AsyncVehicleTrackingSystem{
    public String startTacking(String vehicleId, String vehicleType, CallBackCommand<Map<String,Object>> callbackCommand);
    public void stopTacking(String trackingId);
    public String queryVehicleStatus(String vehicleId);
    public String queryVehicleTrackingId(String vehicleId);
}


class AsyncStartVehicleTrackingMockSystem implements WorkItemHandler{

    private AsyncVehicleTrackingSystem trackingSystem;
    
    public AsyncStartVehicleTrackingMockSystem(AsyncVehicleTrackingSystem trackingSystem) {
        this.trackingSystem = trackingSystem;
    }

    
    public void executeWorkItem(final WorkItem wi, final WorkItemManager wim) {
       String vehicleId = (String) wi.getParameter("vehicle.id");
       String vehicleType = (String) wi.getParameter("vehicle.type");
       this.trackingSystem.startTacking(vehicleId, vehicleType, new CallBackCommand<Map<String, Object>>(){

            public void execute(Map<String, Object> outputParameters) {
                wim.completeWorkItem(wi.getId(), outputParameters);
            }
           
       });
       
    }

    public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}

class MyAsyncTrackingSystemMock implements AsyncVehicleTrackingSystem{
    private Map<String, CallBackCommand<Map<String,Object>>> callbackMap = new HashMap<String, CallBackCommand<Map<String,Object>>>();
    
    public String startTacking(String vehicleId, String vehicleType, CallBackCommand<Map<String,Object>> callbackCommand){
        String trackingId = UUID.randomUUID().toString();
        this.callbackMap.put(trackingId, callbackCommand );
        TrackingJobInfo info = new TrackingJobInfo(trackingId, vehicleId, vehicleType);
        JSONTrackingJobsPersister.getInstance().markTrackingJobAsStarted(info);
        return trackingId;
    }
    
    public void stopTacking(String trackingId){
        JSONTrackingJobsPersister.getInstance().markTrackingJobAsCompleted(trackingId);
        
        Map<String, Object> outputParameters = new HashMap<String, Object>();
        outputParameters.put("trackingId", trackingId);
        
        this.callbackMap.get(trackingId).execute(outputParameters);
    }
    
    public String queryVehicleStatus(String vehicleId){
        Map<String, TrackingJobInfo> trackingsInfo = JSONTrackingJobsPersister.getInstance().getUncompletedTrackingJobs();
        
        for (Map.Entry<String, TrackingJobInfo> entry : trackingsInfo.entrySet()) {
            if (entry.getValue().getVehicleId().equals(vehicleId)){
                return "Vehicle "+vehicleId+" Located at 5th and A Avenue";
            }
        }
        
        return "There is NO tracking information for the given Id ("+vehicleId+")";
    }

    public String queryVehicleTrackingId(String vehicleId) {
        Map<String, TrackingJobInfo> trackingsInfo = JSONTrackingJobsPersister.getInstance().getUncompletedTrackingJobs();
        
        for (Map.Entry<String, TrackingJobInfo> entry : trackingsInfo.entrySet()) {
            if (entry.getValue().getVehicleId().equals(vehicleId)){
                return entry.getValue().getTrackingId();
            }
        }
        
        return null;
    }
}

interface CallBackCommand<T>{
    public void execute(T data);
}