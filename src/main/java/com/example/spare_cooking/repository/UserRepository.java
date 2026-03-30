package com.example.spare_cooking.repository;

import com.example.spare_cooking.model.table.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPasswordHashed(String username, String passwordHashed);
}