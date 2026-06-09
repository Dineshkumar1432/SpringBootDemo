package com.example.demo.dto;

public class AuthResponse {

    private String token;
    private String role;
    private Long userId;

    public AuthResponse(String token, String role, int userId) {
        this.token = token;
        this.role = role;
        this.userId = (long) userId;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }
}