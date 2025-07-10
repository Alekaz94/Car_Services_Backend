package com.example.demo.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoding {
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoding(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hashPassword(String password) {
        if(password == null || password.isEmpty()) {
            throw new IllegalArgumentException("No valid password was given!");
        }
        return passwordEncoder.encode(password);
    }

    public Boolean checkHashedPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
