package ru.itpark.mashacursah.infrastructure.repository.user;

import org.springframework.jdbc.core.RowMapper;
import ru.itpark.mashacursah.entity.Role;
import ru.itpark.mashacursah.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .userId(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .passwordHash(rs.getString("password_hash"))
                .fullName(rs.getString("full_name"))
                .email(rs.getString("email"))
                .role(Role.valueOf(rs.getString("role_name").toUpperCase()))
                .build();
    }
}