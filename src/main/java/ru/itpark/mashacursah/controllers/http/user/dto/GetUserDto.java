package ru.itpark.mashacursah.controllers.http.user.dto;

import ru.itpark.mashacursah.entity.Role;
import ru.itpark.mashacursah.entity.user.User;

/**
 * DTO for {@link User}
 */
public record GetUserDto(Integer userId, String username, String fullName, String email, String role) {
}