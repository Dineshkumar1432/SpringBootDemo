package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginDTO;

import com.example.demo.model.User;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.core.Authentication;
import com.example.demo.repository.UserRepository;
import com.example.demo.serviceImpl.AuthService;
import com.example.demo.serviceImpl.UserServiceImpl;
import com.example.demo.util.JwtUtil;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;

    public AuthController(AuthService authService, UserServiceImpl userService,
            AuthenticationManager authenticationManager,
            UserRepository userRepository, JwtUtil jwtService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {

        userService.addUser(user);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Entered password: " + request.getPassword());
        System.out.println("Stored password: " + user.getPassword());
        System.out.println("Match: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", jwtService.extractRole(token), // ✅ MUST RETURN
                "userId", user.getId() // ✅ MUST RETURN
        ));
    }
}