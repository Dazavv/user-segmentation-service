package com.example.authorization.dto;

import com.example.authorization.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRoleToUserRequest {
    private String login;
    private Role role;
}
