package ru.itpark.mashacursah.controllers.http.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itpark.mashacursah.controllers.http.auth.dto.AuthCredentials;
import ru.itpark.mashacursah.controllers.http.auth.dto.JwtTokenResponse;
import ru.itpark.mashacursah.controllers.http.auth.dto.SignUpDto;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.auth.AuthService;

@RequestMapping("api/auth")
@RestController
@Log
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public JwtTokenResponse auth(@RequestBody AuthCredentials credentials) {
        return authService.authorize(credentials);
    }

    @PostMapping("/sign-up")
    public JwtTokenResponse signUp(@RequestBody SignUpDto dto) {
        return authService.signUp(dto);
    }
}
