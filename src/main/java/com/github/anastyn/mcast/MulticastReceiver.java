package com.github.anastyn.mcast;

import org.springframework.boot.ApplicationArguments;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Receives MULTICAST messages.
 */
public class MulticastReceiver extends MulticastParticipant {

    /**
     * C'tor.
     * @param args application arguments.
     */
    public MulticastReceiver(ApplicationArguments args) {
        super(args);
    }

    /**
     * @see MulticastParticipant#join()
     */
    @Override
    protected MulticastSocket join() throws IOException {
        MulticastSocket socket = new MulticastSocket(getPort());
        if (getNetworkInterface() != null) {
            socket.setInterface(getNetworkInterface());
        }
        socket.joinGroup(getGroup());
        getLogger().info("Listening on [{} : {}]", socket.getLocalAddress(), socket.getLocalPort());
        return socket;
    }

    /**
     * @see MulticastParticipant#loop()
     */
    @Override
    protected void loop() throws IOException {
        byte[] buffer = new byte[8192];
        DatagramPacket messagePacket = new DatagramPacket(buffer, buffer.length);
        getSocket().receive(messagePacket);
        String message = new String(messagePacket.getData(), 0, messagePacket.getLength(), getMessageEncoding());
        getLogger().info("[{} : {}] - {}", messagePacket.getAddress().getHostAddress(), messagePacket.getPort(), message);
    }

}
