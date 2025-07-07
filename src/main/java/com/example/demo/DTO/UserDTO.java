package com.example.demo.DTO;

import lombok.Value;

@Value
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
