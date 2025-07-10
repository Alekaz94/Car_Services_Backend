package com.example.demo.DTO;

import lombok.Value;

@Value
public class LoginRequest {
    String email;
    String password;
}
