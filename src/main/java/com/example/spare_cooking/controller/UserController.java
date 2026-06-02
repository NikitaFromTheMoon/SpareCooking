package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Comment;
import com.example.spare_cooking.model.table.WebUser;
import com.example.spare_cooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forum/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public WebUser getCurrentUser() {
        return userService.getCurrentWebUser();
    }

    @PostMapping("/init")
    public WebUser initUser() {
        return userService.getCurrentWebUser();
    }

    @GetMapping("/users/{id}")
    public List<Comment> getUserComments(@PathVariable long id) {
        return null;
    }
}

