package org.plugtree.training.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.integration.transports.netty.NettyConnectorFactory;
import org.hornetq.integration.transports.netty.TransportConstants;
import org.plugtree.training.MessageProcessor;
import org.plugtree.training.exception.IRCException;
import org.plugtree.training.model.Message;
import org.plugtree.training.server.IRCServer;

public class IRCClient extends MessageProcessor {

	private ClientSession session;
	private ClientProducer producer;
	private ClientConsumer consumer;

	private String userName;
	private String address;
	private Integer port;

	public IRCClient(String userName) {
		if (userName == null) {
			throw new IllegalArgumentException("Username can not be null");
		}
		this.userName = userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void connect(String address, Integer port) throws IRCException {
		this.port = port;
		this.address = address;
		this.connect();
		this.start();
	}

	private void connect() throws IRCException {
		if (session != null && !session.isClosed()) {
			throw new IllegalArgumentException("Already connected. Disconnect first.");
		} 
		Map<String, Object> connectionParams = new HashMap<String, Object>();
		if (address==null) {
			address = "127.0.0.1";
		}
		if (port==null) {
			port = 5445;
		}
		connectionParams.put(TransportConstants.PORT_PROP_NAME, port);
		connectionParams.put(TransportConstants.HOST_PROP_NAME, address);
		TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getCanonicalName(), connectionParams);
		ClientSessionFactory factory = HornetQClient.createClientSessionFactory(transportConfiguration);
		try {
			session = factory.createSession();
			producer = session.createProducer(IRCServer.MESSAGES_QUEUE);
			session.start();
		} catch (HornetQException e) {
			throw new IRCException("Error connecting to IRC Server", e);
		}

		createClientQueue();

		Thread serverEchoThread = new Thread(new Runnable() {
			public void run() {
				try {
					consumer = session.createConsumer(userName);
					while (true) {
						ClientMessage serverMessage = consumer.receive();
						if (serverMessage!=null) {
							Message message = (Message) readMessage(serverMessage);
							System.err.println("\nSERVER MESSAGE: " + message.getContent());
						}
					}
				}
				catch (HornetQException e) {
					if (e.getCode()!=HornetQException.OBJECT_CLOSED) {
						throw new RuntimeException("Client Exception with class " + getClass() + " using port " + port, e);
					}
				}
				catch (Exception e) {
					throw new RuntimeException("Client Exception with class " + getClass() + " using port " + port, e);
				}
			}
		});
		serverEchoThread.start();
	}

	private void createClientQueue() {
		try {
			session.createQueue(userName, userName, true);
		}
		catch (HornetQException e) {
			if (e.getCode()!=HornetQException.QUEUE_EXISTS) {
				throw new RuntimeException("Client Exception with class " + getClass() + " using port " + port, e);
			}
		}
	}

	private void start() {
		Thread inputMessagesThread = new Thread() {
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				while (true) {
					try {
						System.out.print(userName + ": ");
						String message = br.readLine();
						if (message!=null && message.trim().length() > 0) {
							sendMessage(session, producer, new Message(userName, message.trim()));
						}
					} catch (IOException e) {
						System.out.println("Error reading keyboard input. " + e.getMessage());
					} catch (IRCException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		};
		inputMessagesThread.start();
	}

}
