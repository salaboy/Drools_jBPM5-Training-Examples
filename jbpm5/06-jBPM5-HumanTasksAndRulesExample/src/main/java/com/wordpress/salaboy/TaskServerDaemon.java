package com.wordpress.salaboy;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.SystemEventListenerFactory;
import org.jbpm.task.User;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.mina.MinaTaskServer;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.compiler.ExpressionCompiler;


public class TaskServerDaemon {
    
    private boolean running;
    private TaskServer taskServer;
    
    public TaskServerDaemon() {
        this.running = false;
    }
    
    public void startServer() {
        if(isRunning())
            throw new IllegalStateException("Server is already started");
        this.running = true;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.drools.task");
        TaskService taskService = new TaskService(entityManagerFactory, SystemEventListenerFactory.getSystemEventListener());
        TaskServiceSession taskSession = taskService.createSession() ;
        MockUserInfo userInfo = new MockUserInfo();
        taskService.setUserinfo( userInfo);
        
        for (String userName : getDefaultUsers()) {
            taskSession.addUser(new User(userName));
        }
        
        taskServer = new MinaTaskServer(taskService);
        Thread thread = new Thread(taskServer);
        thread.start();
    }

    public void stopServer() throws Exception {
        if(!isRunning())
            return;
        taskServer.stop();
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public String[] getDefaultUsers() {
        return new String[]{"salaboy", "translator", "reviewer", "Administrator"};
    }
    
     public static Object eval(Reader reader,
            Map vars) {
        try {
            return eval(toString(reader),
                    vars);
        } catch (IOException e) {
            throw new RuntimeException("Exception Thrown",
                    e);
        }
    }

    public static String toString(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder(1024);
        int charValue;

        while ((charValue = reader.read()) != -1) {
            sb.append((char) charValue);
        }
        return sb.toString();
    }

    public static Object eval(String str, Map vars) {
        ExpressionCompiler compiler = new ExpressionCompiler(str.trim());

        ParserContext context = new ParserContext();
        context.addPackageImport("org.jbpm.task");
        context.addPackageImport("org.jbpm.task.service");
        context.addPackageImport("org.jbpm.task.query");
        context.addPackageImport("java.util");

        vars.put("now", new Date());
        return MVEL.executeExpression(compiler.compile(context), vars);
    }
    
}
