package com.example.demo.DTO;

import com.example.demo.Entities.User;

public record CarUpdateRequest(String licencePlate,
                               int milage,
                               int price,
                               User user) {
}
