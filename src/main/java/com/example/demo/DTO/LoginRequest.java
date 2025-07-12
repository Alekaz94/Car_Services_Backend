package com.example.demo.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class LoginRequest {
    @NotNull String email;
    @NotNull
    String password;
}
