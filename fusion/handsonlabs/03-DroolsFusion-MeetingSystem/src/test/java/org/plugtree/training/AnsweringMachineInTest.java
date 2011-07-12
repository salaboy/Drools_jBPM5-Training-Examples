package org.plugtree.training;

import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionPseudoClock;
import java.util.ArrayList;
import java.util.List;
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
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.junit.Test;
import org.plugtree.training.event.IncomingCallEvent;
import org.plugtree.training.event.MeetingStartEvent;
import org.plugtree.training.model.Employee;
import org.plugtree.training.model.Meeting;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AnsweringMachineInTest{

    private final List<String> firedRules = new ArrayList<String>();
    private SessionPseudoClock clock;

    @Test
    public void testLuggageNotCheckedInOnTime() throws Exception{
        final StatefulKnowledgeSession ksession = this.createKSession();
        WorkingMemoryEntryPoint meetingsEP = ksession.getWorkingMemoryEntryPoint("meetings");
        WorkingMemoryEntryPoint incomingCallsEP = ksession.getWorkingMemoryEntryPoint("incoming-calls");

        Employee john = new Employee("John", "Terry");
        Employee paul = new Employee("Paul", "Stewart");
        Employee peter = new Employee("Peter", "Corning");
        Employee michael = new Employee("Michael", "Williams");

        //a 2 hours meeting is created
        Meeting meeting = new Meeting("Some important meeting",2*60*60*1000);
        meeting.addParticipant(john);
        meeting.addParticipant(paul);
        meeting.addParticipant(peter);

        //the meeting is started
        MeetingStartEvent meetingEvent = new MeetingStartEvent(meeting);
        meetingsEP.insert(meetingEvent);

        //30 minutes later, a call to Paul arrives.
        clock.advanceTime(30, TimeUnit.MINUTES);
        IncomingCallEvent callEvent = new IncomingCallEvent(paul);
        incomingCallsEP.insert(callEvent);
        assertEquals(IncomingCallEvent.CallStatus.PENDING, callEvent.getStatus());

        ksession.fireAllRules();

        //because Paul is in a meeting the call is redirected to an answering machine
        assertEquals(IncomingCallEvent.CallStatus.REDIRECTED, callEvent.getStatus());


        //at the same time, an incoming call to Michael arrives
        callEvent = new IncomingCallEvent(michael);
        incomingCallsEP.insert(callEvent);
        
        assertEquals(IncomingCallEvent.CallStatus.PENDING, callEvent.getStatus());
        clock.advanceTime(1, TimeUnit.MINUTES);
        
        System.out.println("FIRE FIRE");
        ksession.fireAllRules();

        //because Michael is not in a meeting the call is dispatched
        assertEquals(IncomingCallEvent.CallStatus.DISPATCHED, callEvent.getStatus());

        ksession.dispose();
    }

    
    /**
     * Compiles resources and creates a new Ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSession(){

        //Rules compilation
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/answeringMachineRules.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        //KBase creation
        KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConfig.setOption(EventProcessingOption.STREAM);

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConfig);
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        //KSession creation
        KnowledgeSessionConfiguration ksessionConfig = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        ksessionConfig.setOption(ClockTypeOption.get("pseudo"));

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(ksessionConfig, null);
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        //We add an AgendaEventListener to keep track of fired rules.
        ksession.addEventListener(new DefaultAgendaEventListener(){
            @Override
            public void afterActivationFired(AfterActivationFiredEvent event) {
                firedRules.add(event.getActivation().getRule().getName());
            }
        });

        clock = ksession.getSessionClock();

        return ksession;
    }
}
