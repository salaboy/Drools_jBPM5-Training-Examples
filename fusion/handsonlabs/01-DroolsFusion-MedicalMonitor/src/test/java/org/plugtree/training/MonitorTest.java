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
import org.plugtree.training.model.Doctor;
import org.plugtree.training.model.MonitorEvent;
import org.plugtree.training.model.Nurse;
import org.plugtree.training.model.Patient;
import static org.junit.Assert.*;

/*
 * This example demonstrates the use of event processing for
 * personalized diagnosis.
 */

public class MonitorTest{

    private final List<String> firedRules = new ArrayList<String>();
    private SessionPseudoClock clock;

    @Test
    public void testNurseNotification() throws Exception{
        final StatefulKnowledgeSession ksession = this.createKSession();
        WorkingMemoryEntryPoint bedEventsEP = ksession.getWorkingMemoryEntryPoint("bed-events");

        Patient patient = new Patient("John","Doe");

        Nurse nurse = new Nurse("Mary","Douglas");
        ksession.setGlobal("nurse", nurse);
        Doctor doctor = new Doctor("Gregory","House");
        ksession.setGlobal("doctor", doctor);

        assertTrue(nurse.getNotifications().isEmpty());

        //First event
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_TEMPERATURE));
        ksession.fireAllRules();

        assertTrue(nurse.getNotifications().isEmpty());

        //Second event: 5 minutes after
        clock.advanceTime(5, TimeUnit.MINUTES);
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_TEMPERATURE));
        ksession.fireAllRules();
        
        assertEquals(1,nurse.getNotifications().size());
        nurse.clearNotifications();

        //Third event: 15 minutes after the last one
        clock.advanceTime(15, TimeUnit.MINUTES);
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_TEMPERATURE));
        ksession.fireAllRules();

        assertTrue(nurse.getNotifications().isEmpty());

        //Fourth event: 7 minutes after the last one
        clock.advanceTime(7, TimeUnit.MINUTES);
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_TEMPERATURE));
        ksession.fireAllRules();
        
        assertEquals(1,nurse.getNotifications().size());
        nurse.clearNotifications();

    }

    @Test
    public void testDoctorNotification() throws Exception{
        final StatefulKnowledgeSession ksession = this.createKSession();
        WorkingMemoryEntryPoint bedEventsEP = ksession.getWorkingMemoryEntryPoint("bed-events");

        Patient patient = new Patient("John","Doe");

        Nurse nurse = new Nurse("Mary","Douglas");
        ksession.setGlobal("nurse", nurse);
        Doctor doctor = new Doctor("Gregory","House");
        ksession.setGlobal("doctor", doctor);

        assertTrue(doctor.getNotifications().isEmpty());

        //First event
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_TEMPERATURE));
        ksession.fireAllRules();

        assertTrue(doctor.getNotifications().isEmpty());

        //Second event: 5 minutes after
        clock.advanceTime(5, TimeUnit.MINUTES);
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_BLOOD_PRESSURE));
        ksession.fireAllRules();

        assertEquals(1,doctor.getNotifications().size());
        doctor.clearNotifications();

        //Third event: 15 minutes after the last one
        clock.advanceTime(15, TimeUnit.MINUTES);
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_TEMPERATURE));
        ksession.fireAllRules();

        assertTrue(doctor.getNotifications().isEmpty());

        //Fourth event: 7 minutes after the last one
        clock.advanceTime(7, TimeUnit.MINUTES);
        bedEventsEP.insert(new MonitorEvent(patient, MonitorEvent.Symptom.HIGH_BLOOD_PRESSURE));
        ksession.fireAllRules();

        assertEquals(1,doctor.getNotifications().size());
        doctor.clearNotifications();

    }

    /**
     * Compiles resources and creates a new Ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSession(){

        //Rules compilation
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/rules.drl"), ResourceType.DRL);

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
