package ru.itpark.mashacursah.infrastructure.configuration.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.itpark.mashacursah.infrastructure.configuration.redis.publisher.dto.NotificationEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSubscriber {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void onMessage(NotificationEvent event, String channel) {
        try {
            log.info("Received message from Redis channel {}: {}", channel, event);
            log.info("Deserialized event: {}", event);
            messagingTemplate.convertAndSend("/topic/notifications", event);
        } catch (Exception e) {
            log.error("Failed to process Redis message: {}", e.getMessage());
        }
    }
}
