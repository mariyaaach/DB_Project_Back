package ru.itpark.mashacursah.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.controllers.http.auth.dto.*;
import ru.itpark.mashacursah.entity.Role;
import ru.itpark.mashacursah.entity.user.User;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.infrastructure.configuration.security.providers.JwtTokenProvider;
import ru.itpark.mashacursah.infrastructure.exceptions.base.business.BusinessException;
import ru.itpark.mashacursah.infrastructure.exceptions.base.business.BusinessExceptionCode;
import ru.itpark.mashacursah.infrastructure.exceptions.base.validation.ValidationException;
import ru.itpark.mashacursah.infrastructure.exceptions.base.validation.ValidationExceptionCode;
import ru.itpark.mashacursah.infrastructure.repository.user.UserRepository;
import ru.itpark.mashacursah.service.redis.RefreshTokenService;
import ru.itpark.mashacursah.service.redis.dto.RefreshToken;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Log
    public AuthResponse authorize(AuthCredentials credentials) {
        var user = userRepository.findByUsername(credentials.username())
                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) {
            throw new ValidationException(ValidationExceptionCode.ACCESS_DENIED);
        }

        var token  = jwtTokenProvider.generateToken(credentials.username());

        String stringToken = jwtTokenProvider.generateRefreshToken(credentials.username());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(credentials.username(), stringToken);

        return new AuthResponse(token, stringToken);
    }

    public AuthResponse signUp(SignUpDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new BusinessException(BusinessExceptionCode.USER_ALREADY_EXISTS);
        }

        userRepository.save(User.createNew(
                dto.username(),
                dto.password(), // хэшируем на уровне базы через триггер
                dto.fullName(),
                dto.email(),
                dto.role()
        ));

        var token = jwtTokenProvider.generateToken(dto.username());

        String stringToken = jwtTokenProvider.generateRefreshToken(dto.username());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(dto.username(), stringToken);
        // тут сохраняется токен в редис по имени пользователя и на 7 дней

        return new AuthResponse(token, stringToken);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new ValidationException(ValidationExceptionCode.INVALID_REFRESH_TOKEN);
        }

        RefreshToken storedToken = refreshTokenService.findByToken(refreshToken);
        if (storedToken == null) {
            throw new ValidationException(ValidationExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // Удаляем старый refresh токен
        refreshTokenService.deleteByToken(refreshToken);

        // Генерируем новые токены
        String newAccessToken = jwtTokenProvider.generateToken(storedToken.getUsername());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(storedToken.getUsername());
        refreshTokenService.createRefreshToken(storedToken.getUsername(), newRefreshToken);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
