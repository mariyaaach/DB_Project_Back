package ru.itpark.mashacursah.service.redis.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class RefreshToken implements Serializable {
    private String token;
    private String username;
    private Instant expiryDate;
}