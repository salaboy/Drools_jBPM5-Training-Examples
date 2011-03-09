package com.wordpress.salaboy.examples;

import com.wordpress.salaboy.example.persistence.JSONTrackingJobsPersister;
import com.wordpress.salaboy.example.handlers.MyHumanChangingValuesSimulatorWorkItemHandler;
import com.wordpress.salaboy.example.model.Ambulance;
import com.wordpress.salaboy.example.model.Emergency;
import com.wordpress.salaboy.example.model.Vehicle;
import com.wordpress.salaboy.example.persistence.ActiveWorkItemPersister;
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

/**
 * This test shows a more real scenario:
 * WorkItemHandler start a job in an External System (VehicleTrackingSystem) and
 * then uses another system (ActiveWorkItemPersister) to store the information 
 * of the execution.
 * At this point, the application running the process could fail. 
 * ActiveWorkItemPersister will store all the needed information to continue
 * the execution of the process when VehicleTrackingSystem ends. 
 * Because this is just a test, ActiveWorkItemPersister doesn't really support
 * a system crash because is sharing the same ksession with EmergencyProcessExternalSystemsWorkItemsTest.
 * 
 */
public class EmergencyProcessExternalSystemsWorkItemsTest {
    
    /**
     * The omnipresent ksession. This session will leave along the whole test.
     */
    private StatefulKnowledgeSession ksession;
    
    /**
     * The process instance
     */
    private WorkflowProcessInstance process;
    
    /**
     * The Work Item Handler in charge of communication with External System
     */
    private MyHumanChangingValuesSimulatorWorkItemHandler humanActivitiesSimHandler;
    
    /**
     * The external system
     */
    private VehicleTrackingSystem trackingSystem;

    public EmergencyProcessExternalSystemsWorkItemsTest() {
    }

    /**
     * Creates a ksession and starts the process
     */
    @Before
    public void startProcesses(){
        
        //Creates a new TrackingSystem
        this.trackingSystem = new MyAsyncTrackingSystemMock();
        
        //Creates a new emergency and set it as process input parameter
        Emergency emergency = new Emergency("555-1234");
        emergency.setType("Heart Attack");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("emergency", emergency);
        
        //Starts the process
        process = this.startProcess("com.wordpress.salaboy.bpmn2.SimpleEmergencyService", parameters);
        
        // My Emergency and My Process are both inserted as Facts / Truths in my Knowledge Session
        // Now Emergency and the Process Instance can be used by the inference engine
        ksession.insert(emergency);
        ksession.insert(process);
    }

    /**
     * This test will complete the first two Human Activities. When the process
     * enters the Work Item, it will communicate to the external system. The process
     * will be waiting in the Work Item  for the external system to complete.
     * When the external system complete the request, the process is continued.
     * @throws InterruptedException 
     */
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
        
        //Start Tracking is not automatic. The external system has to finish first.
        //Until then, the process will be waiting in StartTrackingSystem
        Assert.assertEquals("Vehicle "+selectedVehicle.getId()+" Located at 5th and A Avenue"
                                    , trackingSystem.queryVehicleStatus(selectedVehicle.getId()));
        
        // Is the process completed? It shouldn't be because the External System didn't finish yet.
        Assert.assertEquals(ProcessInstance.STATE_ACTIVE, process.getState());
        
        //Now complete the external system.
        this.trackingSystem.stopTacking(this.trackingSystem.queryVehicleTrackingId(selectedVehicle.getId()));
        
        // Is the process completed now?
        Assert.assertEquals(ProcessInstance.STATE_COMPLETED, process.getState());
    }

    
    /**
     * Start the process with the given Id
     * @param processId the process Id
     * @param parameters the input parameters
     * @return 
     */
    public WorkflowProcessInstance startProcess(String processId, Map<String, Object> parameters) {
        return (WorkflowProcessInstance) getKnowledgeSession().startProcess(processId, parameters);
    }

    /**
     * Creates and configures a ksession. It set up all the needed handlers and
     * logger. It also initialize the ActiveWorkItemPersister "system". 
     * Successive calls to this method will return the same ksession. 
     * @return 
     */
    private StatefulKnowledgeSession getKnowledgeSession() {
        if (ksession == null) {
            //Creates a ksession
            ksession = createSession();
            
            //Registers the handler for "Human Task" 
            humanActivitiesSimHandler = new MyHumanChangingValuesSimulatorWorkItemHandler();
            ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanActivitiesSimHandler);

            //Register the handler for "StartTrackingSystem" Work Item 
            AsyncStartVehicleTrackingMockSystem trackingSystemHandler = new AsyncStartVehicleTrackingMockSystem(trackingSystem);
            ksession.getWorkItemManager().registerWorkItemHandler("StartTrackingSystem", trackingSystemHandler);
            
            //Configures a logger for the session
            KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
            
            //Initialize the ActiveWorkItemPersister "system" 
            ActiveWorkItemPersister.getInstance().setKsession(ksession);
        }
        return ksession;
    }

    /**
     * Creates a new kasession containing the process definition and a set
     * of validation rules.
     * @return 
     */
    private StatefulKnowledgeSession createSession() {
        //Creates a builder
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
        //Adds resources to the builder
        kbuilder.add(new ClassPathResource("EmergencyServiceTracking.bpmn"), ResourceType.BPMN2);
        kbuilder.add(new ClassPathResource("SelectEmergencyVehicleSimple.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        
        //Checks for errors
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            throw new IllegalStateException("Error building kbase!");
        }

        //Creates a new kbase and add all the packages from the builder
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        return kbase.newStatefulKnowledgeSession();
    }
   
}

