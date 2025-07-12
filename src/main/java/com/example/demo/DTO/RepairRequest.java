package com.example.demo.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record RepairRequest(@NotNull UUID carId,
                            @NotNull UUID mechanicId,
                            @NotNull String issueDescription,
                            @PositiveOrZero double cost) {
}
