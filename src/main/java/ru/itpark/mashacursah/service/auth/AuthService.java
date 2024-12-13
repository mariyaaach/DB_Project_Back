package ru.itpark.mashacursah.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.controllers.http.auth.dto.AuthCredentials;
import ru.itpark.mashacursah.controllers.http.auth.dto.JwtTokenResponse;
import ru.itpark.mashacursah.controllers.http.auth.dto.SignUpDto;
import ru.itpark.mashacursah.entity.Role;
import ru.itpark.mashacursah.entity.user.User;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.infrastructure.configuration.security.providers.JwtTokenProvider;
import ru.itpark.mashacursah.infrastructure.exceptions.base.business.BusinessException;
import ru.itpark.mashacursah.infrastructure.exceptions.base.business.BusinessExceptionCode;
import ru.itpark.mashacursah.infrastructure.exceptions.base.validation.ValidationException;
import ru.itpark.mashacursah.infrastructure.exceptions.base.validation.ValidationExceptionCode;
import ru.itpark.mashacursah.infrastructure.repository.user.UserRepository;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Log
    public JwtTokenResponse authorize(AuthCredentials credentials) {
        var user = userRepository.findByUsername(credentials.username())
                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) {
            throw new ValidationException(ValidationExceptionCode.ACCESS_DENIED);
        }

        var token = jwtTokenProvider.generateToken(credentials.username());

        return new JwtTokenResponse(token);
    }

    public JwtTokenResponse signUp(SignUpDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new BusinessException(BusinessExceptionCode.USER_ALREADY_EXISTS);
        }

        userRepository.save(User.createNew(
                dto.username(),
                passwordEncoder.encode(dto.password()),
                dto.fullName(),
                dto.email(),
                dto.role()
        ));

        var token = jwtTokenProvider.generateToken(dto.username());

        return new JwtTokenResponse(token);
    }
}
