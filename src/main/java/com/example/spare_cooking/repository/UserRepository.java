package com.example.spare_cooking.repository;

import com.example.spare_cooking.model.table.WebUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<WebUser, Long> {

    Optional<WebUser> findByKeyCUserId(String keycloakUserId);
    Page<WebUser> findAll(Pageable pageable);

}