package org.plugtree.training.server;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hornetq.api.core.HornetQException;
import org.plugtree.training.exception.IRCException;
import org.plugtree.training.model.Message;

public class IRCServerMessageFilter {

	private StatefulKnowledgeSession ksession;
	private final IRCServerHandler handler;

	public IRCServerMessageFilter(IRCServerHandler handler) {
		this.handler = handler;
	}

	public void initialize() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rules/filtering.drl", this.getClass()), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
			if (kbuilder.getErrors().size() > 0) {
				for (KnowledgeBuilderError kerror : kbuilder.getErrors()) {
					System.err.println(kerror);
				}
				throw new RuntimeException("Rules with errors");
			}
		}
		KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		ksession = kbase.newStatefulKnowledgeSession();
		ksession.setGlobal("handler", handler);
	}

	public void filter(Message message) throws HornetQException, IRCException {
		ksession.getWorkingMemoryEntryPoint("messages").insert(message);
		ksession.fireAllRules();
	}

}
