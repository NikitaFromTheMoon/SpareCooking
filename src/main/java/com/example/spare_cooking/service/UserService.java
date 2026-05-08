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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public WebUser getCurrentWebUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return getOrCreateCurrentUser(jwt);
        }

        throw new RuntimeException("Пользователь не авторизован в системе");
    }

    @Transactional
    public WebUser getOrCreateCurrentUser(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();

        return userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseGet(() -> {
                    WebUser webUser = new WebUser();
                    webUser.setKeycloakUserId(keycloakUserId);
                    webUser.setUsername(jwt.getClaimAsString("preferred_username"));
                    webUser.setEmail(jwt.getClaimAsString("email"));
                    webUser.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(webUser);
                });
    }
}