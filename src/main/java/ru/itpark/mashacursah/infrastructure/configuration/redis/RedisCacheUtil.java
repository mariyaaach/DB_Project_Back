package ru.itpark.mashacursah.infrastructure.configuration.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCacheUtil {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void cacheList(String key, List<T> list, long ttl, TimeUnit unit) {
        try {
            String json = objectMapper.writeValueAsString(list);
            redisTemplate.opsForValue().set(key, json, ttl, unit);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации: {}", e.getMessage());
        }
    }

    public <T> List<T> getCachedList(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (JsonProcessingException e) {
                log.error("Ошибка десериализации: {}", e.getMessage());
            }
        }
        return null;
    }

    public void clearCache(String key) {
        redisTemplate.delete(key);
    }
}