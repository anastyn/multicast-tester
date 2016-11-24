package com.github.anastyn.mcast;

import org.springframework.boot.ApplicationArguments;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.List;
import java.util.Scanner;

/**
 * Sends MULTICAST messages.
 */
public class MulticastSender extends MulticastParticipant {

    /**
     * Controls the scope of the multicasts.
     * Should be between 0 and 255.
     * The default value is 1.
     */
    private int ttl = 1;

    /** Reads user's input. */
    private final Scanner inputReader;

    /**
     * C'tor.
     * @param args application arguments.
     */
    public MulticastSender(ApplicationArguments args) {
        super(args);
        parseTTL(args);
        inputReader = new Scanner(System.in);
    }

    /**
     * Extract an optional TTL argument.
     * @param args command line arguments
     */
    private void parseTTL(ApplicationArguments args) {
        List<String> values = args.getOptionValues("ttl");
        if (values != null && values.size() == 1) {
            try {
                this.ttl = Integer.parseInt(values.get(0));
                if (this.ttl < 0 || this.ttl > 255) {
                    throw new IllegalArgumentException("Time To Live value should be a number between 0 and 255.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Time To Live value should be a number.");
            }
        }
    }

    /**
     * @see MulticastParticipant#join()
     */
    @Override
    protected MulticastSocket join() throws IOException {
        MulticastSocket socket = new MulticastSocket(getPort());
        socket.setTimeToLive(getTtl());
        if (getNetworkInterface() != null) {
            socket.setInterface(getNetworkInterface());
        }
        socket.joinGroup(getGroup());

        getLogger().info("Sender [{} : {}, ttl={}]", socket.getLocalAddress(), socket.getLocalPort(), socket.getTimeToLive());
        getLogger().info("Please write a message.");

        return socket;
    }

    /**
     * @see MulticastParticipant#loop()
     */
    @Override
    protected void loop() throws IOException {
        // needed to not hang on nextLine and avoid "No line found" exception on Ctrl+Break
        while (!inputReader.hasNextLine()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                getLogger().error("Exception while waiting for the input", e);
                return;
            }
        }

        String message = inputReader.nextLine();
        // skip empty messages
        if (message != null && message.trim().length() > 0) {
            byte[] inputData = message.getBytes(getMessageEncoding());
            DatagramPacket packet = new DatagramPacket(inputData, inputData.length, getGroup(), getPort());
            getSocket().send(packet);
        }
    }

    public int getTtl() {
        return ttl;
    }

}
