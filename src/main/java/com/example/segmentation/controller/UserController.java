package com.example.segmentation.controller;

import com.example.segmentation.model.dto.UserDto;
import com.example.segmentation.model.requests.CreateUserRequest;
import com.example.segmentation.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST')")
    public List<String> getUsersSegments(@PathVariable @Min(1) Long id) {
        return userService.getUsersSegments(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserDto addUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.addUser(request.getEmail(), request.getName(), request.getSurname());
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteUser(@PathVariable @Min(1) Long id) {
        userService.deleteUser(id);
    }
}
