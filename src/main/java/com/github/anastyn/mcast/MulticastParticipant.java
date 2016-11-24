package com.github.anastyn.mcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Defines a multicast participant.
 */
public abstract class MulticastParticipant implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MulticastParticipant.class);

    /**
     * The default text encoding. There is no standard constant for URF-8 prior to Java 1.7.
     */
    private final static String DEFAULT_MESSAGE_ENCODING = "UTF-8";

    /**
     * MULTICAST address.
     * Range: 224.0.0.0 - 239.255.255.255
     *
     * Range 224.0.0.0 through 224.0.0.255 is reserved for local purposes (as administrative and maintenance tasks)
     * and datagrams destined to them are never forwarded by multicast routers.
     * Similarly, the range 239.0.0.0 to 239.255.255.255 has been reserved for "administrative scoping".
     */
    private InetAddress group;

    /** A random port by default */
    private Integer port = 0;

    /**
     * Useful on hosts with multiple network interfaces, where applications
     * want to use other than the system default.
     */
    private InetAddress networkInterface;

    /** The multicast socket */
    private MulticastSocket socket;

    /** A flag to exit an eternal loop. */
    private volatile boolean stopped;

    /**
     * C'tor.
     */
    public MulticastParticipant(ApplicationArguments args) {
        parseArguments(args);
    }

    /**
     * @see Runnable#run()
     */
    @Override
    public void run() {
        try {
            socket = join();
            while (!stopped) {
                try {
                    loop();
                } catch (IOException e) {
                    getLogger().error("Exception while receiving a packet", e);
                }
            }
        } catch (IOException e) {
            getLogger().error("Unexpected exception", e);
        }
    }

    /**
     * Parse command line arguments.
     * @param args command line arguments
     */
    private void parseArguments(ApplicationArguments args) {
        List<String> values = args.getOptionValues("group");
        if (values == null || values.size() != 1) {
            throw new IllegalArgumentException("Group is required.");
        } else {
            try {
                this.group = InetAddress.getByName(values.get(0));
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("Invalid group value.");
            }
        }

        values = args.getOptionValues("port");
        if (values != null && values.size() != 1) {
            throw new IllegalArgumentException("Port should be a single value.");
        } else if (values != null){
            try {
                this.port = Integer.parseInt(values.get(0));
                if (port < 1 || port > 65535) {
                    throw new IllegalArgumentException("Port should be between 1 and 65535.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Port should be a valid number.");
            }
        }

        values = args.getOptionValues("interface");
        if (values != null && values.size() != 1) {
            throw new IllegalArgumentException("There should be a single network interface.");
        } else if (values != null){
            try {
                this.networkInterface = InetAddress.getByName(values.get(0));
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException("Invalid network interface.");
            }
        }
    }

    /**
     * Fire when a participant joins a multicast group.
     * @return the created multicast socket
     * @throws IOException on error
     */
    protected abstract MulticastSocket join() throws IOException;

    /**
     * Fire in a loop to send/receive multicast messages.
     * @throws IOException on error
     */
    protected abstract void loop() throws IOException;

    /**
     * Stop the communication.
     */
    public void shutdown() {
        stopped = true;

        if (socket != null) {
            try {
                // no exception occurs if you leave a multicast group you never joined
                socket.leaveGroup(group);
                socket.close();
            } catch (IOException e) {
                getLogger().error("Error while closing a socket.", e);
            }
        }
    }

    public InetAddress getGroup() {
        return group;
    }

    public Integer getPort() {
        return port;
    }

    public InetAddress getNetworkInterface() {
        return networkInterface;
    }

    public String getMessageEncoding() {
        return DEFAULT_MESSAGE_ENCODING;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    protected static Logger getLogger() {
        return LOG;
    }

}
