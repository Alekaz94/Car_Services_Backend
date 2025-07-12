package com.example.demo.Controllers;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.UserRequest;
import com.example.demo.Entities.User;
import com.example.demo.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("Received login: " + loginRequest.getEmail() + " / " + loginRequest.getPassword());
        return userService.login(loginRequest);
    }

    @PostMapping("/signup")
    public User signUpUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.signUp(userRequest);
    }
}
