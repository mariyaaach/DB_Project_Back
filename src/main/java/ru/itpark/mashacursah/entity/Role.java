package ru.itpark.mashacursah.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    RESEARCHER("Научный сотрудник"),
    PROJECT_MANAGER("Руководитель проекта"),
    ADMIN("Администратор");

    String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
