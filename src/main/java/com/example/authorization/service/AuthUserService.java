package com.example.authorization.service;


import com.example.authorization.model.AuthUser;
import com.example.authorization.model.Role;
import com.example.authorization.repository.AuthRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    private final AuthRepository authRepository;

    public Optional<AuthUser> getByLogin(String login) {
        return authRepository.findByLogin(login);
    }

    public boolean checkExistedUser(String login, String email) {
        return authRepository.existsByLogin(login) || authRepository.existsByEmail(email);
    }

    public void saveNewUser(AuthUser user) {
        authRepository.save(user);
    }

    public void addNewRole(AuthUser user, Role role) {
        user.getRoles().add(role);
        authRepository.save(user);
    }
}
