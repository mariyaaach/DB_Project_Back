package ru.itpark.mashacursah.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itpark.mashacursah.entity.Role;

import java.util.Collection;
import java.util.List;

@Data
@Table("users")
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    private Integer userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private Role role;

    public static User createNew(String username, String passwordHash, String fullName, String email, Role role) {
        return User.builder()
                .email(email)
                .fullName(fullName)
                .passwordHash(passwordHash)
                .username(username)
                .role(role)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
