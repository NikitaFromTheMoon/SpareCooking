package com.example.spare_cooking.service;

import com.example.spare_cooking.model.table.WebUser;
import com.example.spare_cooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public WebUser getOrCreateCurrentUser(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");

        return userRepository.findByKeyCUserId(keycloakUserId)
                .orElseGet(() -> {
                    WebUser webUser = new WebUser();
                    webUser.setKeycloakUserId(keycloakUserId);
                    webUser.setUsername(username);
                    webUser.setEmail(email);
                    webUser.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(webUser);
                });
    }

    public Map<String, Object> getCurrentUser(Jwt jwt) {
        WebUser webUser = getOrCreateCurrentUser(jwt);

        return Map.of(
                "id", webUser.getId(),
                "keycloakUserId", webUser.getKeycloakUserId(),
                "username", webUser.getUsername(),
                "email", webUser.getEmail()
        );
    }
}