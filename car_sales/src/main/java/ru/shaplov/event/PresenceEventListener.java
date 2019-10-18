package ru.shaplov.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;
import ru.shaplov.models.EnumState;
import ru.shaplov.models.EventMessage;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shaplov
 * @since 18.10.2019
 */
@Component
public class PresenceEventListener {

    private static final Logger LOGGER = LogManager.getLogger(PresenceEventListener.class);

    private final SimpMessagingTemplate messagingTemplate;

    private final SimpUserRegistry simpUserRegistry;

    @Autowired
    public PresenceEventListener(SimpMessagingTemplate messagingTemplate, SimpUserRegistry simpUserRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
    }

//    @EventListener
//    public void handleSessionConnected(SessionConnectedEvent event) {
//        System.out.println("connected");
//    }

    @EventListener
    public void handleSessionSubscribe(SessionSubscribeEvent event) {
        String destination = SimpMessageHeaderAccessor.getDestination(event.getMessage().getHeaders());
        if (event.getUser() == null || event.getUser().getName() == null) {
            LOGGER.error("User is not present.");
        } else {
            Set<String> users = findSubscribedUsers(destination);
            String username = event.getUser().getName();
            if (!users.contains(username)) {
                LOGGER.info("User {} subscribed to {}", event.getUser().getName(), destination);
                if (destination != null && destination.endsWith("/users")) {
                    EventMessage message = new EventMessage();
                    message.setUsername(username);
                    users.add(username);
                    message.setUsers(users);
                    message.setState(EnumState.JOIN);
                    messagingTemplate.convertAndSend(destination, message);
                }
            } else {
                if (destination != null && destination.endsWith("/users")) {
                    LOGGER.info("User {} update list for destination {}", event.getUser().getName(), destination);
                    EventMessage message = new EventMessage();
                    message.setUsers(users);
                    message.setState(EnumState.UPDATE);
                    messagingTemplate.convertAndSend(destination, message);
                }
            }
        }
    }

//    @EventListener
//    public void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
//        String destination = SimpMessageHeaderAccessor.getDestination(event.getMessage().getHeaders());
//        if (event.getUser() == null || event.getUser().getName() == null) {
//            LOGGER.error("User is not present.");
//        } else {
//            LOGGER.info("User {} disconnected from {}", event.getUser().getName(), destination);
//            if (destination != null && destination.endsWith("/users")) {
//                EventMessage message = new EventMessage();
//                message.setUsername(event.getUser().getName());
//                message.setUsers(findSubscribedUsers(destination));
//                message.setState(EnumState.DISCONNECT);
//                messagingTemplate.convertAndSend(destination, message);
//            }
//        }
//    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        if (event.getUser() == null || event.getUser().getName() == null) {
            LOGGER.error("User is not present.");
        } else {
            LOGGER.info("User {} disconnected", event.getUser().getName());
            simpUserRegistry.getUser(event.getUser().getName())
                    .getSessions().stream().flatMap(v -> v.getSubscriptions().stream())
                    .map(SimpSubscription::getDestination).filter(v -> v.endsWith("/users"))
                    .collect(Collectors.collectingAndThen(
                            Collectors.groupingBy(Function.identity(), Collectors.counting()),
                            map -> {
                                map.values().removeIf(cnt -> cnt > 1);
                                return map.keySet();
                            }
                    ))
                    .forEach(
                            (dest) -> {
                                EventMessage message = new EventMessage();
                                message.setUsername(event.getUser().getName());
                                Set<String> users = findSubscribedUsers(dest);
                                users.remove(event.getUser().getName());
                                message.setUsers(users);
                                message.setState(EnumState.DISCONNECT);
                                messagingTemplate.convertAndSend(dest, message);
                            }
                    );
        }
    }

    private Set<String> findSubscribedUsers(String destination) {
        return simpUserRegistry.findSubscriptions(
                s -> destination.equals(s.getDestination())
        ).stream().map(s -> s.getSession().getUser().getName())
                .collect(Collectors.toSet());
    }
}
