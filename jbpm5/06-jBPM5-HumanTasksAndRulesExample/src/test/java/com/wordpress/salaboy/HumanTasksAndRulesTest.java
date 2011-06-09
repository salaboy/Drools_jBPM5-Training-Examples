/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

import java.util.List;
import org.jbpm.task.query.TaskSummary;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.QueryResultsRow;
import org.drools.time.SessionPseudoClock;
import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author salaboy
 */
public class HumanTasksAndRulesTest {

    private TaskServerDaemon taskServerDaemon = new TaskServerDaemon();
    private SessionPseudoClock clock;

    public HumanTasksAndRulesTest() {
    }

    @Before
    public void setUp() throws InterruptedException {
        taskServerDaemon.startServer();

        Thread.sleep(3000);


    }

    @After
    public void tearDown() throws Exception {
        taskServerDaemon.stopServer();
    }

    @Test
    public void simpleTest() throws InterruptedException {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        //Adds resources to the builder
        kbuilder.add(new ClassPathResource("rules/SimpleRulesAndTask.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();

        //Checks for errors
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error.getMessage());

            }
            throw new IllegalStateException("Error building kbase!");
        }

        //Creates a new kbase and add all the packages from the builder
        KnowledgeBaseConfiguration kbaseConf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConf.setOption(EventProcessingOption.STREAM);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConf);
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        KnowledgeSessionConfiguration ksessionConf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        ksessionConf.setOption(ClockTypeOption.get("pseudo"));
        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(ksessionConf, null);
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", new WSHumanTaskHandler());
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession); 
     //   ksession.addEventListener(new DebugAgendaEventListener());
        //Force Init
        TaskClientHelper.getInstance();
        
        Thread.sleep(3000);
        
        Task task = createTask();

//        new Thread(new Runnable() {
//
//            public void run() {
//                ksession.fireUntilHalt();
//            }
//        }).start();
        
        
        // Insert Tasks
        ksession.insert(task);
        // Insert Average
        ksession.insert(new Average(0.0));
        
        //Create and Insert Threshold
        Threshold avgThreshold = new Threshold("AverageThreshold", 2, -1);
        ksession.insert(avgThreshold);
        
        //Insert User
        ksession.insert(new User("salaboy"));
        clock = ksession.getSessionClock();

        System.out.println(">>> Time "+clock.getCurrentTime());        
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(10.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        
        
        org.drools.runtime.rule.QueryResults results =
                ksession.getQueryResults("getAverage", new Object[]{});

        for (QueryResultsRow row : results) {
            System.out.println(">>> Current Average: "+((Average) row.get("$currentAverage")).getValue().toString());
            assertEquals("10.0", ((Average) row.get("$currentAverage")).getValue().toString());
        }
        
        Thread.sleep(3000);
        
        List<TaskSummary> tasks = TaskClientHelper.getInstance().getAssignedTasksByUser("salaboy");
        assertEquals(1, tasks.size());
        System.out.println(">>> Current Assigned Tasks: "+tasks.get(0).getName());
        
        System.out.println(">>> Time "+clock.getCurrentTime());        
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("water-events").insert(new WaterFlowingEvent(1.0));
        ksession.fireAllRules();
        clock.advanceTime(1000, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        
        results = ksession.getQueryResults("getAverage", new Object[]{});

        Thread.sleep(3000);
        
        for (QueryResultsRow row : results) {
            System.out.println(">>> Current Average: "+((Average) row.get("$currentAverage")).getValue().toString());
            assertEquals("1.0", ((Average) row.get("$currentAverage")).getValue().toString());
        }
        
        Thread.sleep(3000);
        
        tasks = TaskClientHelper.getInstance().getAssignedTasksByUser("salaboy");
        assertEquals(0, tasks.size());
        System.out.println(">>> I don't have any task assigned now!");
        
        Thread.sleep(3000);
    }

    private Task createTask() {
        Map<String, Object> vars = new HashMap();
        Map<String, User> users = new HashMap<String, User>();
        for (String user : taskServerDaemon.getDefaultUsers()) {
            users.put(user, new User(user));
        }

        vars.put("users", users);

        vars.put("now", new Date());

        String str = "(with (new Task()) { priority = 55, taskData = (with( new TaskData()) { workItemId = 1 } ), ";
        str += "peopleAssignments = (with ( new PeopleAssignments() ) { potentialOwners = [users['salaboy']], recipients = [users['Administrator']] }),";
        str += "descriptions = [ new I18NText( 'en-UK', 'This is my description')], ";
	str += "subjects = [ new I18NText( 'en-UK', 'This is my subject')], ";
        str += "names = [ new I18NText( 'en-UK', 'This is my task name')] })";
        

        Task task = (Task) TaskServerDaemon.eval(new StringReader(str), vars);
        return task;
    }
}
