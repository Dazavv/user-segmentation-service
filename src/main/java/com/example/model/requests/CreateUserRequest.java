package com.example.model.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    private String email;
    private String name;
    private String surname;
}
