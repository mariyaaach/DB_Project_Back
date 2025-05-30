package ru.itpark.mashacursah.infrastructure.configuration.redis.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.infrastructure.configuration.redis.publisher.dto.NotificationEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publishEvent(String channel, NotificationEvent event) {
        try {
//            String message = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(channel, event);
            log.info("Published event to channel {}: {}", channel, event);
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
