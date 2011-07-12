package org.plugtree.training.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.integration.transports.netty.NettyAcceptorFactory;
import org.hornetq.integration.transports.netty.NettyConnectorFactory;
import org.hornetq.integration.transports.netty.TransportConstants;
import org.plugtree.training.MessageProcessor;
import org.plugtree.training.exception.IRCException;
import org.plugtree.training.model.Message;

public class IRCServer extends MessageProcessor implements Runnable {

	public static final String MESSAGES_QUEUE = "irc-messages";

	private HornetQServer server;
	private Configuration configuration;
	private final IRCServerHandler messageHandler;
	private final IRCServerMessageFilter messageFilter;
	private ClientSession session;
	private ClientConsumer consumer;

	private final Integer port;

	public IRCServer(int port) {
		this.port = port;
		this.messageHandler = new IRCServerHandler();
		this.messageFilter = new IRCServerMessageFilter(messageHandler);
		this.messageFilter.initialize();
	}

	public void run() {
		try {
			start();
		} catch (IRCException e) {
			throw new RuntimeException("Error starting IRC server.", e);
		}
		ClientMessage clientMessage;
		while (true) {
			try {
				clientMessage = consumer.receive();
				Message message = (Message) readMessage(clientMessage);
				messageFilter.filter(message);
			} catch (HornetQException e) {
				System.out.println("Error receiving/sending message " + e.getMessage());
			} catch (IRCException e) {
				System.out.println("Error reading message: " + e.getMessage());
			}
		}
	}

	private void start() throws IRCException {

		Map<String, Object> connectionParams = new HashMap<String, Object>();
		connectionParams.put(TransportConstants.PORT_PROP_NAME, port);

		if (configuration==null) {
			configuration = new ConfigurationImpl();
			configuration.setPersistenceEnabled(false);
			configuration.setSecurityEnabled(false);
			configuration.setClustered(false);
		}

		TransportConfiguration transpConf = new TransportConfiguration(NettyAcceptorFactory.class.getName(), connectionParams);

		HashSet<TransportConfiguration> setTransp = new HashSet<TransportConfiguration>();
		setTransp.add(transpConf);

		configuration.setAcceptorConfigurations(setTransp);

		server = HornetQServers.newHornetQServer(configuration);
		try {
			server.start();
		} catch (Exception e) {
			throw new IRCException("Error starting internal HornetQ Server", e);
		}

		TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getCanonicalName(), connectionParams);
		ClientSessionFactory factory = HornetQClient.createClientSessionFactory(transportConfiguration);
		try {
			session = factory.createSession();
			messageHandler.setSession(session);
			session.createQueue(MESSAGES_QUEUE, MESSAGES_QUEUE, true);
			consumer = session.createConsumer(MESSAGES_QUEUE);
			session.start();
		} catch (HornetQException e) {
			throw new IRCException("Error creating internal HornetQ message queues", e);
		}
	}

}
