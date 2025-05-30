package ru.itpark.mashacursah.controllers.http.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itpark.mashacursah.controllers.http.auth.dto.*;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.auth.AuthService;

@RequestMapping("api/auth")
@RestController
@Log
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public AuthResponse auth(@RequestBody AuthCredentials credentials) {
        return authService.authorize(credentials);
    }

    @PostMapping("/sign-up")
    public AuthResponse signUp(@RequestBody SignUpDto dto) {
        return authService.signUp(dto);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }
}
