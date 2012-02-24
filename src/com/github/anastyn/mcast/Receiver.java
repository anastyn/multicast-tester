package com.github.anastyn.mcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver extends MulticastParticipant {
	
	public Receiver(InetAddress networkInterface, InetAddress group, int port, String messageEnconding) {
		super(networkInterface, group, port, messageEnconding);
	}
	
	public void run() {
		try {
			socket = new MulticastSocket(port);
			if (networkInterface != null) {
				socket.setInterface(networkInterface);
			}
			socket.joinGroup(group);
			System.out.println("Receiver [" + 
					socket.getLocalAddress() + ":" + socket.getLocalPort() + "]");
		
			byte[] buffer = new byte[8192];
			while (!stopped) {
				try {
					DatagramPacket messagePacket = new DatagramPacket(buffer, buffer.length);
					socket.receive(messagePacket);
					String message = new String(messagePacket.getData(), 0, messagePacket.getLength(), messageEnconding);
					System.out.println("[" + messagePacket.getAddress().getHostAddress() + ":" + messagePacket.getPort() + "] " + message);
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	
}
