package com.example.spare_cooking.controller;

import com.example.spare_cooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return userService.getCurrentUser(jwt);
    }
}