package org.plugtree.training;

import org.plugtree.training.client.IRCClient;
import org.plugtree.training.server.IRCServer;

public class IRCMessageFiltering {

	public static void main(String[] args) throws Exception {
		Thread server = new Thread(new IRCServer(5445));
		server.start();
		Thread.sleep(500);
		IRCClient client = new IRCClient("user1");
		client.connect("127.0.0.1", 5445);
	}

}
