package ru.itpark.mashacursah.controllers.http.user.dto;

import ru.itpark.mashacursah.entity.Role;
import ru.itpark.mashacursah.entity.user.User;

/**
 * DTO for {@link User}
 */
public record UpdateUserDto(Integer userId, String username, String fullName, String email, Role role) {
}