package com.example.spare_cooking.service;

import com.example.spare_cooking.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserByNameAndPassword(String username, String passwordHashed) {
        return userRepository.findByUsernameAndPasswordHashed(username, passwordHashed)
                .map(user -> "Пользователь найден: " + user.getUsername())
                .orElse("Пользователь не найден");
    }
}
