package com.example.demo.DTO;

import com.example.demo.Enums.Status;
import jakarta.validation.constraints.NotNull;

public record RepairUpdateRequest(@NotNull Status status) {
}
