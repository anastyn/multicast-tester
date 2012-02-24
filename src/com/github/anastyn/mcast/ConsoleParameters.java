package com.github.anastyn.mcast;

import java.net.InetAddress;

/**
 * Reads and holds the provided console arguments.
 */
public class ConsoleParameters {

	// Multicast group
	private InetAddress multicastGroup;
	private InetAddress networkInterface;
	// Binding port
	private int port;
	// Sender or receiver(server)
	private boolean server;
	
	private byte messageTTL = 1;

	private String message;
	private boolean printVersion;
	
	private ConsoleParameters() {
	}

	/**
	 * Parse the console arguments. Unknown arguments will be skipped.
	 */
	public static ConsoleParameters parse(final String[] args) {
		final ConsoleParameters params = new ConsoleParameters();
		try {
			for (int index = 0; index < args.length; index++) {
				String arg = args[index];
				if ("-g".equals(arg)) {
					params.multicastGroup = InetAddress.getByName(args[++index]);
				} else if ("-p".equals(arg)) {
					params.port = Integer.parseInt(args[++index]);
				} else if ("-server".equals(arg)) {
					params.server = true;
				} else if ("-i".equals(arg)) {
					params.networkInterface = InetAddress.getByName(args[++index]);
				} else if ("-ttl".equals(arg)) {
					params.messageTTL = Byte.parseByte(args[++index]);
				} else if ("-version".equals(arg)) {
					params.printVersion = true;
				} else {
					params.message = arg;
				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return params;
	}

	public InetAddress getMulticastGroup() {
		return multicastGroup;
	}
	public InetAddress getNetworkInterface() {
		return networkInterface;
	}
	public int getPort() {
		return port;
	}
	public boolean isServer() {
		return server;
	}
	public byte getMessageTTL() {
		return messageTTL;
	}
	public String getMessage() {
		return message;
	}
	public boolean isPrintVersion() {
		return printVersion;
	}
	
}
