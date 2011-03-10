package com.wordpress.salaboy.example;

import java.util.Random;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

import com.wordpress.salaboy.example.model.Hour;

public class Ex02_HelloRules {
	public static final void main(String[] args) {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("ex02_helloRules.drl"), ResourceType.DRL);
			KnowledgeBase kbase = kbuilder.newKnowledgeBase();
			StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
			Hour hour = new Hour(new Random().nextInt(24));
			ksession.execute(hour);
	}
}