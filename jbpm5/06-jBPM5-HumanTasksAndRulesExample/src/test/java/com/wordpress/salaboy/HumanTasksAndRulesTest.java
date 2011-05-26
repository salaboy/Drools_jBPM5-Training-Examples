/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.workitem.wsht.BlockingAddTaskResponseHandler;
import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.mina.MinaTaskClientConnector;
import org.jbpm.task.service.mina.MinaTaskClientHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author salaboy
 */
public class HumanTasksAndRulesTest {

    private TaskClient taskClient;
    private TaskServerDaemon taskServerDaemon = new TaskServerDaemon();

    public HumanTasksAndRulesTest() {
    }

    @Before
    public void setUp() throws InterruptedException {
        taskServerDaemon.startServer();
        
        Thread.sleep(5000);
        
        taskClient = new TaskClient(new MinaTaskClientConnector("client 1",
                new MinaTaskClientHandler(SystemEventListenerFactory.getSystemEventListener())));
        boolean connected = taskClient.connect("127.0.0.1", 9123);
        
        System.out.println("Connected ? =>"+connected);


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
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", new WSHumanTaskHandler());
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        ksession.setGlobal("taskClient", taskClient);

        
        
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


        BlockingAddTaskResponseHandler addTaskResponseHandler = new BlockingAddTaskResponseHandler();
        Task task = (Task) TaskServerDaemon.eval(new StringReader(str), vars);
        //taskClient.addTask(task, null, addTaskResponseHandler);
        ksession.insert(task);
        
        
        ksession.fireAllRules();
        Thread.sleep(2000);
        ksession.insert(new User("salaboy"));
        
        ksession.fireAllRules();
        
        Thread.sleep(2000);

    }

   
}