/**
 * An asynchronous implementation of a WorkItemHandler. 
 * This implementation will start a a job in an external system (VehicleTrackingSystem)
 * and register the job (only the id of the job and the id of the work item)
 * in another system (ActiveWorkItemPersister).
 * This is implementation is asynchronous because it doesn't complete the 
 * work item. The responsible to complete it is VehicleTrackingSystem through
 * ActiveWorkItemPersister.
 * @author esteban
 */
class AsyncStartVehicleTrackingMockSystem implements WorkItemHandler{

    private VehicleTrackingSystem trackingSystem;
    
    /**
     * Constructs a AsyncStartVehicleTrackingMockSystem. The given 
     * VehicleTrackingSystem is used to start a job.
     * @param trackingSystem 
     */
    public AsyncStartVehicleTrackingMockSystem(VehicleTrackingSystem trackingSystem) {
        this.trackingSystem = trackingSystem;
    }


    /**
     * This method will start a job in an external system (VehicleTrackingSystem)
     * and register it in ActiveWorkItemPersister.
     * This method WILL NOT complete the work item.
     * @param wi
     * @param wim 
     */
    public void executeWorkItem(final WorkItem wi, final WorkItemManager wim) {
       //Gets the parameters needed by the external system
       String vehicleId = (String) wi.getParameter("vehicle.id");
       String vehicleType = (String) wi.getParameter("vehicle.type");
       
       //Starts a job in the external system. This execution is async. It will
       //inmediatly return a job id, but the execution will take some time. 
       String trackingId = this.trackingSystem.startTacking(vehicleId, vehicleType);
       
       //Register the workItem/trackingId pair into ActiveWorkItemPersister.
       //ActiveWorkItemPersister will be used later by VehicleTrackingSystem
       //in order to complete the work item.
       ActiveWorkItemPersister.getInstance().registerWorkItem(wi.getId(), trackingId);
       
    }

    public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}

/**
 * Implementation of VehicleTrackingSystem that makes use of ActiveWorkItemPersister
 * to notify when a job is actually finished.
 * @author esteban
 */
class MyAsyncTrackingSystemMock implements VehicleTrackingSystem{
    
    /**
     * Starts a new tracking job. This method emulates an async call, where a job
     * id is immediately returned, but the job execution is queued.
     * @param vehicleId
     * @param vehicleType
     * @return 
     */
    public String startTacking(String vehicleId, String vehicleType){
        //Creates an id for the job
        String trackingId = UUID.randomUUID().toString();
        
        //Persist the job. This could emulate the queuing of the job for later
        //execution
        TrackingJobInfo info = new TrackingJobInfo(trackingId, vehicleId, vehicleType);
        JSONTrackingJobsPersister.getInstance().markTrackingJobAsStarted(info);
        
        //Return the job id
        return trackingId;
    }
    
    /**
     * Finishes a tracking job. When a job is ended, this method notifies
     * ActiveWorkItemPersister so the process could continue its execution.
     * @param trackingId the id of the tracking job.
     */
    public void stopTacking(String trackingId){
        //Removes the job from the persistent storage.
        JSONTrackingJobsPersister.getInstance().markTrackingJobAsCompleted(trackingId);
        
        //Creates the output parameters
        Map<String, Object> outputParameters = new HashMap<String, Object>();
        outputParameters.put("trackingId", trackingId);
        
        //Notifies ActiveWorkItemPersister
        ActiveWorkItemPersister.getInstance().externalSystemJobCompleted(trackingId, outputParameters);
    }
    
    /**
     * Returns the tracking status of a vehicle.
     * @param vehicleId the vehicle id.
     * @return a String representing the tracking status of the vehicle
     */
    public String queryVehicleStatus(String vehicleId){
        Map<String, TrackingJobInfo> trackingsInfo = JSONTrackingJobsPersister.getInstance().getUncompletedTrackingJobs();
        
        for (Map.Entry<String, TrackingJobInfo> entry : trackingsInfo.entrySet()) {
            if (entry.getValue().getVehicleId().equals(vehicleId)){
                return "Vehicle "+vehicleId+" Located at 5th and A Avenue";
            }
        }
        
        return "There is NO tracking information for the given Id ("+vehicleId+")";
    }

    /**
     * Returns the tracking job id for a vehicle.
     * @param vehicleId the id of the vehicle.
     * @return 
     */
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