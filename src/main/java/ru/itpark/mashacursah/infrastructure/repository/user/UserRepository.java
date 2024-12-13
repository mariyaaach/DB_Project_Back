package ru.itpark.mashacursah.infrastructure.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.entity.user.User;
import ru.itpark.mashacursah.infrastructure.aspects.Log;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<User> findById(Long userId) {
        final String sql = """
                SELECT user_id, username, password_hash, full_name, email, role
                FROM users
                WHERE user_id = :userId
                """;

        try {
            User user = jdbcTemplate.queryForObject(sql, Map.of("userId", userId), new UserMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String username) {
        final String sql = """
                SELECT user_id, username, password_hash, full_name, email, roles.name as role_name
                FROM users
                join roles on users.role_id = roles.role_id
                WHERE username = :username
                """;

        try {
            User user = jdbcTemplate.queryForObject(sql, Map.of("username", username), new UserMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        final String sql = """
                SELECT user_id, username, password_hash, full_name, email, role_name
                FROM users
                join roles on roles.role_id = users.role_id
                WHERE email = :email
                """;

        try {
            User user = jdbcTemplate.queryForObject(sql, Map.of("email", email), new UserMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Log
    public void save(User user) {

        final String sql = "INSERT INTO users (username, password_hash, full_name, email, role_id) VALUES (:username, :passwordHash, :fullName, :email, (select role_id from roles where name = :role))";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("passwordHash", user.getPassword())
                .addValue("fullName", user.getFullName())
                .addValue("email", user.getEmail())
                .addValue("role", user.getRole().toString());

        jdbcTemplate.update(sql, params);
    }

    public int updateUser(Long userId, User updatedUser) {
        final String sql = """
                UPDATE users
                SET username = :username, password_hash = :passwordHash, full_name = :fullName, email = :email, role = :role
                WHERE user_id = :userId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", updatedUser.getUsername())
                .addValue("passwordHash", updatedUser.getPasswordHash())
                .addValue("fullName", updatedUser.getFullName())
                .addValue("email", updatedUser.getEmail())
                .addValue("role", updatedUser.getRole())
                .addValue("userId", userId);

        return jdbcTemplate.update(sql, params);
    }

    public List<User> findAll(String role) {
        final String sql = (role != null) ? """
                SELECT user_id, username, password_hash, full_name, email, role
                FROM users
                WHERE role = :role
                """ : """
                SELECT user_id, username, password_hash, full_name, email, role
                FROM users
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        if (role != null) {
            params.addValue("role", role);
        }

        return jdbcTemplate.query(sql, params, new UserMapper());
    }
}