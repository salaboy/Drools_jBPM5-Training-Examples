package org.plugtree.training;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.plugtree.training.exception.IRCException;
import org.plugtree.training.model.Message;

public abstract class MessageProcessor {

	protected Object readMessage(ClientMessage msgReceived) throws IRCException {
		int bodySize = msgReceived.getBodySize();
		byte[] message = new byte[bodySize];
		msgReceived.getBodyBuffer().readBytes(message);
		ByteArrayInputStream bais = new ByteArrayInputStream(message);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			throw new IRCException("Unable to read Message", e);
		} catch (ClassNotFoundException e) {
			throw new IRCException("Unable to create Message object", e);
		}
	}

	protected void sendMessage(ClientSession session, ClientProducer producer, Message content) throws IRCException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oout;
		try {
			oout = new ObjectOutputStream(baos);
			oout.writeObject(content);
			ClientMessage message = session.createMessage(true);
			message.getBodyBuffer().writeBytes(baos.toByteArray());
			message.putStringProperty("producerId", content.getUserName());
			producer.send(message);
		} catch (IOException e) {
			throw new IRCException("Error generating message", e);
		} catch (HornetQException e) {
			throw new IRCException("Error sending message to IRC Server", e);
		}
	}

}
