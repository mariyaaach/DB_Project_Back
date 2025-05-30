package ru.itpark.mashacursah.infrastructure.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
                SELECT user_id, username, password_hash, full_name, email, roles.name as role_name
                FROM users 
                JOIN roles  on users.role_id = roles.role_id
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
                SELECT user_id, username, password_hash, full_name, email, roles.name as role_name
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

    @Transactional
    public void save(User user) {
        String roleName = user.getRole().toString();
        MapSqlParameterSource roleParams = new MapSqlParameterSource().addValue("role", roleName);

        // Проверяем, существует ли роль
        String selectRoleSql = "SELECT role_id FROM roles WHERE name = :role";
        Optional<Integer> roleIdOpt = jdbcTemplate.query(
                selectRoleSql,
                roleParams,
                rs -> rs.next() ? Optional.of(rs.getInt("role_id")) : Optional.empty()
        );

        Integer roleId;
        if (roleIdOpt.isEmpty()) {
            // Роль не существует, вставляем её
            String insertRoleSql = "INSERT INTO roles (name) VALUES (:role) RETURNING role_id";
            roleId = jdbcTemplate.queryForObject(insertRoleSql, roleParams, Integer.class);
        } else {
            roleId = roleIdOpt.get();
        }

        // Вставляем пользователя с полученным role_id
        String insertUserSql = "INSERT INTO users (username, password_hash, full_name, email, role_id) " +
                "VALUES (:username, :passwordHash, :fullName, :email, :roleId)";
        MapSqlParameterSource userParams = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("passwordHash", user.getPassword()) // Чистый пароль
                .addValue("fullName", user.getFullName())
                .addValue("email", user.getEmail())
                .addValue("roleId", roleId);



        jdbcTemplate.update(insertUserSql, userParams);

    }

    public int updateUser(Long userId, User updatedUser) {
        final String sql = """
                UPDATE users
                SET username = :username, password_hash = :passwordHash, full_name = :fullName, email = :email, role_id = (select role_id from roles where name = :role)
                WHERE user_id = :userId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", updatedUser.getUsername())
                .addValue("passwordHash", updatedUser.getPasswordHash())
                .addValue("fullName", updatedUser.getFullName())
                .addValue("email", updatedUser.getEmail())
                .addValue("role", updatedUser.getRole().toString())
                .addValue("userId", userId);

        return jdbcTemplate.update(sql, params);
    }

    public List<User> findAll(String role) {
        final String sql = (role != null) ? """
                SELECT user_id, username, password_hash, full_name, email, roles.name as role_name
                FROM users
                join roles
                WHERE roles.name = :role
                """ : """
                SELECT user_id, username, password_hash, full_name, email, roles.name as role_name
                FROM users
                join roles
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        if (role != null) {
            params.addValue("role", role);
        }

        return jdbcTemplate.query(sql, params, new UserMapper());
    }
}