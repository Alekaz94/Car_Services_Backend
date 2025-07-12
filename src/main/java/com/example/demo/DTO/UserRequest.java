package com.example.demo.DTO;

import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotNull String firstName,
                          @NotNull String lastName,
                          @NotNull String email,
                          @NotNull String password) {
}
