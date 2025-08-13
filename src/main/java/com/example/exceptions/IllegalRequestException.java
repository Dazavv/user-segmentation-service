package com.example.exceptions;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException(String message) {
        super(message);
    }
}
