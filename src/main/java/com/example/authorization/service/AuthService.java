package com.example.authorization.service;
import com.example.authorization.dto.ChangeRoleRequest;
import com.example.authorization.dto.JwtResponse;
import com.example.authorization.dto.LoginRequest;
import com.example.authorization.dto.RegisterRequest;
import com.example.authorization.exceptions.AuthException;
import com.example.authorization.exceptions.RegisterException;
import com.example.authorization.jwt.JwtAuthentication;
import com.example.authorization.model.AuthUser;
import com.example.authorization.model.Role;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthUserService authUserService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse login(@NonNull LoginRequest authRequest) {
        final AuthUser user = getUser(authRequest.getLogin());
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Password is wrong");
        }
    }

    public JwtResponse register(@NonNull RegisterRequest registerRequest) {
        if (authUserService.checkExistedUser(registerRequest.getLogin(), registerRequest.getEmail())) {
            throw new RegisterException("User with login: " + registerRequest.getLogin() + " already exists");
        }

        AuthUser user = new AuthUser();
        user.setLogin(registerRequest.getLogin());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setRoles(Collections.singleton(Role.ANALYST));

        authUserService.saveNewUser(user);

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getLogin(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse logout(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                refreshStorage.remove(login);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final AuthUser user = getUser(login);
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final AuthUser user = getUser(login);
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("JWT was not valid");
    }
    public void changeRoleToUser(@NonNull ChangeRoleRequest request) {
        final AuthUser user = getUser(request.getLogin());
        authUserService.addNewRole(user, request.getRole());
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public AuthUser getUser(String login) {
        return authUserService.getByLogin(login)
                .orElseThrow(() -> new AuthException("User was not found"));
    }
}