package com.example.demo.DTO;

import com.example.demo.Entities.User;
import jakarta.validation.constraints.NotNull;

public record CarUpdateRequest(@NotNull String licencePlate,
                               @NotNull int milage,
                               @NotNull int price,
                               @NotNull User user) {
}
