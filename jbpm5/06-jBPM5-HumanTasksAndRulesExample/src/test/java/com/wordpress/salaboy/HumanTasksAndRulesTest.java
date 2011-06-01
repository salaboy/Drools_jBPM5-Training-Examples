/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

import com.wordpress.salaboy.model.events.PulseEvent;
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
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.time.SessionPseudoClock;
import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
       // ksession.addEventListener(new DebugWorkingMemoryEventListener());
        ksession.addEventListener(new DebugAgendaEventListener());
        
        
        //Force Init
        TaskClientHelper.getInstance();
        
        Thread.sleep(3000);
        
        Task task = createTask();

        new Thread(new Runnable() {

            public void run() {
                ksession.fireUntilHalt();
            }
        }).start();
        
        ksession.insert(task);
        
        Threshold avgThreshold = new Threshold("AverageThreshold", 2, -2);
        ksession.insert(avgThreshold);

        ksession.insert(new User("salaboy"));
        clock = ksession.getSessionClock();

//        System.out.println(">>> Inserting the first set of events");
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        
//        System.out.println(">>> Nothing Until Here, because the AVG is 1");
        System.out.println(">>> Time "+clock.getCurrentTime());
        
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        clock.advanceTime(1500, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        clock.advanceTime(1500, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        System.out.println(">>> Time "+clock.getCurrentTime());
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(10));
        
        
        clock.advanceTime(1500, TimeUnit.MILLISECONDS);
        clock.advanceTime(1500, TimeUnit.MILLISECONDS);
        clock.advanceTime(1500, TimeUnit.MILLISECONDS);
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(9));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        System.out.println(">>> Time "+clock.getCurrentTime());
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(8));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(7));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(6));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        System.out.println(">>> Time "+clock.getCurrentTime());
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(2));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(2));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        System.out.println(">>> Time "+clock.getCurrentTime());
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        
//        
//         ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        System.out.println(">>> Time "+clock.getCurrentTime());
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        System.out.println(">>> Time "+clock.getCurrentTime());
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        System.out.println(">>> Time "+clock.getCurrentTime());
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
//        System.out.println(">>> Time "+clock.getCurrentTime());
//        ksession.getWorkingMemoryEntryPoint("pulse-events").insert(new PulseEvent(1));
//        clock.advanceTime(500, TimeUnit.MILLISECONDS);
        
        
        Thread.sleep(5000);
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
        str += "peopleAssignments = (with ( new PeopleAssignments() ) { potentialOwners = [users['salaboy']], }),";
        str += "names = [ new I18NText( 'en-UK', 'This is my task name')] })";


        Task task = (Task) TaskServerDaemon.eval(new StringReader(str), vars);
        return task;
    }
}
