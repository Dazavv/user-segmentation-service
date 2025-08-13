package com.example.authorization.repository;

import com.example.authorization.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}