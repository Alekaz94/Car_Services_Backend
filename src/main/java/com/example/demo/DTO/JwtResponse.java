package com.example.demo.DTO;

import com.example.demo.Entities.User;
import lombok.Data;

@Data
public class JwtResponse {
    private User user;
    private String token;

    public JwtResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
