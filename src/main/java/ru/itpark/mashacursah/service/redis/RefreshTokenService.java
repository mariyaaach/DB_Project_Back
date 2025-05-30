package ru.itpark.mashacursah.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.service.redis.dto.RefreshToken;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    public RefreshToken createRefreshToken(String username, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));

        // Сохраняем в Redis с TTL
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + token,
                refreshToken,
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public RefreshToken findByToken(String token) {
        Object raw = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + token);
        if (raw == null) return null;
        return objectMapper.convertValue(raw, RefreshToken.class);
    }

    public void deleteByToken(String token) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + token);
    }

    public boolean validateRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        if (refreshToken == null || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            deleteByToken(token); // Удаляем недействительный токен
            return false;
        }
        return true;
    }
}