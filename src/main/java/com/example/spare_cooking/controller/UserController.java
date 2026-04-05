package com.example.spare_cooking.controller;

import com.example.spare_cooking.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String getUserByNameAndPassword(@RequestParam String username,
                                           @RequestParam String passwordHashed) {
        return userService.getUserByNameAndPassword(username, passwordHashed);
    }
}
