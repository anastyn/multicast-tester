package com.github.anastyn.mcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public abstract class MulticastParticipant implements Runnable {
	
	protected final InetAddress group;
	protected final int port;
	protected final InetAddress networkInterface;
	protected final String messageEnconding;
	
	protected MulticastSocket socket;
	protected volatile boolean stopped;
	
	public MulticastParticipant(InetAddress networkInterface, InetAddress group, int port, String messageEnconding) {
		this.networkInterface = networkInterface;
		this.group = group;
		this.port = port;
		this.messageEnconding = messageEnconding;
	}
	
	public void shutdown() {
		stopped = true;
		
		if (socket != null) {
			try {
				// no exception occurs if you leave a multicast group you never joined
				socket.leaveGroup(group);
				socket.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
	
}
