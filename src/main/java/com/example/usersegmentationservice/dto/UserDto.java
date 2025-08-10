package com.example.usersegmentationservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private Set<Long> segmentIds;
}
