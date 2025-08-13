package com.example.authorization.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    VIEWER("VIEWER"),
    ADMIN("ADMIN"),
    ANALYST("ANALYST");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}