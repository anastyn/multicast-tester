package com.github.anastyn.mcast;

public class ResourceCleaner extends Thread {

	private final MulticastParticipant resource;
	
	public ResourceCleaner(final MulticastParticipant resource) {
		this.resource = resource;
	}

	@Override
	public void run() {
		resource.shutdown();
	}
	
	
}
