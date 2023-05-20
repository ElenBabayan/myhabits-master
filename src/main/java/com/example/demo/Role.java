package com.example.demo;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public static Role getByName(String role) {
        return Arrays.stream(values()).filter(it -> it.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid Role"));
    }
}
