package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        authService.register(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {

        return authService.login(
                loginDTO.getUsername(),
                loginDTO.getPassword());
    }
}