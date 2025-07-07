package com.example.demo.Controllers;

import com.example.demo.DTO.LoginDTO;
import com.example.demo.Entities.User;
import com.example.demo.Services.UserService;
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
    public User loginUser(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }
}
