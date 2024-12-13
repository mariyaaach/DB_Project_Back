package ru.itpark.mashacursah.infrastructure.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.CorsFilter;
import ru.itpark.mashacursah.infrastructure.configuration.security.filters.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CorsFilter corsFilter, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.corsFilter = corsFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    private static final String[] WHITELIST = {"/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter, SessionManagementFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}