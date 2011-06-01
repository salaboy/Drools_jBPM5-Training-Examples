/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

import java.util.List;
import org.drools.SystemEventListenerFactory;
import org.jbpm.process.workitem.wsht.BlockingAddTaskResponseHandler;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.hornetq.HornetQTaskClientConnector;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;

/**
 *
 * @author salaboy
 */
public class TaskClientHelper {

    private static TaskClientHelper helper;
    private TaskClient taskClient;

    public static TaskClientHelper getInstance(){
        if(helper == null){
            helper = new TaskClientHelper();
        }
        return helper;
    }

    private TaskClientHelper() {
        init();
    }
    
    
    
    private void init() {
        taskClient = new TaskClient(new HornetQTaskClientConnector("client 1",
                new HornetQTaskClientHandler(SystemEventListenerFactory.getSystemEventListener())));
        boolean connected = taskClient.connect("127.0.0.1", 5443);

        System.out.println("Connected ? =>" + connected);
    }
    
    public Task addTask(Task task, ContentData data){
        BlockingAddTaskResponseHandler addTaskResponseHandler = new BlockingAddTaskResponseHandler();
        taskClient.addTask(task, data, addTaskResponseHandler);
        long taskId = addTaskResponseHandler.getTaskId();
        task.setId(taskId);
        return task;
    }
    
    public Task start(Task task, User user){
        BlockingTaskOperationResponseHandler startResponseHandler = new BlockingTaskOperationResponseHandler();
        taskClient.start(task.getId(), user.getId(), startResponseHandler);
        return task;
        
    }
    
    public Task complete(Task task, User user, ContentData data){
        BlockingTaskOperationResponseHandler completeResponseHandler = new BlockingTaskOperationResponseHandler();
        taskClient.complete(task.getId(), user.getId(), data, completeResponseHandler);
        return task;
    }
    
    public Task remove(Task task, User user ){
        BlockingTaskOperationResponseHandler removeResponseHandler = new BlockingTaskOperationResponseHandler();
        taskClient.remove(task.getId(), "Administrator", removeResponseHandler); 
        return task;
    }
    
    public Task forward(Task task, User user){
         BlockingTaskOperationResponseHandler forwardResponseHandler = new BlockingTaskOperationResponseHandler();
        taskClient.forward(task.getId(), user.getId(), "Administrator" , forwardResponseHandler);
        return task;
    }

    List<TaskSummary> getAssignedTasksByUser(String user) {
        BlockingTaskSummaryResponseHandler handler = new BlockingTaskSummaryResponseHandler();
        taskClient.getTasksAssignedAsPotentialOwner(user, "en-UK", handler);
        return handler.getResults();
    }
    
    
    
    
}
