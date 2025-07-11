package com.example.demo.DTO;

public record UserRequest(String firstName,
                          String lastName,
                          String email,
                          String password) {
}
