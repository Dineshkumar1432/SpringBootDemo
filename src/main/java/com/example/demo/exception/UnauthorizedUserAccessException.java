package com.example.demo.exception;

public class UnauthorizedUserAccessException extends RuntimeException {

    public UnauthorizedUserAccessException(String message) {
        super(message);
    }
}