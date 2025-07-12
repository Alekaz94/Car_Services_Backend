package com.example.demo.DTO;

import jakarta.validation.constraints.NotNull;

public record CarRequest(@NotNull String make,
                         @NotNull String model,
                         @NotNull String licencePlate,
                         @NotNull int year,
                         @NotNull int milage,
                         @NotNull int price) {

}
