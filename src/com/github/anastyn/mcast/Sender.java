package com.github.anastyn.mcast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Sender extends MulticastParticipant {
	
	private final byte messageTTL;
	private final String message;
	
	public Sender(InetAddress networkInterface, InetAddress targetGroup, int targetPort, byte messageTTL, String message, String messageEnconding) {
		super(networkInterface, targetGroup, targetPort, messageEnconding);
		this.message = message;
		this.messageTTL = messageTTL;
	}
	
	@Override
	public void run() {
		try {
			// open socket on random unused port and send packets to the
			// specific port
			socket = new MulticastSocket();
			socket.setTimeToLive(messageTTL);
			if (networkInterface != null) {
				socket.setInterface(networkInterface);
			}
			socket.joinGroup(group);
		
			System.out.println("Sender [" + socket.getLocalAddress() + ":" + socket.getLocalPort() + ", ttl=" + socket.getTimeToLive() + "]");
			
			if (message != null) {
				post(message);
			} else {
				final BufferedReader inputReader = new BufferedReader(
						new InputStreamReader(System.in, messageEnconding));
				System.out.println("Waiting for user input...");
				while (!stopped) {
					try {
						post(inputReader.readLine());
					} catch (IOException e) {
						System.err.println(e);
					}
				}			
			}
		} catch (IOException e) {
			System.err.println(e);
		}
		
	}

	/**
	 * Post {@code message} to a target group
	 */
	public void post(final String message) throws IOException {
		// skip empty messages
		if (message != null && message.trim().length() > 0) {
			byte[] inputData = message.getBytes(messageEnconding);
			DatagramPacket packet = 
					new DatagramPacket(inputData, inputData.length, group, port);
			socket.send(packet);
		}
		
	}
	
}
