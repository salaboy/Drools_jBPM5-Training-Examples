package org.plugtree.training.server;

import java.util.HashMap;
import java.util.Map;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.plugtree.training.MessageProcessor;
import org.plugtree.training.exception.IRCException;
import org.plugtree.training.model.Message;

public class IRCServerHandler extends MessageProcessor {

	private Map<String, ClientProducer> producers;
	private ClientSession session;

	public IRCServerHandler() {
		this.producers = new HashMap<String, ClientProducer>();
	}

	public void notify(String destination, Message message) throws HornetQException, IRCException {
		ClientProducer producer = producers.get(destination);
		if (producer==null) {
			producer = getSession().createProducer(destination);
			producers.put(destination, producer);
		}
		// send message to destination
		sendMessage(session, producer, message);
	}

	public void setSession(ClientSession session) {
		this.session = session;
	}

	public ClientSession getSession() {
		return session;
	}

}