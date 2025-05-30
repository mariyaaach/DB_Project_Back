package ru.itpark.mashacursah.controllers.http.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.controllers.http.user.dto.GetUserDto;
import ru.itpark.mashacursah.controllers.http.user.dto.UpdateUserDto;
import ru.itpark.mashacursah.controllers.http.user.dto.UserMapper;
import ru.itpark.mashacursah.entity.user.User;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping("/{id}")
    public GetUserDto getUserById(@PathVariable Long id) {
        return mapper.toDto(
                userService.findById(id).orElse(null)
        );
    }

    @GetMapping("/username/{username}")
    public GetUserDto getUserByUsername(@PathVariable String username) {
        return mapper.toDto(
                userService.findByUsername(username).orElse(null)
        );
    }

    @GetMapping("/email/{email}")
    public GetUserDto getUserByEmail(@PathVariable String email) {
        return mapper.toDto(
                userService.findByEmail(email).orElse(null)
        );
    }

    @GetMapping
    public List<GetUserDto> getAllUsers(@RequestParam(required = false) String role) {
        return mapper.toDto(
                userService.findAll(role)
        );
    }


    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updatedUser) {
        User entity = mapper.toEntity(updatedUser);
        userService.updateUser(id, entity);
    }
}

