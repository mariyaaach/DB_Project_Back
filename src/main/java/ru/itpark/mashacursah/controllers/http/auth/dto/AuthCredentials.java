package ru.itpark.mashacursah.controllers.http.auth.dto;

public record AuthCredentials(
        String username,
        String password
) {
}
