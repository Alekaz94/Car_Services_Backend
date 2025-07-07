package com.example.demo.Authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoding {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        if(password == null || password.isEmpty()) {
            throw new IllegalArgumentException("No valid password was given!");
        }
        return bCryptPasswordEncoder.encode(password);
    }

    public Boolean checkHashedPassword(String password, String hashedPassword) {
        boolean passwordCheck = bCryptPasswordEncoder.matches(password, hashedPassword);

        return passwordCheck;
    }
}
