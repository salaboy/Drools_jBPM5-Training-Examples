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
import org.plugtree.training.business.AlertSystem;
import org.plugtree.training.business.LuggageManager;
import org.plugtree.training.event.CheckInEvent;
import org.plugtree.training.event.LuggageReceptionEvent;
import org.plugtree.training.model.Alert.AlertType;
import org.plugtree.training.model.Luggage;

import static org.junit.Assert.*;

/*
 * This example demonstrates the use of event processing for detecting and
 * eliminating exceptions within a processing system.
 */

public class CheckInTest{

    private final List<String> firedRules = new ArrayList<String>();
    private SessionPseudoClock clock;

    @Test
    public void testLuggageNotCheckedInOnTime() throws Exception{
        final StatefulKnowledgeSession ksession = this.createKSession();
        WorkingMemoryEntryPoint checkInEventsEP = ksession.getWorkingMemoryEntryPoint("check-in-sensors");


        //Init the LuggageManager
        LuggageManager luggageManager = new LuggageManager();
        ksession.setGlobal("luggageManager", luggageManager);

        //Init the AlertSystem
        AlertSystem alertSystem = new AlertSystem();
        ksession.setGlobal("alertSystem", alertSystem);

        //flight code
        String flightCode = "AR 1435";

        //Luggage registration
        Luggage luggage = luggageManager.registerNewLuggage();
        LuggageReceptionEvent luggageReceptionEvent = new LuggageReceptionEvent(luggage,flightCode);
        ksession.insert(luggage);
        ksession.insert(luggageReceptionEvent);

        clock.advanceTime(3, TimeUnit.MINUTES);
        ksession.fireAllRules();

        //no alert should be fired 3 minutes after the luggage was received
        assertEquals(0, alertSystem.getAlertCount());
        //the luggage's location should be FRONT-DESK
        assertEquals(Luggage.Location.FRONT_DESK, luggage.getLocation());

        clock.advanceTime(3, TimeUnit.MINUTES);
        ksession.fireAllRules();

        //3 minutes later, the LUGGAGE_NOT_CHECKED_IN_ON_TIME alert should be fired
        assertEquals(1, alertSystem.getAlertCount());
        assertEquals(1, alertSystem.getAlertCount(AlertType.LUGGAGE_NOT_CHECKED_IN_ON_TIME));
        //the luggage's location should be FRONT-DESK
        assertEquals(Luggage.Location.FRONT_DESK, luggage.getLocation());



        ///////////////////////////////////////////

        alertSystem.clearAllAlerts();

        //A new Luggage is dispatched
        luggage = luggageManager.registerNewLuggage();
        luggageReceptionEvent = new LuggageReceptionEvent(luggage,flightCode);
        ksession.insert(luggage);
        ksession.insert(luggageReceptionEvent);

        //the luggage's location should be FRONT-DESK
        assertEquals(Luggage.Location.FRONT_DESK, luggage.getLocation());

        //2 minutes later, the luggage is checked-in
        clock.advanceTime(2, TimeUnit.MINUTES);
        CheckInEvent checkInEvent = new CheckInEvent(luggage.getCode());
        checkInEventsEP.insert(checkInEvent);
        ksession.fireAllRules();

        //No alerts
        assertEquals(0, alertSystem.getAlertCount());
        //the luggage's location should be CAROUSEL
        assertEquals(Luggage.Location.CAROUSEL, luggage.getLocation());

        //10 minutes later no alert should be fired because the luggage was
        //checked-in
        clock.advanceTime(10, TimeUnit.MINUTES);
        ksession.fireAllRules();
        assertEquals(0, alertSystem.getAlertCount());

        ksession.dispose();
    }

    @Test
    public void testNonExistentLuggage() throws Exception{
        final StatefulKnowledgeSession ksession = this.createKSession();
        WorkingMemoryEntryPoint checkInEventsEP = ksession.getWorkingMemoryEntryPoint("check-in-sensors");


        //Init the LuggageManager
        LuggageManager luggageManager = new LuggageManager();
        ksession.setGlobal("luggageManager", luggageManager);

        //Init the AlertSystem
        AlertSystem alertSystem = new AlertSystem();
        ksession.setGlobal("alertSystem", alertSystem);

        //flight code
        String flightCode = "AR 1435";

        //Luggage registration
        Luggage luggage = luggageManager.registerNewLuggage();
        LuggageReceptionEvent luggageReceptionEvent = new LuggageReceptionEvent(luggage,flightCode);
        ksession.insert(luggageReceptionEvent);

        //A non existant luggage is trying to be checked-in
        CheckInEvent checkInEvent = new CheckInEvent("NonExistentLuggageCode");
        checkInEventsEP.insert(checkInEvent);

        ksession.fireAllRules();

        assertEquals(1, alertSystem.getAlertCount());
        assertEquals(1, alertSystem.getAlertCount(AlertType.NON_EXISTENT_LUGGAGE));

        ksession.dispose();
    }

    /**
     * Compiles resources and creates a new Ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSession(){

        //Rules compilation
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/checkInRules.drl"), ResourceType.DRL);

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
