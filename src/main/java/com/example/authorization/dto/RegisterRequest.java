package com.example.authorization.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}