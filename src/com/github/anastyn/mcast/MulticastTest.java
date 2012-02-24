package com.github.anastyn.mcast;

public class MulticastTest {
		
	public static void main(String[] args) {
		ConsoleParameters params = ConsoleParameters.parse(args);
		if (params == null || params.getMulticastGroup() == null || params.getPort() == 0) {
			System.out.println(ResourceManager.getUsage());
			System.exit(1);
		}
		if (params.isPrintVersion()) {
			System.out.println(ResourceManager.getVersion());
		}
		
		MulticastParticipant participant;
		if (params.isServer()) {
			participant = new Receiver(params.getNetworkInterface(), params.getMulticastGroup(), params.getPort(), ResourceManager.getEncoding());
		} else {
			participant = new Sender(params.getNetworkInterface(), params.getMulticastGroup(), params.getPort(),
					params.getMessageTTL(), params.getMessage(), ResourceManager.getEncoding());
		}
		
		// in case the execution is interrupted (by Ctrl-Break, etc.)
		Runtime.getRuntime().addShutdownHook(new ResourceCleaner(participant));

		new Thread(participant).start();
		
	}
	
}
