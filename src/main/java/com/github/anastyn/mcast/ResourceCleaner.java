package com.github.anastyn.mcast;

/**
 * Clean up the resources.
 */
public class ResourceCleaner extends Thread {

	/** Multicast participant to shut down. */
	private final MulticastParticipant resource;

	/**
	 * C'tor.
	 * @param resource the participant to close
	 */
	public ResourceCleaner(final MulticastParticipant resource) {
		this.resource = resource;
	}

	/**
	 * Run the cleaner.
	 */
	@Override
	public void run() {
		resource.shutdown();
	}
	
	
}
