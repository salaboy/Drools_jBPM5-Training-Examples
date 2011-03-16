package com.wordpress.salaboy.example;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class Ex01_HelloBPM {
	public static final void main(String[] args) {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("ex01_helloBPM.bpmn"), ResourceType.BPMN2);
			KnowledgeBase kbase = kbuilder.newKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			ksession.startProcess("com.wordpress.salaboy.bpmn2.hello");
			ksession.dispose();
	}
}