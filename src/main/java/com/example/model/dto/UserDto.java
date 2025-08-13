package com.example.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String surname;
    private Set<String> segmentsCode;
}
