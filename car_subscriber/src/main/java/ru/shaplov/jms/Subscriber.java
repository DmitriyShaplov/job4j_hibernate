package ru.shaplov.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author shaplov
 * @since 16.10.2019
 */
@Service
public class Subscriber {

    private static final Logger LOGGER = LogManager.getLogger(Subscriber.class);

    @JmsListener(destination = "${car.topic}",
    containerFactory = "jmsListenerContainerFactory")
    public void receive(String message) {
        LOGGER.info("received message='{}'", message);
    }
}
