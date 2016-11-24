package com.github.anastyn.mcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MulticastParticipantApplication implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MulticastParticipantApplication.class);

    /** Provides a participant depending on the user's input. */
    private final MulticastParticipantFactory participantFactory;

    /**
     * C'tor.
     */
    @Autowired
    public MulticastParticipantApplication(MulticastParticipantFactory participantFactory) {
        this.participantFactory = participantFactory;
    }

    /**
     * Parse the command line arguments and start the application thread.
     * @param args command line arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        try {
            MulticastParticipant participant = participantFactory.getParticipant(args);

            // Intercept Ctrl-Break
            Runtime.getRuntime().addShutdownHook(new ResourceCleaner(participant));
            // not a daemon thread
            new Thread(participant).start();

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(MulticastParticipantApplication.class);
        // turn off the standard Spring Boot welcome banner.
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
