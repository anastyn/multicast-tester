package com.github.anastyn.mcast;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Instantiates MULTICAST participants.
 */
@Component
public class MulticastParticipantFactory {

    /**
     * Returns the appropriate participant instance depending on the settings.
     * @param args the settings.
     * @return the participant instance
     */
    public MulticastParticipant getParticipant(ApplicationArguments args) {
        List<String> values = args.getOptionValues("sender");
        boolean isReceiver = values == null;
        return isReceiver ? new MulticastReceiver(args) : new MulticastSender(args);
    }

}
