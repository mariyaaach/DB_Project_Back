package ru.itpark.mashacursah.infrastructure.configuration.redis.publisher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationEvent {
    @JsonProperty("type")
    private String type;
    @JsonProperty("message")
    private String message;


}