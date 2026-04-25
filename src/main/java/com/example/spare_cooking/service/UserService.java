package com.example.spare_cooking.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    public Map<String, Object> getCurrentUser(Jwt jwt) {
        return Map.of(
                "username", jwt.getClaimAsString("preferred_username")
        );
    }
}