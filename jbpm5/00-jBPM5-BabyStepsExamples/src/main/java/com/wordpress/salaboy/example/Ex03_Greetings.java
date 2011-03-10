package com.wordpress.salaboy.example;

import java.util.HashMap;
import java.util.Random;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.wordpress.salaboy.example.model.Hour;

public class Ex03_Greetings {
	public static final void main(String[] args) {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("ex03_greetings.bpmn"), ResourceType.BPMN2);
			kbuilder.add(ResourceFactory.newClassPathResource("ex03_greetings.drl"), ResourceType.DRL);
			KnowledgeBase kbase = kbuilder.newKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("hourOfDay", new Hour(new Random().nextInt(24)));
			ksession.startProcess("com.wordpress.salaboy.bpmn2.greetings",params);
			ksession.fireAllRules();
			//logger.close();
			ksession.dispose();
	}
}