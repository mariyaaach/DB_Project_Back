package ru.itpark.mashacursah.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.entity.user.User;
import ru.itpark.mashacursah.infrastructure.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll(String role) {
        return userRepository.findAll(role);
    }

    public void updateUser(Long id, User updatedUser) {
        userRepository.updateUser(id, updatedUser);
    }
}
