package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.WebUser;
import com.example.spare_cooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return userService.getCurrentUser(jwt);
    }

}

