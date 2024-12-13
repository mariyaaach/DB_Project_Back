package ru.itpark.mashacursah.controllers.http.auth.dto;

import ru.itpark.mashacursah.entity.Role;

public record SignUpDto(
        String username,
        String password,
        String fullName,
        String email,
        Role role
) {
}
